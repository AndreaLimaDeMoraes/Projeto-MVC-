package br.edu.fatecgru.mvcaluno.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class ConnectionFactory {

	public static Connection getConnection() throws Exception{
		//metodo getconnection - não irá tratar erros
		try {
			//indica o DB mysql e aponta para o driver
			Class.forName("com.mysql.jdbc.Driver");
			//conexão com DB
			String login = "root";
			String senha = "";
			String url = "jdbc:mysql://localhost:3306/sistema_academico";
			return DriverManager.getConnection(url,login,senha);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	// FECHA APENAS A CONEXÃO
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // FECHA CONEXÃO + PreparedStatement
    public static void closeConnection(Connection conn, PreparedStatement ps) {
        closeConnection(conn);
        try {
            if (ps != null) ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // FECHA CONEXÃO + PreparedStatement + ResultSet
    public static void closeConnection(Connection conn, PreparedStatement ps, ResultSet rs) {
        closeConnection(conn, ps);
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	public static void main(String args[]) {
		try {
			Connection conn = ConnectionFactory.getConnection();
			JOptionPane.showMessageDialog(null, "CONECTOU O BANCO");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}	
}