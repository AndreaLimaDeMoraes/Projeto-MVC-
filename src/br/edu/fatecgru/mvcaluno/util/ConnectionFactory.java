package br.edu.fatecgru.mvcaluno.util;

import java.sql.Connection;
import java.sql.DriverManager;

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
			String url = "jdbc:mysql://localhost:3306";
			return DriverManager.getConnection(url,login,senha);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
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