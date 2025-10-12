package br.edu.fatecgru.mvcaluno.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.fatecgru.mvcaluno.model.Aluno;
import br.edu.fatecgru.mvcaluno.model.AlunoView;
import br.edu.fatecgru.mvcaluno.util.ConnectionFactory;

public class AlunoDAO {
    private Connection conn;

    public AlunoDAO() throws Exception {
        try {
            this.conn = ConnectionFactory.getConnection();
        } catch (Exception e) {
            throw new Exception("Erro ao conectar: " + e.getMessage());
        }
    }

    // ========================
    // CREATE - Inserir Aluno
    // ========================
    public void salvar(Aluno aluno) throws Exception {
        if (aluno == null)
            throw new Exception("O valor passado não pode ser nulo");

        String SQL = "INSERT INTO aluno (ra, nome, dataNascimento, cpf, email, endereco, municipio, uf, celular, ativo) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;

        try {
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
     // Mude a lista para AlunoView
     List<AlunoView> lista = new ArrayList<>();
     
     // Novo SQL usando JOIN para buscar o nome do curso e campus
     String SQL = "SELECT a.*, c.nome AS nomeCurso, c.campus "
                + "FROM aluno a "
                + "INNER JOIN matricula m ON a.idAluno = m.idAluno "
                + "INNER JOIN curso c ON m.idCurso = c.idCurso "
                + "ORDER BY a.nome";
     
     PreparedStatement ps = null;
     ResultSet rs = null;

     try {
         ps = conn.prepareStatement(SQL);
         rs = ps.executeQuery();
         while (rs.next()) {
             // Instancia o novo AlunoView
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
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
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
        PreparedStatement ps = null;

        try {
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
        PreparedStatement ps = null;

        try {
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
	 // READ - Listar por Filtro (RA ou Nome)
	 // ========================
	 public List<AlunoView> listarPorFiltro(String filtro) throws Exception {
	     List<AlunoView> lista = new ArrayList<>();
	     
	     // Constrói o filtro SQL com coringas '%'
	     String filtroSQL = "%" + filtro + "%";
	     
	     // SQL com JOIN para buscar por RA OU Nome
	     String SQL = "SELECT a.*, c.nome AS nomeCurso, c.campus "
	                + "FROM aluno a "
	                + "INNER JOIN matricula m ON a.idAluno = m.idAluno "
	                + "INNER JOIN curso c ON m.idCurso = c.idCurso "
	                + "WHERE a.nome LIKE ? OR a.ra LIKE ? "
	                + "ORDER BY a.nome";
	     
	     PreparedStatement ps = null;
	     ResultSet rs = null;
	
	     try {
	         ps = conn.prepareStatement(SQL);
	         // Os dois parâmetros do WHERE (nome e ra) usam o mesmo filtro
	         ps.setString(1, filtroSQL); 
	         ps.setString(2, filtroSQL);
	         
	         rs = ps.executeQuery();
	         
	         while (rs.next()) {
	             // Lógica de instanciação de AlunoView
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
	         ConnectionFactory.closeConnection(conn, ps, rs);
	     }
	     return lista;
	 }
}