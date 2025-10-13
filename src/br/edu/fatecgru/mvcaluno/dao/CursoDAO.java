package br.edu.fatecgru.mvcaluno.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.fatecgru.mvcaluno.model.Curso;
import br.edu.fatecgru.mvcaluno.util.ConnectionFactory;

public class CursoDAO {
    // REMOVER a conexão como atributo da classe
    // private Connection conn; ← REMOVER ESTA LINHA

    public CursoDAO() throws Exception {
        // Construtor vazio ou com lógica que não cria conexão
    }

    // Inserir curso - MODIFICADO
    public void salvar(Curso curso) throws Exception {
        if (curso == null)
            throw new Exception("O valor passado não pode ser nulo");

        Connection conn = null; // Conexão local
        PreparedStatement ps = null;
        
        String SQL = "INSERT INTO curso (nome, campus, periodo, duracao, ativo) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try {
            conn = ConnectionFactory.getConnection(); // Nova conexão
            ps = conn.prepareStatement(SQL);
            ps.setString(1, curso.getNome());
            ps.setString(2, curso.getCampus());
            ps.setString(3, curso.getPeriodo());
            ps.setInt(4, curso.getDuracao());
            ps.setBoolean(5, curso.isAtivo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro ao inserir curso: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps);
        }
    }

    // Listar todos os cursos - MODIFICADO
    public List<Curso> listarTodos() throws Exception {
        List<Curso> lista = new ArrayList<>();
        Connection conn = null; // Conexão local
        PreparedStatement ps = null;
        ResultSet rs = null;

        String SQL = "SELECT * FROM curso WHERE ativo = true ORDER BY nome";

        try {
            conn = ConnectionFactory.getConnection(); // Nova conexão
            ps = conn.prepareStatement(SQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                Curso curso = new Curso(
                    rs.getInt("idCurso"),
                    rs.getString("nome"),
                    rs.getString("campus"),
                    rs.getString("periodo"),
                    rs.getInt("duracao"),
                    rs.getBoolean("ativo")
                );
                lista.add(curso);
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao listar cursos: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps, rs);
        }
        return lista;
    }

    // Buscar curso por Id - MODIFICADO
    public Curso buscarPorId(int idCurso) throws Exception {
        Curso curso = null;
        Connection conn = null; // Conexão local
        PreparedStatement ps = null;
        ResultSet rs = null;

        String SQL = "SELECT * FROM curso WHERE idCurso = ? AND ativo = true";

        try {
            conn = ConnectionFactory.getConnection(); // Nova conexão
            ps = conn.prepareStatement(SQL);
            ps.setInt(1, idCurso);
            rs = ps.executeQuery();
            if (rs.next()) {
                curso = new Curso(
                    rs.getInt("idCurso"),
                    rs.getString("nome"),
                    rs.getString("campus"),
                    rs.getString("periodo"),
                    rs.getInt("duracao"),
                    rs.getBoolean("ativo")
                );
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar curso: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps, rs);
        }
        return curso;
    }

    // Atualizar curso - MODIFICADO
    public void atualizar(Curso curso) throws Exception {
        if (curso == null)
            throw new Exception("O valor passado não pode ser nulo");

        Connection conn = null; // Conexão local
        PreparedStatement ps = null;

        String SQL = "UPDATE curso SET nome=?, campus=?, periodo=?, duracao=? WHERE idCurso=?";

        try {
            conn = ConnectionFactory.getConnection(); // Nova conexão
            ps = conn.prepareStatement(SQL);
            ps.setString(1, curso.getNome());
            ps.setString(2, curso.getCampus());
            ps.setString(3, curso.getPeriodo());
            ps.setInt(4, curso.getDuracao());
            ps.setInt(5, curso.getIdCurso());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro ao atualizar curso: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps);
        }
    }

    // Remover curso - MODIFICADO
    public void excluir(int idCurso) throws Exception {
        Connection conn = null; // Conexão local
        PreparedStatement ps = null;

        String SQL = "UPDATE curso SET ativo = false WHERE idCurso=?";

        try {
            conn = ConnectionFactory.getConnection(); // Nova conexão
            ps = conn.prepareStatement(SQL);
            ps.setInt(1, idCurso);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro ao excluir curso: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps);
        }
    }

    // Listar por filtro - MODIFICADO
    public List<Curso> listarPorFiltro(String filtro) throws Exception {
        List<Curso> lista = new ArrayList<>();
        Connection conn = null; // Conexão local
        PreparedStatement ps = null;
        ResultSet rs = null;

        String filtroSQL = "%" + filtro + "%";
        String SQL = "SELECT * FROM curso WHERE ativo = true AND (nome LIKE ? OR campus LIKE ?) ORDER BY nome";

        try {
            conn = ConnectionFactory.getConnection(); // Nova conexão
            ps = conn.prepareStatement(SQL);
            ps.setString(1, filtroSQL);
            ps.setString(2, filtroSQL);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Curso curso = new Curso(
                    rs.getInt("idCurso"),
                    rs.getString("nome"),
                    rs.getString("campus"),
                    rs.getString("periodo"),
                    rs.getInt("duracao"),
                    rs.getBoolean("ativo")
                );
                lista.add(curso);
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao filtrar cursos: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps, rs);
        }
        return lista;
    }

    // Verifica se curso já existe - MODIFICADO
    public boolean existeCurso(String nome) throws Exception {
        Connection conn = null; // Conexão local
        PreparedStatement ps = null;
        ResultSet rs = null;

        String SQL = "SELECT COUNT(*) FROM curso WHERE nome = ? AND ativo = true";

        try {
            conn = ConnectionFactory.getConnection(); // Nova conexão
            ps = conn.prepareStatement(SQL);
            ps.setString(1, nome);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao verificar curso: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps, rs);
        }
        return false;
    }
}