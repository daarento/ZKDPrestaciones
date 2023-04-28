package com.springzk.pruebazk.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class MiConexion {
	static Connection con = null;
	static String URL = "jdbc:postgresql://localhost:5432/banco";
	static String USER = "postgres";
	static String PSSW = "root";
	
	public MiConexion() {
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(URL, USER, PSSW);
		}
		catch(SQLException ex) {
			System.out.println("Error al conectarse con la base de datos: " + ex);
            ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
            Logger.getLogger(MiConexion.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	public Connection getConnection() {
		return con;
	}
	
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl("jdbc:postgresql://localhost:5432/banco");
		dataSource.setUsername("postgres");
		dataSource.setPassword("root");
		
		return dataSource;
	}
}
