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

    /**
     * Construtor padrão. Não precisa mais gerenciar a conexão aqui.
     */
    public AlunoDAO() {
        // O construtor agora pode ficar vazio.
    }

    // ========================
    // CREATE - Inserir Aluno
    // ========================
    public void salvar(Aluno aluno) throws Exception {
        if (aluno == null)
            throw new Exception("O valor passado não pode ser nulo");

        String SQL = "INSERT INTO aluno (ra, nome, dataNascimento, cpf, email, endereco, municipio, uf, celular, ativo) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnectionFactory.getConnection(); // Abre a conexão
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
            ConnectionFactory.closeConnection(conn, ps); // Fecha a conexão
        }
    }

    // ========================
    // READ - Listar todos
    // ========================
    public List<AlunoView> listarTodos() throws Exception {
        List<AlunoView> lista = new ArrayList<>();
        
        String SQL = "SELECT a.*, c.nome AS nomeCurso, c.campus "
                   + "FROM aluno a "
                   + "INNER JOIN matricula m ON a.idAluno = m.idAluno "
                   + "INNER JOIN curso c ON m.idCurso = c.idCurso "
                   + "ORDER BY a.nome";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection(); // Abre a conexão
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
                        rs.getString("campus")
                );
                lista.add(aluno);
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao listar alunos: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps, rs); // Fecha a conexão
        }
        return lista;
    }

    // ========================
    // READ - Buscar por ID
    // ========================
    public Aluno buscarPorId(int idAluno) throws Exception {
        Aluno aluno = null;
        String SQL = "SELECT * FROM aluno WHERE idAluno = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection(); // Abre a conexão
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
            ConnectionFactory.closeConnection(conn, ps, rs); // Fecha a conexão
        }
        return aluno;
    }

    // ========================
    // UPDATE - Atualizar Aluno
    // ========================
    public void atualizar(Aluno aluno) throws Exception {
        if (aluno == null)
            throw new Exception("O valor passado não pode ser nulo");

        String SQL = "UPDATE aluno SET ra=?, nome=?, dataNascimento=?, cpf=?, email=?, endereco=?, "
                   + "municipio=?, uf=?, celular=?, ativo=? WHERE idAluno=?";
        
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnectionFactory.getConnection(); // Abre a conexão
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
            ConnectionFactory.closeConnection(conn, ps); // Fecha a conexão
        }
    }

    // ========================
    // DELETE - Remover Aluno
    // ========================
    public void excluir(int idAluno) throws Exception {
        String SQL = "DELETE FROM aluno WHERE idAluno=?";
        
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnectionFactory.getConnection(); // Abre a conexão
            ps = conn.prepareStatement(SQL);
            ps.setInt(1, idAluno);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro ao excluir aluno: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps); // Fecha a conexão
        }
    }
    
    // ========================
    // READ - Listar por Filtro (ID, RA ou Nome)
    // ========================
    public List<AlunoView> listarPorFiltro(String filtro) throws Exception {
        List<AlunoView> lista = new ArrayList<>();
        
        String filtroSQL = "%" + filtro + "%";
        
        String SQL = "SELECT a.*, c.nome AS nomeCurso, c.campus "
                   + "FROM aluno a "
                   + "INNER JOIN matricula m ON a.idAluno = m.idAluno "
                   + "INNER JOIN curso c ON m.idCurso = c.idCurso "
                   + "WHERE a.nome LIKE ? OR a.ra LIKE ? OR CAST(a.idAluno AS VARCHAR(20)) LIKE ? "
                   + "ORDER BY a.nome LIMIT 50";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection(); // Abre a conexão
            ps = conn.prepareStatement(SQL);
            ps.setString(1, filtroSQL); 
            ps.setString(2, filtroSQL);
            ps.setString(3, filtroSQL);
            
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
                        rs.getString("campus")
                );
                lista.add(aluno);
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao filtrar alunos: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps, rs); // Fecha a conexão
        }
        return lista;
    }
    
 // Na classe AlunoDAO.java:

    /**
     * Lista alunos filtrando pela tabela de Curso.
     */
    public List<AlunoView> listarPorCurso(String nomeCurso) throws Exception {
        List<AlunoView> listaAlunos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // NOVO SQL: Inclui a.* para trazer todos os campos necessários para AlunoView
        String SQL = "SELECT a.*, c.nome AS nomeCurso, c.campus "
                   + "FROM aluno a "
                   + "INNER JOIN matricula m ON a.idAluno = m.idAluno "
                   + "INNER JOIN curso c ON m.idCurso = c.idCurso "
                   + "WHERE a.ativo = true AND c.nome = ? "
                   + "ORDER BY a.nome";

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(SQL);
            ps.setString(1, nomeCurso); // Filtra pelo nome do curso
            rs = ps.executeQuery();

            while (rs.next()) {
                // Este construtor agora tem todas as colunas disponíveis no ResultSet
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
                        rs.getString("nomeCurso"), // alias do JOIN
                        rs.getString("campus")      // alias do JOIN
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
    
 // ========================
 // READ - Buscar Dados do Aluno para Boletim (ID, RA, Nome, Curso)
 // ========================
 public BoletimAluno buscarDadosBoletimAluno(int idAluno) throws Exception {
     BoletimAluno dados = null;
     String SQL = "SELECT a.idAluno, a.ra, a.nome, c.nome AS nomeCurso " +
                  "FROM aluno a " +
                  "JOIN matricula m ON a.idAluno = m.idAluno " +
                  "JOIN curso c ON m.idCurso = c.idCurso " +
                  "WHERE a.idAluno = ? AND a.ativo = TRUE AND m.ativo = TRUE";
     
     Connection conn = null;
     PreparedStatement ps = null;
     ResultSet rs = null;

     try {
         conn = ConnectionFactory.getConnection();
         ps = conn.prepareStatement(SQL);
         ps.setInt(1, idAluno);
         rs = ps.executeQuery();
         if (rs.next()) {
             dados = new BoletimAluno(
                 rs.getInt("idAluno"),
                 rs.getString("ra"),
                 rs.getString("nome"),
                 rs.getString("nomeCurso")
             );
         }
     } catch (SQLException e) {
         throw new Exception("Erro ao buscar dados do aluno para boletim: " + e.getMessage());
     } finally {
         ConnectionFactory.closeConnection(conn, ps, rs);
     }
     return dados;
 }

 // ========================
 // READ - Buscar Disciplinas do Boletim (com Notas e Faltas)
 // ========================
 public List<DisciplinaBoletim> buscarDisciplinasBoletim(int idAluno) throws Exception {
     List<DisciplinaBoletim> disciplinas = new ArrayList<>();
     String SQL = "SELECT d.nome AS nomeDisciplina, md.nota, md.faltas, md.semestreAtual " +
                  "FROM aluno a " +
                  "JOIN matricula m ON a.idAluno = m.idMatricula " +  // Correção: m.idAluno = a.idAluno
                  "JOIN matriculaDisciplina md ON m.idMatricula = md.idMatricula " +
                  "JOIN disciplina d ON md.idDisciplina = d.idDisciplina " +
                  "WHERE a.idAluno = ? AND a.ativo = TRUE AND m.ativo = TRUE AND md.ativo = TRUE " +
                  "ORDER BY md.semestreAtual, d.nome";
     
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
 
//========================
//READ - Buscar Histórico Escolar (Todas as disciplinas)
//========================
public List<DisciplinaBoletim> buscarHistoricoEscolar(int idAluno) throws Exception {
  List<DisciplinaBoletim> disciplinas = new ArrayList<>();
  String SQL = "SELECT d.nome AS nomeDisciplina, md.nota, md.faltas, md.semestreAtual " +
               "FROM aluno a " +
               "JOIN matricula m ON a.idAluno = m.idAluno " +
               "JOIN matriculaDisciplina md ON m.idMatricula = md.idMatricula " +
               "JOIN disciplina d ON md.idDisciplina = d.idDisciplina " +
               "WHERE a.idAluno = ? AND a.ativo = TRUE AND m.ativo = TRUE AND md.ativo = TRUE " +
               "ORDER BY md.semestreAtual, d.nome";  // Sem filtro por semestre
  
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
              rs.getString("semestreAtual")
          );
          disciplinas.add(disc);
      }
  } catch (SQLException e) {
      throw new Exception("Erro ao buscar histórico escolar: " + e.getMessage());
  } finally {
      ConnectionFactory.closeConnection(conn, ps, rs);
  }
  return disciplinas;
}
    
}