package br.edu.fatecgru.mvcaluno.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.edu.fatecgru.mvcaluno.model.Disciplina;
import br.edu.fatecgru.mvcaluno.model.MatriculaDisciplina;
import br.edu.fatecgru.mvcaluno.model.MatriculaDisciplinaDetalhe;
import br.edu.fatecgru.mvcaluno.util.ConnectionFactory;

public class MatriculaDisciplinaDAO {

    private final double NOTA_MINIMA_APROVACAO = 6.0;
    private final int MAX_FALTAS_PERMITIDAS = 20;

    // ===========================================
    // CÁLCULOS DE SEMESTRE
    // ===========================================

    public String calcularProximoSemestreLetivo(String semestreAtual) {
        if (semestreAtual == null || !semestreAtual.contains("/")) {
            return LocalDate.now().getYear() + "/2";
        }
        String[] partes = semestreAtual.split("/");
        int ano = Integer.parseInt(partes[0]);
        int semestre = Integer.parseInt(partes[1]);
        return (semestre == 1) ? (ano + "/2") : ((ano + 1) + "/1");
    }

    
    public int calcularProximoSemestreCurso(int idMatricula) throws Exception {
        int semestreAtual = 1;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT MAX(d.semestre) AS semestreAtual " +
                 "FROM matriculaDisciplina md " +
                 "JOIN disciplina d ON md.idDisciplina = d.idDisciplina " +
                 "WHERE md.idMatricula = ?"
             )) {
            ps.setInt(1, idMatricula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    semestreAtual = rs.getInt("semestreAtual");
                }
            }
        }

        // Se o aluno já está no semestre 3, não cria mais matrículas
        if (semestreAtual >= 3) {
            return -1; // ou 0 para indicar "fim do curso"
        }

        return semestreAtual + 1; //retorna o próximo semestre do curso
    }

    
    
    
    public boolean temDisciplinaCursando(int idAluno, String semestreCursado) throws Exception {
        String sql = "SELECT COUNT(*) AS qtd " +
                     "FROM matriculaDisciplina MD " +
                     "JOIN matricula M ON MD.idMatricula = M.idMatricula " +
                     "WHERE M.idAluno = ? AND MD.semestreCursado = ? " +
                     "AND MD.status = 'Cursando' AND MD.ativo = TRUE";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAluno);
            ps.setString(2, semestreCursado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("qtd") > 0;
                }
            }
        }
        return false;
    }

    public List<Disciplina> listarDisciplinasProximoSemestre(int idCurso, int proximoSemestre, int idMatricula) throws Exception {
        List<Disciplina> disciplinas = new ArrayList<>();
        String sql = "SELECT * FROM disciplina D " +
                     "WHERE D.idCurso=? AND D.semestre=? AND D.ativo=TRUE " +
                     "AND D.idDisciplina NOT IN (SELECT MD.idDisciplina FROM matriculaDisciplina MD WHERE MD.idMatricula=? AND MD.ativo=TRUE)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCurso);
            ps.setInt(2, proximoSemestre);
            ps.setInt(3, idMatricula);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Disciplina d = new Disciplina();
                    d.setIdDisciplina(rs.getInt("idDisciplina"));
                    d.setNome(rs.getString("nome"));
                    d.setSemestre(rs.getInt("semestre"));
                    disciplinas.add(d);
                }
            }
        }
        return disciplinas;
    }
    
    public String calcularSemestreAtual() {
        LocalDate hoje = LocalDate.now();
        int ano = hoje.getYear();
        int semestre = (hoje.getMonthValue() <= 6) ? 1 : 2;
        return ano + "/" + semestre;
    }


    // ===========================================
    // MÉTODOS DE NOTAS E FALTAS
    // ===========================================

    private String calcularStatusFinal(double nota, int faltas) {
        if (nota == 0 && faltas == 0) return "Cursando";
        if (nota < NOTA_MINIMA_APROVACAO || faltas > MAX_FALTAS_PERMITIDAS) return "Reprovado";
        return "Aprovado";
    }

    public void atribuirTemporariamenteNotaFaltas(MatriculaDisciplina md) throws Exception {
        String status = calcularStatusFinal(md.getNota(), md.getFaltas());
        String SQL = "UPDATE matriculaDisciplina SET nota=?, faltas=?, status=? WHERE idMatriculaDisciplina=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL)) {
            ps.setDouble(1, md.getNota());
            ps.setInt(2, md.getFaltas());
            ps.setString(3, status);
            ps.setInt(4, md.getIdMatriculaDisciplina());

            if (ps.executeUpdate() == 0) {
                throw new Exception("Nenhuma nota/falta atualizada. Registro não encontrado.");
            }
        }
    }

    public void finalizarNotaFaltas(MatriculaDisciplina md) throws Exception {
        atribuirTemporariamenteNotaFaltas(md); // mesmo comportamento
    }

    public MatriculaDisciplina buscarNotaFaltas(int idMatricula, int idDisciplina, String semestreCursado) throws Exception {
        MatriculaDisciplina md = null;
        String SQL = "SELECT * FROM matriculaDisciplina WHERE idMatricula=? AND idDisciplina=? AND semestreCursado=? AND ativo=TRUE";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL)) {
            ps.setInt(1, idMatricula);
            ps.setInt(2, idDisciplina);
            ps.setString(3, semestreCursado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    md = new MatriculaDisciplina();
                    md.setIdMatriculaDisciplina(rs.getInt("idMatriculaDisciplina"));
                    md.setIdMatricula(rs.getInt("idMatricula"));
                    md.setIdDisciplina(rs.getInt("idDisciplina"));
                    md.setSemestreCursado(rs.getString("semestreCursado"));
                    md.setFaltas(rs.getInt("faltas"));
                    md.setNota(rs.getDouble("nota"));
                    md.setStatus(rs.getString("status"));
                    md.setAtivo(rs.getBoolean("ativo"));
                }
            }
        }
        return md;
    }

    // ===========================================
    // MÉTODOS DE POPULAÇÃO DE UI
    // ===========================================

    public List<String> listarSemestresCursados(int idAluno) throws Exception {
        List<String> semestres = new ArrayList<>();
        String sql = "SELECT DISTINCT semestreCursado FROM matriculaDisciplina " +
                     "WHERE idMatricula = ? AND ativo = TRUE ORDER BY semestreCursado";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAluno);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    semestres.add(rs.getString("semestreCursado"));
                }
            }
        }
        return semestres;
    }


    public List<MatriculaDisciplinaDetalhe> listarDisciplinasParaAlunoNoSemestre(int idAluno, String semestre) throws Exception {
        List<MatriculaDisciplinaDetalhe> detalhes = new ArrayList<>();
        String SQL = "SELECT D.idDisciplina, D.nome AS nomeDisciplina, MD.idMatriculaDisciplina " +
                     "FROM matriculaDisciplina MD " +
                     "JOIN matricula M ON MD.idMatricula = M.idMatricula " +
                     "JOIN Disciplina D ON MD.idDisciplina = D.idDisciplina " +
                     "WHERE M.idAluno=? AND MD.semestreCursado=? AND MD.ativo=TRUE " +
                     "ORDER BY D.nome";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL)) {
            ps.setInt(1, idAluno);
            ps.setString(2, semestre);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MatriculaDisciplinaDetalhe detalhe = new MatriculaDisciplinaDetalhe();
                    detalhe.setIdDisciplina(rs.getInt("idDisciplina"));
                    detalhe.setNomeDisciplina(rs.getString("nomeDisciplina"));
                    detalhe.setIdMatriculaDisciplina(rs.getInt("idMatriculaDisciplina"));
                    detalhes.add(detalhe);
                }
            }
        }
        return detalhes;
    }

    // ===========================================
    // MÉTODOS NOVOS PARA SUA VIEW
    // ===========================================

    /** Verifica se existem disciplinas com status 'Cursando' */
    public boolean verificarPendencias(int idMatricula) throws Exception {
        String SQL = "SELECT COUNT(*) FROM matriculaDisciplina WHERE idMatricula=? AND status='Cursando' AND ativo=TRUE";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL)) {
            ps.setInt(1, idMatricula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    public List<MatriculaDisciplina> listarDisciplinasPorStatus(int idMatricula, String status) throws Exception {
        List<MatriculaDisciplina> lista = new ArrayList<>();
        
        String sql = "SELECT * FROM matriculaDisciplina WHERE idMatricula = ? AND status = ? AND ativo = TRUE";
        try (Connection conn = ConnectionFactory.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMatricula);
            ps.setString(2, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MatriculaDisciplina md = new MatriculaDisciplina();
                md.setIdMatriculaDisciplina(rs.getInt("idMatriculaDisciplina"));
                md.setIdDisciplina(rs.getInt("idDisciplina"));
                md.setNota(rs.getDouble("nota"));
                md.setFaltas(rs.getInt("faltas"));
                md.setStatus(rs.getString("status"));
                md.setSemestreCursado(rs.getString("semestreCursado"));
                lista.add(md);
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao listar disciplinas por status.", e);
        }
        return lista;
    }

    public void matricularDisciplinaNoSemestre(int idDisciplina, int idMatricula, String semestre) throws Exception {
    	Connection conn = ConnectionFactory.getConnection();

    	String sql = "INSERT INTO matriculaDisciplina (idMatricula, idDisciplina, semestreCursado, faltas, nota, status, ativo) "
                   + "VALUES (?, ?, ?, 0, 0.0, 'Cursando', TRUE)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMatricula);
            ps.setInt(2, idDisciplina);
            ps.setString(3, semestre);
            ps.executeUpdate();
        }
    }
    
    public List<Integer> listarDisciplinasPorSemestreCurso(int idCurso, String semestre) throws Exception {
        List<Integer> lista = new ArrayList<>();
        int semestreNum;

        if (semestre.contains("/")) {
            // Ex: "2025/2"
            semestreNum = Integer.parseInt(semestre.split("/")[1]);
        } else {
            // Ex: "3" → já é o número do semestre
            semestreNum = Integer.parseInt(semestre);
        }

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT idDisciplina FROM disciplina WHERE idCurso = ? AND semestre = ? AND ativo = TRUE"
             )) {
            ps.setInt(1, idCurso);
            ps.setInt(2, semestreNum);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(rs.getInt("idDisciplina"));
                }
            }
        }
        return lista;
    }

