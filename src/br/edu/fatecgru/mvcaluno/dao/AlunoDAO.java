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

 // ========================
 // READ - Listar todos
 // ========================
 public List<AlunoView> listarTodos() throws Exception {
     List<AlunoView> lista = new ArrayList<>();
     
     // CORREÇÃO FINAL: Usando Sub-consulta t1 não-correlacionada para o MAX(semestreAtual)
     String SQL = "SELECT a.*, c.nome AS nomeCurso, c.campus, t1.maxSemestreAtual "
                + "FROM aluno a "
                // 1. Encontra a matrícula ativa/mais recente do aluno
                + "INNER JOIN matricula m ON a.idAluno = m.idAluno AND m.idMatricula = ("
                + "    SELECT MAX(idMatricula) FROM matricula m_max WHERE m_max.idAluno = a.idAluno"
                + ") "
                // 2. Faz JOIN com o Curso dessa matrícula
                + "INNER JOIN curso c ON m.idCurso = c.idCurso "
                // 3. LEFT JOIN com a Sub-consulta t1 que pré-calcula o semestre máximo para CADA matrícula
                + "LEFT JOIN ("
                + "    SELECT md.idMatricula, MAX(md.semestreAtual) AS maxSemestreAtual "
                + "    FROM matriculaDisciplina md "
                + "    GROUP BY md.idMatricula"
                + ") AS t1 ON m.idMatricula = t1.idMatricula "
                // GROUP BY completo para satisfazer o MySQL/MariaDB
                + "GROUP BY a.idAluno, a.ra, a.nome, a.dataNascimento, a.cpf, a.email, a.endereco, a.municipio, a.uf, a.celular, a.ativo, c.nome, c.campus, t1.maxSemestreAtual "
                + "ORDER BY a.nome";
     
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
                     rs.getString("maxSemestreAtual") // Mapeamento do novo alias
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

    // ========================
    // DELETE - Remover Aluno
    // ========================
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
    
 // ========================
 // READ - Listar por Filtro (ID, RA ou Nome)
 // ========================
    public List<AlunoView> listarPorFiltro(String filtro) throws Exception {
        List<AlunoView> lista = new ArrayList<>();
        
        String filtroSQL = "%" + filtro + "%";
        
        String SQL = "SELECT a.*, c.nome AS nomeCurso, c.campus, t1.maxSemestreAtual "
                   + "FROM aluno a "
                   + "INNER JOIN matricula m ON a.idAluno = m.idAluno AND m.idMatricula = ("
                   + "    SELECT MAX(idMatricula) FROM matricula m_max WHERE m_max.idAluno = a.idAluno"
                   + ") "
                   + "INNER JOIN curso c ON m.idCurso = c.idCurso "
                   + "LEFT JOIN ("
                   + "    SELECT md.idMatricula, MAX(md.semestreAtual) AS maxSemestreAtual "
                   + "    FROM matriculaDisciplina md "
                   + "    GROUP BY md.idMatricula"
                   + ") AS t1 ON m.idMatricula = t1.idMatricula "
                   + "WHERE a.nome LIKE ? OR a.ra LIKE ? OR CAST(a.idAluno AS VARCHAR(20)) LIKE ? "
                   + "GROUP BY a.idAluno, a.ra, a.nome, a.dataNascimento, a.cpf, a.email, a.endereco, a.municipio, a.uf, a.celular, a.ativo, c.nome, c.campus, t1.maxSemestreAtual "
                   + "ORDER BY a.nome LIMIT 50";
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
                        rs.getString("maxSemestreAtual")
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
    
 /**
  * Lista alunos filtrando pela tabela de Curso.
  */
 public List<AlunoView> listarPorCurso(String nomeCurso) throws Exception {
     List<AlunoView> listaAlunos = new ArrayList<>();
     Connection conn = null;
     PreparedStatement ps = null;
     ResultSet rs = null;

     // CORREÇÃO FINAL: Usando Sub-consulta t1 não-correlacionada
     String SQL = "SELECT a.*, c.nome AS nomeCurso, c.campus, t1.maxSemestreAtual "
                + "FROM aluno a "
                // 1. Encontra a matrícula ativa/mais recente do aluno
                + "INNER JOIN matricula m ON a.idAluno = m.idAluno AND m.idMatricula = ("
                + "    SELECT MAX(idMatricula) FROM matricula m_max WHERE m_max.idAluno = a.idAluno"
                + ") "
                // 2. Faz JOIN com o Curso dessa matrícula
                + "INNER JOIN curso c ON m.idCurso = c.idCurso "
                // 3. LEFT JOIN com a Sub-consulta t1 que pré-calcula o semestre máximo para CADA matrícula
                + "LEFT JOIN ("
                + "    SELECT md.idMatricula, MAX(md.semestreAtual) AS maxSemestreAtual "
                + "    FROM matriculaDisciplina md "
                + "    GROUP BY md.idMatricula"
                + ") AS t1 ON m.idMatricula = t1.idMatricula "
                + "WHERE a.ativo = true AND c.nome = ? "
                // GROUP BY completo para satisfazer o MySQL/MariaDB
                + "GROUP BY a.idAluno, a.ra, a.nome, a.dataNascimento, a.cpf, a.email, a.endereco, a.municipio, a.uf, a.celular, a.ativo, c.nome, c.campus, t1.maxSemestreAtual "
                + "ORDER BY a.nome";

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
                     rs.getString("maxSemestreAtual")
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
 // READ - Buscar Dados do Aluno para Boletim (ID, RA, Nome, Curso, Campus)
 // (MANTIDO o SQL simples aqui, pois não precisa do semestreAtual)
 // ========================
    public BoletimAluno buscarDadosBoletimAluno(int idAluno) throws Exception {
        BoletimAluno dados = null;
        
        String SQL = "SELECT a.idAluno, a.ra, a.nome, c.nome AS nomeCurso, c.campus " +
                     "FROM aluno a " +
                     "JOIN matricula m ON a.idAluno = m.idAluno " +
                     "JOIN curso c ON m.idCurso = c.idCurso " +
                     "WHERE a.idAluno = ? AND a.ativo = TRUE AND m.ativo = TRUE " +
                     "ORDER BY m.idMatricula DESC LIMIT 1"; // Pega a matrícula mais recente
     
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

 // ========================
 // READ - Buscar Disciplinas do Boletim (com Notas e Faltas)
 // (MANTIDO, pois a lógica de sub-consulta estava correta)
 // ========================
 public List<DisciplinaBoletim> buscarDisciplinasBoletim(int idAluno) throws Exception {
     List<DisciplinaBoletim> disciplinas = new ArrayList<>();
     String SQL = "SELECT d.nome AS nomeDisciplina, md.nota, md.faltas, md.semestreAtual " +
             "FROM aluno a " +
             "JOIN matricula m ON a.idAluno = m.idAluno " +  
             "JOIN matriculaDisciplina md ON m.idMatricula = md.idMatricula " +
             "JOIN disciplina d ON md.idDisciplina = d.idDisciplina " +
             "WHERE a.idAluno = ? AND a.ativo = TRUE AND m.ativo = TRUE AND md.ativo = TRUE " +
             "  AND md.semestreAtual = ( " +
             "      SELECT MAX(md_sub.semestreAtual) " +
             "      FROM matriculaDisciplina md_sub " +
             "      JOIN matricula m_sub ON md_sub.idMatricula = m_sub.idMatricula " +
             "      WHERE m_sub.idAluno = a.idAluno " +
             "  ) " +
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
// (MANTIDO, pois estava correto)
//========================
public List<DisciplinaBoletim> buscarHistoricoEscolar(int idAluno) throws Exception {
  List<DisciplinaBoletim> disciplinas = new ArrayList<>();
  String SQL = "SELECT d.nome AS nomeDisciplina, md.nota, md.faltas, md.semestreAtual " +
               "FROM aluno a " +
               "JOIN matricula m ON a.idAluno = m.idAluno " +
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
      throw new Exception("Erro ao buscar histórico escolar: " + e.getMessage());
  } finally {
      ConnectionFactory.closeConnection(conn, ps, rs);
  }
  return disciplinas;
}

//========================
//READ - Buscar ID da Matrícula Ativa do Aluno
//========================
public int buscarIdMatricula(int idAluno) throws Exception {
 int idMatricula = -1;
 String SQL = "SELECT idMatricula FROM matricula WHERE idAluno = ? AND ativo = 1 ORDER BY idMatricula DESC LIMIT 1";
 
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

//========================
//READ - Listar Disciplinas por Curso e Semestre
//========================
public List<String> listarDisciplinasPorCursoESemestre(int idCurso, int semestre) throws Exception {
 List<String> disciplinas = new ArrayList<>();
 String SQL = "SELECT idDisciplina, nome FROM disciplina WHERE idCurso = ? AND semestre = ? AND ativo = 1 ORDER BY nome";
 
 Connection conn = null;
 PreparedStatement ps = null;
 ResultSet rs = null;

 try {
     conn = ConnectionFactory.getConnection();
     ps = conn.prepareStatement(SQL);
     ps.setInt(1, idCurso);
     ps.setInt(2, semestre);
     rs = ps.executeQuery();
     while (rs.next()) {
         disciplinas.add(rs.getInt("idDisciplina") + " - " + rs.getString("nome"));
     }
 } catch (SQLException e) {
     throw new Exception("Erro ao listar disciplinas: " + e.getMessage());
 } finally {
     ConnectionFactory.closeConnection(conn, ps, rs);
 }
 return disciplinas;
}

//========================
//READ - Buscar Nota e Faltas
//========================
public double[] buscarNotaFaltas(int idMatricula, int idDisciplina, String semestre) throws Exception {
 double[] resultado = null;
 String SQL = "SELECT nota, faltas FROM matriculaDisciplina WHERE idMatricula = ? AND idDisciplina = ? AND semestreAtual = ? AND ativo = 1";
 
 Connection conn = null;
 PreparedStatement ps = null;
 ResultSet rs = null;

 try {
     conn = ConnectionFactory.getConnection();
     ps = conn.prepareStatement(SQL);
     ps.setInt(1, idMatricula);
     ps.setInt(2, idDisciplina);
     ps.setString(3, semestre);
     rs = ps.executeQuery();
     if (rs.next()) {
         resultado = new double[]{rs.getDouble("nota"), rs.getInt("faltas")};
     }
 } catch (SQLException e) {
     throw new Exception("Erro ao buscar nota e faltas: " + e.getMessage());
 } finally {
     ConnectionFactory.closeConnection(conn, ps, rs);
 }
 return resultado;
}

//========================
//CREATE/UPDATE - Salvar Nota e Faltas
//========================
public void salvarNotaFaltas(int idMatricula, int idDisciplina, String semestre, double nota, int faltas) throws Exception {
 String SQLCheck = "SELECT idMatriculaDisciplina FROM matriculaDisciplina WHERE idMatricula = ? AND idDisciplina = ? AND semestreAtual = ?";
 
 Connection conn = null;
 PreparedStatement ps = null;
 ResultSet rs = null;

 try {
     conn = ConnectionFactory.getConnection();
     ps = conn.prepareStatement(SQLCheck);
     ps.setInt(1, idMatricula);
     ps.setInt(2, idDisciplina);
     ps.setString(3, semestre);
     rs = ps.executeQuery();
     
     if (rs.next()) {
         // Update
         ps.close();
         String SQLUpdate = "UPDATE matriculaDisciplina SET nota = ?, faltas = ? WHERE idMatricula = ? AND idDisciplina = ? AND semestreAtual = ?";
         ps = conn.prepareStatement(SQLUpdate);
         ps.setDouble(1, nota);
         ps.setInt(2, faltas);
         ps.setInt(3, idMatricula);
         ps.setInt(4, idDisciplina);
         ps.setString(5, semestre);
         ps.executeUpdate();
     } else {
         // Insert
         ps.close();
         String SQLInsert = "INSERT INTO matriculaDisciplina (idMatricula, idDisciplina, faltas, semestreAtual, nota, ativo) VALUES (?, ?, ?, ?, ?, 1)";
         ps = conn.prepareStatement(SQLInsert);
         ps.setInt(1, idMatricula);
         ps.setInt(2, idDisciplina);
         ps.setInt(3, faltas);
         ps.setString(4, semestre);
         ps.setDouble(5, nota);
         ps.executeUpdate();
     }
 } catch (SQLException e) {
     throw new Exception("Erro ao salvar nota e faltas: " + e.getMessage());
 } finally {
     ConnectionFactory.closeConnection(conn, ps, rs);
 }
  
}


public int obterIdCursoDaMatricula(int idMatricula) throws Exception {
    int idCurso = -1;
    String SQL = "SELECT idCurso FROM matricula WHERE idMatricula = ? AND ativo = 1";
    
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
        conn = ConnectionFactory.getConnection();
        ps = conn.prepareStatement(SQL);
        ps.setInt(1, idMatricula);
        rs = ps.executeQuery();
        if (rs.next()) {
            idCurso = rs.getInt("idCurso");
        }
    } catch (SQLException e) {
        throw new Exception("Erro ao obter ID do curso da matrícula: " + e.getMessage());
    } finally {
        ConnectionFactory.closeConnection(conn, ps, rs);
    }
    return idCurso;
}


    
}