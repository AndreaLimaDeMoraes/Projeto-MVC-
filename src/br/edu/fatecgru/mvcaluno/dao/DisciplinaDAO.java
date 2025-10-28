package br.edu.fatecgru.mvcaluno.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.fatecgru.mvcaluno.model.Disciplina;
import br.edu.fatecgru.mvcaluno.util.ConnectionFactory;

public class DisciplinaDAO {
	
	public DisciplinaDAO() throws Exception {
		
	}
	
	
	// Inserir curso
    public void salvar(Disciplina disciplina) throws Exception {
        if (disciplina == null)
            throw new Exception("O valor passado não pode ser nulo");

        Connection conn = null; // Conexão local
        PreparedStatement ps = null;
        
        String SQL = "INSERT INTO disciplina (idCurso, nome, semestre, ativo) VALUES (?, ?, ?, ?)";

        try {
        	conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(SQL);
            ps.setInt(1, disciplina.getIdCurso());
            ps.setString(2, disciplina.getNome());
            ps.setInt(3, disciplina.getSemestre());
            ps.setBoolean(4, disciplina.isAtivo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro ao inserir curso: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps);
        }
    }
	

    
 // Listar todas as disciplinas
    public List<Disciplina> listarTodos() throws Exception {
        List<Disciplina> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String SQL = "SELECT * FROM disciplina WHERE ativo = true ORDER BY semestre";

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(SQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                Disciplina disciplina = new Disciplina();
                disciplina.setIdDisciplina(rs.getInt("idDisciplina"));
                disciplina.setIdCurso(rs.getInt("idCurso"));
                disciplina.setNome(rs.getString("nome"));
                disciplina.setSemestre(rs.getInt("semestre"));
                disciplina.setAtivo(rs.getBoolean("ativo"));
                lista.add(disciplina);
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao listar disciplinas: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps, rs);
        }

        return lista;
    }
    
    
 // Atualizar disciplina existente
    public void atualizar(Disciplina disciplina) throws Exception {
        if (disciplina == null)
            throw new Exception("O valor passado não pode ser nulo");

        Connection conn = null;
        PreparedStatement ps = null;

        String SQL = "UPDATE disciplina SET nome = ?, semestre = ?, idCurso = ?, ativo = ? WHERE idDisciplina = ?";

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(SQL);
            ps.setString(1, disciplina.getNome());
            ps.setInt(2, disciplina.getSemestre());
            ps.setInt(3, disciplina.getIdCurso());
            ps.setBoolean(4, disciplina.isAtivo());
            ps.setInt(5, disciplina.getIdDisciplina());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro ao atualizar disciplina: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps);
        }
    }
    
 // Excluir disciplina (desativar ou deletar)
    public void excluir(int idDisciplina) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;

        //  excluir de vez, use DELETE
        // String SQL = "DELETE FROM disciplina WHERE idDisciplina = ?";

        // apenas desativar (para manter histórico)
        String SQL = "UPDATE disciplina SET ativo = false WHERE idDisciplina = ?";

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(SQL);
            ps.setInt(1, idDisciplina);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro ao excluir disciplina: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps);
        }
    }

    
    
}