public boolean jaMatriculado(int idMatricula, Integer idDisciplina, String semestre) throws Exception {
    	
    	String sql = "SELECT COUNT(*) FROM matriculaDisciplina WHERE idMatricula = ? AND idDisciplina = ? AND semestreCursado = ? AND ativo = TRUE";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMatricula);
            ps.setInt(2, idDisciplina);
            ps.setString(3, semestre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new Exception("Erro ao verificar matrícula existente.", e);
        }
    }
    
/** Verifica se ainda existem disciplinas com status 'Cursando' no semestre específico. */
public boolean verificarPendenciasNoSemestre(int idMatricula, String semestre) throws Exception {
    String SQL = "SELECT COUNT(*) FROM matriculaDisciplina WHERE idMatricula=? AND semestreCursado=? AND status='Cursando' AND ativo=TRUE";
    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement ps = conn.prepareStatement(SQL)) {
        ps.setInt(1, idMatricula);
        ps.setString(2, semestre);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1) > 0;
        }
    }
    return false;
}

public List<String> processarFimSemestre(int idMatricula, int idCurso, String semestreAtualAluno) throws Exception {
    List<String> disciplinasMatriculadas = new ArrayList<>();
    Connection conn = null;

    try {
        // 0️ VERIFICAÇÃO DE PENDÊNCIAS (NOVO PASSO)
        if (verificarPendenciasNoSemestre(idMatricula, semestreAtualAluno)) {
            throw new Exception("Não é possível finalizar o semestre. Existem disciplinas pendentes ('Cursando') sem nota ou falta atribuídas.");
        }
        
        conn = ConnectionFactory.getConnection();
        conn.setAutoCommit(false); // Inicia a transação

        // 1️Atualizar status (Aprovado/Reprovado) das disciplinas do semestre encerrado
        String sqlUpdateStatus = "UPDATE matriculaDisciplina SET status = CASE " +
                                 "WHEN nota >= ? AND faltas <= ? THEN 'Aprovado' " +
                                 "ELSE 'Reprovado' END " +
                                 "WHERE idMatricula = ? AND semestreCursado = ? AND ativo = TRUE AND status = 'Cursando'";
        try (PreparedStatement ps = conn.prepareStatement(sqlUpdateStatus)) {
            ps.setDouble(1, NOTA_MINIMA_APROVACAO);
            ps.setInt(2, MAX_FALTAS_PERMITIDAS);
            ps.setInt(3, idMatricula);
            ps.setString(4, semestreAtualAluno);
            ps.executeUpdate();
        }

        // 2️ Calcular próximo semestre letivo (ex: "2025/1" → "2025/2")
        String proximoSemestreLetivo = calcularProximoSemestreLetivo(semestreAtualAluno);

        // 3️ Buscar disciplinas reprovadas (que precisam ser repetidas)
        List<MatriculaDisciplina> reprovadas = listarDisciplinasPorStatus(idMatricula, "Reprovado");
        Set<Integer> idsReprovadas = reprovadas.stream()
                .map(MatriculaDisciplina::getIdDisciplina)
                .collect(Collectors.toSet());

        // 4️ Buscar disciplinas aprovadas (para evitar matrícula duplicada)
        List<MatriculaDisciplina> aprovadas = listarDisciplinasPorStatus(idMatricula, "Aprovado");
        Set<Integer> idsAprovadas = aprovadas.stream()
                .map(MatriculaDisciplina::getIdDisciplina)
                .collect(Collectors.toSet());

     // 5️ Determinar qual é o próximo semestre da grade curricular do curso
        int proximoSemestreCurso = calcularProximoSemestreCurso(idMatricula);

        // 6️ Determinar disciplinas para matrícula (AVANÇA E REPETE PENDÊNCIAS)
        List<Integer> disciplinasParaMatricular = new ArrayList<>();

        // Se proximoSemestreCurso é 3, o semestre alvo para busca de pendências é 3, 
        // o que significa que devemos verificar os semestres 1 e 2.
        int semestreAlvoParaPendencias = (proximoSemestreCurso != -1) ? proximoSemestreCurso : 
                                         (idsAprovadas.isEmpty() ? 1 : calcularMaxSemestreAprovado(idsAprovadas, idCurso) + 1);


        // ===============================================
        // 6.1: Adiciona TODAS as disciplinas pendentes de semestres anteriores (REPETIÇÃO/PENDÊNCIAS)
        // ===============================================

        // Busca todas as IDs de disciplinas obrigatórias dos semestres anteriores ao alvo.
        List<Integer> idsObrigatoriasAnteriores = listarDisciplinasSemestreAte(idCurso, semestreAlvoParaPendencias); 

        for (Integer idDisc : idsObrigatoriasAnteriores) {
            // Se não foi aprovada E não está na lista de matrícula E não está matriculada neste semestre letivo
            if (!idsAprovadas.contains(idDisc) &&
                !disciplinasParaMatricular.contains(idDisc) &&
                !jaMatriculado(idMatricula, idDisc, proximoSemestreLetivo)) {
                
                disciplinasParaMatricular.add(idDisc);
                System.out.println("ADICIONANDO PENDÊNCIA (Anterior/Reprovada): ID Disciplina " + idDisc);
            }
        }


        // ===============================================
        // 6.2: Adiciona as NOVAS disciplinas da grade (AVANÇO)
        // ===============================================
        if (proximoSemestreCurso != -1) {
            List<Integer> disciplinasProxSemestre =
                    listarDisciplinasPorSemestreCurso(idCurso, String.valueOf(proximoSemestreCurso));

            for (Integer idDisc : disciplinasProxSemestre) {
                // Matrícula na disciplina nova, desde que não tenha sido aprovada
                // E (principalmente) não tenha sido adicionada como Pendência no Passo 6.1 (caso seja uma reprovação do último semestre)
                if (!idsAprovadas.contains(idDisc) &&
                    !disciplinasParaMatricular.contains(idDisc) && // <- Verificação crucial
                    !jaMatriculado(idMatricula, idDisc, proximoSemestreLetivo)) {
                    
                    disciplinasParaMatricular.add(idDisc);
                }
            }
        }

        // O curso só termina se não há mais disciplinas novas E não há pendências de repetição.
        if (proximoSemestreCurso == -1 && disciplinasParaMatricular.isEmpty()) {
             System.out.println("Aluno concluiu todas as disciplinas do curso e não tem pendências.");
             conn.commit();
             return disciplinasMatriculadas;
        }

        // 7️ Matricular as disciplinas selecionadas
        if (!disciplinasParaMatricular.isEmpty()) {
            String sqlInsert = "INSERT INTO matriculaDisciplina " +
                    "(idMatricula, idDisciplina, semestreCursado, faltas, nota, status, ativo) " +
                    "VALUES (?, ?, ?, 0, 0.0, 'Cursando', TRUE)";
            try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                for (Integer idDisc : disciplinasParaMatricular) {
                    ps.setInt(1, idMatricula);
                    ps.setInt(2, idDisc);
                    ps.setString(3, proximoSemestreLetivo);
                    ps.addBatch();
                }
                ps.executeBatch(); // Executa todas as inserções
            }

            // Retornar nomes das disciplinas matriculadas para exibir na interface
            String sqlNomes = "SELECT nome FROM disciplina WHERE idDisciplina IN (" +
                    disciplinasParaMatricular.stream().map(String::valueOf).collect(Collectors.joining(",")) + ")";
            try (PreparedStatement ps = conn.prepareStatement(sqlNomes);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    disciplinasMatriculadas.add(rs.getString("nome"));
                }
            }
        }

        conn.commit(); // Confirma a transação
    } catch (Exception e) {
        if (conn != null) conn.rollback(); // Desfaz em caso de erro
        throw new Exception("Erro ao processar fim de semestre: " + e.getMessage(), e); 
    } finally {
        if (conn != null) {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    return disciplinasMatriculadas;
}

/** Lista todas as IDs de disciplinas obrigatórias do curso, de 1 até o semestre alvo-1. */
public List<Integer> listarDisciplinasSemestreAte(int idCurso, int semestreAlvo) throws Exception {
    List<Integer> lista = new ArrayList<>();
    // Busca disciplinas com semestre MENOR que o semestreAlvo
    String sql = "SELECT idDisciplina FROM disciplina WHERE idCurso = ? AND semestre < ? AND ativo = TRUE";
    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idCurso);
        ps.setInt(2, semestreAlvo);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getInt("idDisciplina"));
            }
        }
    }
    return lista;
}

