package br.edu.fatecgru.mvcaluno.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.fatecgru.mvcaluno.model.Aluno;
import br.edu.fatecgru.mvcaluno.model.AlunoView;
import br.edu.fatecgru.mvcaluno.model.BoletimAluno;
import br.edu.fatecgru.mvcaluno.model.DisciplinaBoletim;
import br.edu.fatecgru.mvcaluno.util.ConnectionFactory;

public class AlunoDAO {

    public AlunoDAO() {
    }

    public void salvar(Aluno aluno) throws Exception {
        if (aluno == null)
            throw new Exception("O valor passado não pode ser nulo");

        String SQL = "INSERT INTO aluno (ra, nome, dataNascimento, cpf, email, endereco, municipio, uf, celular, ativo) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnectionFactory.getConnection(); 
            ps = conn.prepareStatement(SQL);
            ps.setString(1, aluno.getRa());
            ps.setString(2, aluno.getNome());
            ps.setString(3, aluno.getDataNascimento());
            ps.setString(4, aluno.getCpf());
            ps.setString(5, aluno.getEmail());
            ps.setString(6, aluno.getEndereco());
            ps.setString(7, aluno.getMunicipio());
            ps.setString(8, aluno.getUf());
            ps.setString(9, aluno.getCelular());
            ps.setBoolean(10, aluno.isAtivo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro ao inserir aluno: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps);  
        }
    }
    
    public Aluno buscarPorId(int idAluno) throws Exception {
        Aluno aluno = null;
        String SQL = "SELECT * FROM aluno WHERE idAluno = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection(); 
            ps = conn.prepareStatement(SQL);
            ps.setInt(1, idAluno);
            rs = ps.executeQuery();
            if (rs.next()) {
                aluno = new Aluno(
                        rs.getInt("idAluno"),
                        rs.getString("ra"),
                        rs.getString("nome"),
                        rs.getString("dataNascimento"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getString("endereco"),
                        rs.getString("municipio"),
                        rs.getString("uf"),
                        rs.getString("celular"),
                        rs.getBoolean("ativo")
                );
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar aluno: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps, rs);  
        }
        return aluno;
    }

    public void atualizar(Aluno aluno) throws Exception {
        if (aluno == null)
            throw new Exception("O valor passado não pode ser nulo");

        String SQL = "UPDATE aluno SET ra=?, nome=?, dataNascimento=?, cpf=?, email=?, endereco=?, "
                   + "municipio=?, uf=?, celular=?, ativo=? WHERE idAluno=?";
        
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnectionFactory.getConnection();  
            ps = conn.prepareStatement(SQL);
            ps.setString(1, aluno.getRa());
            ps.setString(2, aluno.getNome());
            ps.setString(3, aluno.getDataNascimento());
            ps.setString(4, aluno.getCpf());
            ps.setString(5, aluno.getEmail());
            ps.setString(6, aluno.getEndereco());
            ps.setString(7, aluno.getMunicipio());
            ps.setString(8, aluno.getUf());
            ps.setString(9, aluno.getCelular());
            ps.setBoolean(10, aluno.isAtivo());
            ps.setInt(11, aluno.getIdAluno());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro ao atualizar aluno: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps);  
        }
    }

    public void excluir(int idAluno) throws Exception {
        String SQL = "DELETE FROM aluno WHERE idAluno=?";
        
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnectionFactory.getConnection();  
            ps = conn.prepareStatement(SQL);
            ps.setInt(1, idAluno);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro ao excluir aluno: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps);  
        }
    }
    // Método listarTodos
    public List<AlunoView> listarTodos() throws Exception {
        List<AlunoView> lista = new ArrayList<>();
        
        String SQL = "SELECT a.*, c.nome AS nomeCurso, c.campus, " +
                "COALESCE((SELECT MAX(md.semestreCursado) FROM matriculaDisciplina md WHERE md.idMatricula = m.idMatricula AND md.status = 'Cursando' AND md.ativo = TRUE), 'Formado') AS semestreAtual, " +
                "m.idCurso " +
                "FROM aluno a " +
                "INNER JOIN matricula m ON a.idAluno = m.idAluno AND m.idMatricula = (" +
                "    SELECT MAX(idMatricula) FROM matricula m_max WHERE m_max.idAluno = a.idAluno) " +
                "INNER JOIN curso c ON m.idCurso = c.idCurso " +
                "GROUP BY a.idAluno, a.ra, a.nome, a.dataNascimento, a.cpf, a.email, a.endereco, a.municipio, a.uf, a.celular, a.ativo, c.nome, c.campus, semestreAtual, m.idCurso " +
                "ORDER BY a.nome";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            conn = ConnectionFactory.getConnection(); 
            ps = conn.prepareStatement(SQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                AlunoView aluno = new AlunoView(
                        rs.getInt("idAluno"),
                        rs.getString("ra"),
                        rs.getString("nome"),
                        rs.getString("dataNascimento"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getString("endereco"),
                        rs.getString("municipio"),
                        rs.getString("uf"),
                        rs.getString("celular"),
                        rs.getBoolean("ativo"),
                        rs.getString("nomeCurso"),
                        rs.getString("campus"),
                        rs.getString("semestreAtual"),
                        rs.getInt("idCurso")
                );
                lista.add(aluno);
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao listar alunos: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps, rs); 
        }
        return lista;
    }
           
    public List<AlunoView> listarPorFiltro(String filtro) throws Exception {
        List<AlunoView> lista = new ArrayList<>();
        
        String filtroSQL = "%" + filtro + "%";
        String SQL = "SELECT a.*, c.nome AS nomeCurso, c.campus, " +
                "COALESCE((SELECT MAX(md.semestreCursado) FROM matriculaDisciplina md WHERE md.idMatricula = m.idMatricula AND md.status = 'Cursando' AND md.ativo = TRUE), 'N/A') AS semestreAtual, " +
                "m.idCurso " +
                "FROM aluno a " +
                "LEFT JOIN matricula m ON a.idAluno = m.idAluno AND m.idMatricula = (" +  
                "    SELECT MAX(idMatricula) FROM matricula m_max WHERE m_max.idAluno = a.idAluno" +
                ") " +
                "LEFT JOIN curso c ON m.idCurso = c.idCurso " + 
                "WHERE a.nome LIKE ? OR a.ra LIKE ? OR CONVERT(a.idAluno, CHAR) LIKE ? " +
                "GROUP BY a.idAluno, a.ra, a.nome, a.dataNascimento, a.cpf, a.email, a.endereco, a.municipio, a.uf, a.celular, a.ativo, c.nome, c.campus, semestreAtual, m.idCurso " +
                "ORDER BY a.nome LIMIT 50";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionFactory.getConnection();  
            ps = conn.prepareStatement(SQL);
            ps.setString(1, filtroSQL); 
            ps.setString(2, filtroSQL);
            ps.setString(3, filtroSQL);
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                // Tratamento para alunos sem matrícula/curso (valores nulos)
                String nomeCurso = rs.getString("nomeCurso");
                String campus = rs.getString("campus");
                String semestreAtual = rs.getString("semestreAtual");
                Integer idCurso = rs.getInt("idCurso");
                if (rs.wasNull()) {
                    nomeCurso = "N/A";
                    campus = "N/A";
                    semestreAtual = "N/A";
                    idCurso = null;
                }
                
                AlunoView aluno = new AlunoView(
                    rs.getInt("idAluno"),
                    rs.getString("ra"),
                    rs.getString("nome"),
                    rs.getString("dataNascimento"),
                    rs.getString("cpf"),
                    rs.getString("email"),
                    rs.getString("endereco"),
                    rs.getString("municipio"),
                    rs.getString("uf"),
                    rs.getString("celular"),
                    rs.getBoolean("ativo"),
                    nomeCurso,
                    campus,
                    semestreAtual,
                    idCurso
                );
                lista.add(aluno);
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao filtrar alunos: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps, rs); 
        }
        return lista;
    }
    
        // Método listarPorCurso
    public List<AlunoView> listarPorCurso(String nomeCurso) throws Exception {
        List<AlunoView> listaAlunos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String SQL = "SELECT a.*, c.nome AS nomeCurso, c.campus, " +
                "COALESCE((SELECT MAX(md.semestreCursado) FROM matriculaDisciplina md WHERE md.idMatricula = m.idMatricula AND md.status = 'Cursando' AND md.ativo = TRUE), 'N/A') AS semestreAtual, " +
                "m.idCurso " +
                "FROM aluno a " +
                "INNER JOIN matricula m ON a.idAluno = m.idAluno AND m.idMatricula = (" +
                "    SELECT MAX(idMatricula) FROM matricula m_max WHERE m_max.idAluno = a.idAluno) " + 
                "INNER JOIN curso c ON m.idCurso = c.idCurso " +
                "WHERE a.ativo = true AND c.nome = ? " +
                "GROUP BY a.idAluno, a.ra, a.nome, a.dataNascimento, a.cpf, a.email, a.endereco, a.municipio, a.uf, a.celular, a.ativo, c.nome, c.campus, semestreAtual, m.idCurso " +
                "ORDER BY a.nome";
   
        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(SQL);
            ps.setString(1, nomeCurso);
            rs = ps.executeQuery();
            while (rs.next()) {
                AlunoView aluno = new AlunoView(
                        rs.getInt("idAluno"),
                        rs.getString("ra"),
                        rs.getString("nome"),
                        rs.getString("dataNascimento"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getString("endereco"),
                        rs.getString("municipio"),
                        rs.getString("uf"),
                        rs.getString("celular"),
                        rs.getBoolean("ativo"),
                        rs.getString("nomeCurso"),  
                        rs.getString("campus"),
                        rs.getString("semestreAtual"), 
                        rs.getInt("idCurso")
                );
                listaAlunos.add(aluno);
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao listar alunos por curso: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps, rs);
        }
        return listaAlunos;
    }
    

    public BoletimAluno buscarDadosBoletimAluno(int idAluno) throws Exception {
        BoletimAluno dados = null;
        
        String SQL = "SELECT a.idAluno, a.ra, a.nome, c.nome AS nomeCurso, c.campus " +
                     "FROM aluno a " +
                     "JOIN matricula m ON a.idAluno = m.idAluno " +
                     "JOIN curso c ON m.idCurso = c.idCurso " +
                     "WHERE a.idAluno = ? " +  
                     "ORDER BY m.idMatricula DESC LIMIT 1";
     
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(SQL);
            ps.setInt(1, idAluno);
            rs = ps.executeQuery();
            if (rs.next()) {
                dados = new BoletimAluno();  
                dados.setIdAluno(rs.getInt("idAluno"));
                dados.setRa(rs.getString("ra"));
                dados.setNome(rs.getString("nome"));
                dados.setNomeCurso(rs.getString("nomeCurso"));
                dados.setCampus(rs.getString("campus"));
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar dados do aluno para boletim: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps, rs);
        }
        return dados;
    }


    public List<DisciplinaBoletim> buscarDisciplinasBoletim(int idAluno) throws Exception {
        List<DisciplinaBoletim> disciplinas = new ArrayList<>();
        
        // O boletim mostra TODAS as disciplinas ativas (ativo = TRUE) do aluno,
        // independentemente do semestre ou status. 
        String SQL = "SELECT d.nome AS nomeDisciplina, md.nota, md.faltas, md.status, md.semestreCursado AS semestreAtual " +
                     "FROM aluno a " +
                     "JOIN matricula m ON a.idAluno = m.idAluno " +  
                     "JOIN matriculaDisciplina md ON m.idMatricula = md.idMatricula " +
                     "JOIN disciplina d ON md.idDisciplina = d.idDisciplina " +
                     "WHERE a.idAluno = ? AND m.ativo = TRUE AND md.ativo = TRUE " + 
                     "ORDER BY d.nome";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(SQL);
            ps.setInt(1, idAluno);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                DisciplinaBoletim disc = new DisciplinaBoletim(
                    rs.getString("nomeDisciplina"),
                    rs.getDouble("nota"),
                    rs.getInt("faltas"),
                    rs.getString("status"),
                    rs.getString("semestreAtual")
                );
                disciplinas.add(disc);
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar disciplinas do boletim: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps, rs);
        }
        return disciplinas;
    }

    public List<DisciplinaBoletim> buscarHistoricoEscolar(int idAluno) throws Exception {
        List<DisciplinaBoletim> disciplinas = new ArrayList<>();

        String SQL = "SELECT d.nome AS nomeDisciplina, md.nota, md.faltas, md.status, md.semestreCursado " +
                     "FROM matriculaDisciplina md " +
                     "INNER JOIN disciplina d ON md.idDisciplina = d.idDisciplina " +
                     "INNER JOIN matricula m ON md.idMatricula = m.idMatricula " +
                     "INNER JOIN aluno a ON m.idAluno = a.idAluno " +
                     "WHERE a.idAluno = ? " +
                     "ORDER BY CAST(SUBSTRING_INDEX(md.semestreCursado, '/', 1) AS UNSIGNED), " +
                     "         CAST(SUBSTRING_INDEX(md.semestreCursado, '/', -1) AS UNSIGNED), " +
                     "         d.nome";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setInt(1, idAluno);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DisciplinaBoletim disc = new DisciplinaBoletim(
                        rs.getString("nomeDisciplina"),
                        rs.getDouble("nota"),
                        rs.getInt("faltas"),
                        rs.getString("status"),
                        rs.getString("semestreCursado")
                    );
                    disciplinas.add(disc);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar histórico escolar: " + e.getMessage(), e);
        }

        return disciplinas;
    }


        public int buscarIdMatricula(int idAluno) throws Exception {
            int idMatricula = -1;
            String SQL = "SELECT idMatricula FROM matricula WHERE idAluno = ? ORDER BY idMatricula DESC LIMIT 1";
            
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                conn = ConnectionFactory.getConnection();
                ps = conn.prepareStatement(SQL);
                ps.setInt(1, idAluno);
                rs = ps.executeQuery();
                if (rs.next()) {
                    idMatricula = rs.getInt("idMatricula");
                }
            } catch (SQLException e) {
                throw new Exception("Erro ao buscar matrícula do aluno: " + e.getMessage());
            } finally {
                ConnectionFactory.closeConnection(conn, ps, rs);
            }
            return idMatricula;
        }
        

     // NOVO SQL (Substitua no AlunoDAO)
        public List<AlunoView> listarPorCursoECampusEFiltro(String nomeCurso, String campus, String filtro) throws Exception {
            List<AlunoView> listaAlunos = new ArrayList<>();
            
            // Otimizando a query para buscar dados completos da AlunoView
            String sql = "SELECT A.idAluno, A.ra, A.nome, A.dataNascimento, A.cpf, A.email, A.endereco, A.municipio, A.uf, A.celular, A.ativo, " +
                         "C.idCurso, C.nome AS nomeCurso, C.campus, C.periodo, " +
                         // Usando a matrícula mais recente, independentemente do status M.ativo
                         "COALESCE((SELECT MAX(md.semestreCursado) FROM matriculaDisciplina md WHERE md.idMatricula = M.idMatricula AND md.status = 'Cursando' AND md.ativo = TRUE), 'N/A') AS semestreAtual " +
                         "FROM aluno A " +
                         "JOIN matricula M ON A.idAluno = M.idAluno AND M.idMatricula = (" + // Pega a última matrícula
                         "    SELECT MAX(idMatricula) FROM matricula m_max WHERE m_max.idAluno = A.idAluno" +
                         ") " + // REMOVIDO: AND M.ativo = TRUE
                         "JOIN curso C ON M.idCurso = C.idCurso " +     
                         // Cláusula WHERE principal
                         "WHERE C.nome = ? AND C.campus = ? "; 
            
            boolean aplicarFiltroTexto = filtro != null && !filtro.trim().isEmpty();
            
            if (aplicarFiltroTexto) {
                sql += "AND (A.nome LIKE ? OR A.ra LIKE ?)"; 
            }
            
            // O filtro de aluno A.ativo = TRUE também pode ser removido, dependendo da sua regra de negócio
            // Para listar TODOS os alunos (ativos ou inativos), mas apenas cursos ativos:
            sql += " AND C.ativo = TRUE"; 
            // Se quiser garantir que SÓ alunos ativos apareçam, mantenha: sql += " AND A.ativo = TRUE"; 
            
            sql += " ORDER BY A.nome"; 
            
            // ... (O restante da sua lógica de PreparedStatement e ResultSet continua a mesma)
            // Lembre-se de mapear todos os campos do Aluno base no seu construtor de AlunoView!
            // Você pode usar o construtor completo ou setters, mas garanta que A.ativo seja lido.
            
            // ... (código de PreparedStatement, que está correto)

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                 
                int indice = 1;
                stmt.setString(indice++, nomeCurso); 
                stmt.setString(indice++, campus); 
                
                if (aplicarFiltroTexto) {
                    String filtroFormatado = "%" + filtro.trim() + "%";
                    stmt.setString(indice++, filtroFormatado);
                    stmt.setString(indice++, filtroFormatado);
                }
                
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        // Ao buscar mais campos, use o construtor completo do AlunoView (boa prática)
                        AlunoView aluno = new AlunoView(
                            rs.getInt("idAluno"),
                            rs.getString("ra"),
                            rs.getString("nome"),
                            rs.getString("dataNascimento"),
                            rs.getString("cpf"),
                            rs.getString("email"),
                            rs.getString("endereco"),
                            rs.getString("municipio"),
                            rs.getString("uf"),
                            rs.getString("celular"),
                            rs.getBoolean("ativo"),
                            rs.getString("nomeCurso"),
                            rs.getString("campus"),
                            rs.getString("semestreAtual"),
                            rs.getInt("idCurso")
                        );
                        listaAlunos.add(aluno);
                    }
                }
            }
            return listaAlunos;
        }
        
            public List<String> listarSemestresPorAluno(int idAluno) throws Exception {
            List<String> semestres = new ArrayList<>();
            
            String SQL = "SELECT DISTINCT md.semestreCursado AS semestre " +
                         "FROM matricula m " +
                         "JOIN matriculaDisciplina md ON m.idMatricula = md.idMatricula " +
                         "WHERE m.idAluno = ? " +
                         "ORDER BY semestre";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(SQL)) {

                ps.setInt(1, idAluno);
                
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        semestres.add(rs.getString("semestre"));
                    }
                }
            } catch (SQLException e) {
                throw new Exception("Erro ao listar semestres por aluno: " + e.getMessage());
            }
            return semestres;
        }
        
        // Método auxiliar
        private String calcularSemestreAtual() {
            java.time.LocalDate hoje = java.time.LocalDate.now();
            int ano = hoje.getYear();
            int semestre = (hoje.getMonthValue() <= 6) ? 1 : 2;
            return ano + "/" + semestre;
        }
}
    