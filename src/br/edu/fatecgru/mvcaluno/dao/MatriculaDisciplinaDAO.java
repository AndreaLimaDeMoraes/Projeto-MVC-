package br.edu.fatecgru.mvcaluno.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import br.edu.fatecgru.mvcaluno.model.DisciplinaBoletim;
import br.edu.fatecgru.mvcaluno.model.MatriculaDisciplina;
import br.edu.fatecgru.mvcaluno.model.NotaFaltas;
import br.edu.fatecgru.mvcaluno.util.ConnectionFactory;

public class MatriculaDisciplinaDAO {

    private static final Logger logger = Logger.getLogger(MatriculaDisciplinaDAO.class.getName());

    public MatriculaDisciplinaDAO() {}

    // ===============================================
    // READ - Buscar Nota e Faltas (Item Específico)
    // ===============================================
    public NotaFaltas buscarNotaFaltas(int idMatricula, int idDisciplina, String semestre) throws Exception {
        if (semestre == null || semestre.trim().isEmpty()) {
            throw new IllegalArgumentException("Semestre não pode ser nulo ou vazio.");
        }

        NotaFaltas resultado = null;
        String SQL = "SELECT md.nota, md.faltas, md.status FROM matriculaDisciplina md " +
                     "JOIN matricula m ON md.idMatricula = m.idMatricula " +
                     "WHERE md.idMatricula = ? AND md.idDisciplina = ? AND m.semestreInicio = ? AND md.ativo = 1";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setInt(1, idMatricula);
            ps.setInt(2, idDisciplina);
            ps.setString(3, semestre);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double nota = rs.getDouble("nota");
                    int faltas = rs.getInt("faltas");
                    String status = rs.getString("status");
                    resultado = new NotaFaltas(nota, faltas, status);
                }
            }

        } catch (SQLException e) {
            throw new Exception("Erro ao buscar nota e faltas: " + e.getMessage());
        }
        return resultado;
    }

    // ===============================================
    // CREATE/UPDATE - Salvar/Alterar Nota, Faltas e Status (UPSERT) - Versão com 6 argumentos
    // ===============================================
    public void salvarNotaFaltas(int idMatricula, int idDisciplina, String semestre,
                                 double nota, int faltas, String status) throws Exception {
        if (semestre == null || semestre.trim().isEmpty()) {
            throw new IllegalArgumentException("Semestre não pode ser nulo ou vazio.");
        }
        if (nota < 0 || nota > 10) {
            throw new IllegalArgumentException("Nota deve estar entre 0 e 10.");
        }
        if (faltas < 0) {
            throw new IllegalArgumentException("Faltas não podem ser negativas.");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status não pode ser nulo ou vazio.");
        }

        String SQL = "INSERT INTO matriculaDisciplina " +
                     "(idMatricula, idDisciplina, faltas, nota, status, ativo) " +
                     "VALUES (?, ?, ?, ?, ?, 1) " +
                     "ON DUPLICATE KEY UPDATE nota = VALUES(nota), faltas = VALUES(faltas), status = VALUES(status)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setInt(1, idMatricula);
            ps.setInt(2, idDisciplina);
            ps.setInt(3, faltas);
            ps.setDouble(4, nota);
            ps.setString(5, status);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new Exception("Erro ao salvar nota, faltas e status: " + e.getMessage());
        }
    }

    // ===============================================
    // CREATE/UPDATE - Salvar/Alterar Nota e Faltas (UPSERT) - Overload com 5 argumentos (status padrão "Cursando")
    // ===============================================
    public void salvarNotaFaltas(int idMatricula, int idDisciplina, String semestre,
                                 double nota, int faltas) throws Exception {
        salvarNotaFaltas(idMatricula, idDisciplina, semestre, nota, faltas, "Cursando");
    }

    // ===============================================
    // DELETE - Excluir Nota e Faltas
    // ===============================================
    public void excluirNotaFaltas(int idMatricula, int idDisciplina, String semestre) throws Exception {
        if (semestre == null || semestre.trim().isEmpty()) {
            throw new IllegalArgumentException("Semestre não pode ser nulo ou vazio.");
        }

        String SQL = "DELETE md FROM matriculaDisciplina md " +
                     "JOIN matricula m ON md.idMatricula = m.idMatricula " +
                     "WHERE md.idMatricula = ? AND md.idDisciplina = ? AND m.semestreInicio = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setInt(1, idMatricula);
            ps.setInt(2, idDisciplina);
            ps.setString(3, semestre);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new Exception("Nenhuma nota/falta encontrada para excluir com os parâmetros fornecidos.");
            }

        } catch (SQLException e) {
            throw new Exception("Erro ao excluir nota e faltas: " + e.getMessage());
        }
    }

    // ===============================================
    // READ - Buscar Disciplinas para Boletim (Semestre Atual)
    // ===============================================
    public List<DisciplinaBoletim> buscarDisciplinasBoletim(int idAluno) throws Exception {
        List<DisciplinaBoletim> disciplinas = new ArrayList<>();
        String SQL = "SELECT d.nome AS nomeDisciplina, md.nota, md.faltas, md.status, m.semestreInicio AS semestreAtual " +
                     "FROM aluno a " +
                     "JOIN matricula m ON a.idAluno = m.idAluno " +
                     "JOIN matriculaDisciplina md ON m.idMatricula = md.idMatricula " +
                     "JOIN disciplina d ON md.idDisciplina = d.idDisciplina " +
                     "WHERE a.idAluno = ? AND a.ativo = TRUE AND m.ativo = TRUE AND md.ativo = TRUE " +
                     "ORDER BY d.nome";

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
                        rs.getString("semestreAtual")
                    );
                    disciplinas.add(disc);
                }
            }

        } catch (SQLException e) {
            throw new Exception("Erro ao buscar disciplinas do boletim: " + e.getMessage());
        }
        return disciplinas;
    }

    // ===============================================
    // MÉTODO AUXILIAR - Buscar alunos reprovados para rematrícula
    // ===============================================
    public List<MatriculaDisciplina> listarAlunosReprovados(String semestre) throws Exception {
        if (semestre == null || semestre.trim().isEmpty()) {
            throw new IllegalArgumentException("Semestre não pode ser nulo ou vazio.");
        }

        List<MatriculaDisciplina> reprovados = new ArrayList<>();
        String SQL = "SELECT md.idMatriculaDisciplina, md.idMatricula, md.idDisciplina, md.faltas, md.nota, md.status, md.ativo " +
                     "FROM matriculaDisciplina md " +
                     "JOIN matricula m ON md.idMatricula = m.idMatricula " +
                     "WHERE m.semestreInicio = ? AND md.status = 'Reprovado' AND md.ativo = 1";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setString(1, semestre);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MatriculaDisciplina md = new MatriculaDisciplina(
                        rs.getInt("idMatriculaDisciplina"),
                        rs.getInt("idMatricula"),
                        rs.getInt("idDisciplina"),
                        rs.getInt("faltas"),
                        rs.getDouble("nota"),
                        rs.getString("status"),
                        rs.getBoolean("ativo")
                    );
                    reprovados.add(md);
                }
            }

        } catch (SQLException e) {
            throw new Exception("Erro ao listar alunos reprovados: " + e.getMessage());
        }
        return reprovados;
    }

    /**
     * Realiza a rematrícula automática das disciplinas reprovadas de um semestre para o próximo.
     * @param semestreAtual O semestre em que os alunos foram reprovados (ex: "2025/1")
     * @param proximoSemestre O semestre para rematrícula (ex: "2025/2")
     * @throws Exception
     */
    public void rematricularReprovados(String semestreAtual, String proximoSemestre) throws Exception {
        if (semestreAtual == null || semestreAtual.trim().isEmpty() || proximoSemestre == null || proximoSemestre.trim().isEmpty()) {
            throw new IllegalArgumentException("Semestres não podem ser nulos ou vazios.");
        }

        List<MatriculaDisciplina> reprovados = listarAlunosReprovados(semestreAtual);

        if (reprovados.isEmpty()) {
            logger.info("Nenhum aluno reprovado encontrado no semestre " + semestreAtual);
            return;
        }

        // Primeiro, atualiza o semestreInicio na tabela matricula para o próximo semestre
        String SQLUpdateMatricula = "UPDATE matricula SET semestreInicio = ? WHERE idMatricula IN (" +
                                    "SELECT DISTINCT md.idMatricula FROM matriculaDisciplina md WHERE md.status = 'Reprovado' AND md.ativo = 1)";

        String SQLInsert = "INSERT INTO matriculaDisciplina " +
                           "(idMatricula, idDisciplina, faltas, nota, status, ativo) " +
                           "VALUES (?, ?, 0, 0, 'Cursando', 1)";

        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false); // Inicia transação

            // Atualiza o semestre das matrículas reprovadas
            try (PreparedStatement psUpdate = conn.prepareStatement(SQLUpdateMatricula)) {
                psUpdate.setString(1, proximoSemestre);
                psUpdate.executeUpdate();
            }

            // Insere novas matrículas em disciplinas
            try (PreparedStatement ps = conn.prepareStatement(SQLInsert)) {
                for (MatriculaDisciplina md : reprovados) {
                    ps.setInt(1, md.getIdMatricula());
                    ps.setInt(2, md.getIdDisciplina());
                    ps.addBatch();
                }

                int[] resultados = ps.executeBatch();
                conn.commit(); // Confirma transação
                logger.info("Rematrícula concluída: " + resultados.length + " disciplinas rematriculadas para o semestre " + proximoSemestre);
            }

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Reverte em caso de erro
                } catch (SQLException rollbackEx) {
                    logger.log(Level.SEVERE, "Erro ao fazer rollback: " + rollbackEx.getMessage());
                }
            }
            throw new Exception("Erro ao realizar rematrícula automática: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restaura auto-commit
                    conn.close();
                } catch (SQLException closeEx) {
                    logger.log(Level.WARNING, "Erro ao fechar conexão: " + closeEx.getMessage());
                }
            }
        }
    }
    
 // ===============================================
 // READ - Obter ID do Curso de uma Matrícula
 // ===============================================
 public int obterIdCursoDaMatricula(int idMatricula) throws Exception {
     String SQL = "SELECT idCurso FROM matricula WHERE idMatricula = ?";
     try (Connection conn = ConnectionFactory.getConnection();
          PreparedStatement ps = conn.prepareStatement(SQL)) {
         ps.setInt(1, idMatricula);
         try (ResultSet rs = ps.executeQuery()) {
             if (rs.next()) {
                 return rs.getInt("idCurso");
             }
         }
     } catch (SQLException e) {
         throw new Exception("Erro ao obter ID do curso da matrícula: " + e.getMessage());
     }
     return -1; // Retorno padrão se não encontrar
 }
 
//===============================================
//READ - Listar Disciplinas por Curso e Semestre
//===============================================
public List<String> listarDisciplinasPorCursoESemestre(int idCurso, int semestre) throws Exception {
  List<String> disciplinas = new ArrayList<>();
  String SQL = "SELECT CONCAT(idDisciplina, ' - ', nome) AS disciplina " +
               "FROM disciplina " +
               "WHERE idCurso = ? AND semestre = ? AND ativo = 1 " +
               "ORDER BY nome";
  try (Connection conn = ConnectionFactory.getConnection();
       PreparedStatement ps = conn.prepareStatement(SQL)) {
      ps.setInt(1, idCurso);
      ps.setInt(2, semestre);
      try (ResultSet rs = ps.executeQuery()) {
          while (rs.next()) {
              disciplinas.add(rs.getString("disciplina"));
          }
      }
  } catch (SQLException e) {
      throw new Exception("Erro ao listar disciplinas por curso e semestre: " + e.getMessage());
  }
  return disciplinas;
}
}