/** Calcula o maior número de semestre de uma disciplina que o aluno já aprovou. */
public int calcularMaxSemestreAprovado(Set<Integer> idsAprovadas, int idCurso) throws Exception {
    if (idsAprovadas.isEmpty()) return 0;
    
    String ids = idsAprovadas.stream().map(String::valueOf).collect(Collectors.joining(","));
    
    String sql = "SELECT MAX(semestre) FROM disciplina WHERE idDisciplina IN (" + ids + ") AND idCurso = ?";
    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idCurso);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    }
    return 0;
}


    private int getTotalDisciplinasAnteriores(int idCurso, int semestreAlvo) throws Exception { 
        int count = 0;
        String sql = "SELECT COUNT(idDisciplina) FROM disciplina WHERE idCurso = ? AND semestre < ? AND ativo = TRUE";
        try (Connection conn = ConnectionFactory.getConnection(); 
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCurso);
            ps.setInt(2, semestreAlvo); // Conta todos os semestres abaixo do alvo (e.g., para o 3º, conta 1º e 2º)
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            // Captura o SQLException e relança como Exception para o método chamador
            throw new Exception("Erro ao contar total de disciplinas anteriores.", e);
        }
        return count;
    }

    /** Conta quantas disciplinas dos semestres anteriores (1 até N-1) o aluno JÁ APROVOU. */
    private int totalAprovadasDisciplinasAnteriores(int idMatricula, int idCurso, int semestreAlvo) throws Exception { // MUDADO PARA throws Exception
        int count = 0;
        String sql = "SELECT COUNT(DISTINCT d.idDisciplina) " +
                     "FROM matriculaDisciplina md " +
                     "JOIN disciplina d ON md.idDisciplina = d.idDisciplina " +
                     "WHERE md.idMatricula = ? AND d.idCurso = ? AND d.semestre < ? AND md.status = 'Aprovado' AND d.ativo = TRUE";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMatricula);
            ps.setInt(2, idCurso);
            ps.setInt(3, semestreAlvo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            // Captura o SQLException e relança como Exception para o método chamador
            throw new Exception("Erro ao contar disciplinas aprovadas anteriores.", e);
        }
        return count;
    }

    public String obterSemestreAtualAluno(int idAluno) throws Exception {
        String sql = "SELECT semestreCursado FROM matriculaDisciplina md " +
                     "JOIN matricula m ON md.idMatricula = m.idMatricula " +
                     "WHERE m.idAluno = ? AND md.ativo = TRUE " +
                     "ORDER BY md.semestreCursado DESC LIMIT 1";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAluno);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("semestreCursado");
                }
            }
        }
        return calcularSemestreAtual(); // fallback
    }
    
    public String calcularProximoSemestreDoAluno(int idMatricula) throws Exception {
        String sql = "SELECT MAX(semestreCursado) AS ultimoSemestre FROM matriculaDisciplina WHERE idMatricula=? AND ativo=TRUE";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMatricula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String ultimo = rs.getString("ultimoSemestre");
                    if (ultimo != null && ultimo.contains("/")) {
                        String[] partes = ultimo.split("/");
                        int ano = Integer.parseInt(partes[0]);
                        int sem = Integer.parseInt(partes[1]);
                        int proximoSem = (sem == 1) ? 2 : 1;
                        int proximoAno = (sem == 1) ? ano : ano + 1;
                        return proximoAno + "/" + proximoSem;
                    } else {
                        // fallback seguro
                        return calcularSemestreAtual();
                    }

                }
            }
        }
        // fallback se não tiver nenhum semestre
        return calcularSemestreAtual();
    }
    

}
