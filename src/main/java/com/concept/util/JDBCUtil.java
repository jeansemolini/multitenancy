package com.concept.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.concept.master.model.EmpresaNova;

public class JDBCUtil {

	public static void criarBancoDados(EmpresaNova empresa) {
		Connection connection = null;
		try {
			String driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);

			String url = "jdbc:mysql://localhost:3306/empresas_master?useSSL=false";

			connection = DriverManager.getConnection(url, "root", "root");

			Statement stmt = connection.createStatement();

			stmt.executeUpdate("CREATE DATABASE `" + empresa.getCnpj() + "`;");
			stmt.executeUpdate("INSERT INTO `empresas_master`.`empresa` (`razao_social`,`nome_fantasia`,`cnpj`, `url`, `username`, `password`, `ativo`, `responsavel`) VALUES ('" +  empresa.getRazaoSocial() + "', '" + empresa.getNomeFantasia() + "', '" + empresa.getCnpj() + "', 'jdbc:mysql://localhost:3306/" + empresa.getCnpj() + "?useSSL=false', 'root', 'root', '1', '" + empresa.getResponsavel() +"');");
			stmt.executeUpdate("INSERT INTO `empresas_master`.`usuarios` (`login`,`nome`,`senha`,`empresa`) VALUES ('" +  empresa.getEmail() + "', '" + empresa.getNomeUsuario() + "', '" + empresa.getSenha() + "', '" + empresa.getCnpj() + "');");

			connection.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static String consultaBanco(String bancoDados, String sql) {
		String retorno = null;
		Connection connection = null;
		try {
			String driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);

			String url = "jdbc:mysql://localhost:3306/" + bancoDados  + "?useSSL=false";

			connection = DriverManager.getConnection(url, "root", "root");

			Statement stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery(sql);

			while(rs.next()) {
				retorno = rs.getString(1);
			}

			connection.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return retorno;
	}

}
