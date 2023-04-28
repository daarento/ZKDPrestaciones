package com.springzk.pruebazk;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.springzk.pruebazk.model.Prestaciones;
import com.springzk.pruebazk.dao.PrestacionesDAO;
import com.springzk.pruebazk.dao.PrestacionesDAOImpl;

import com.springzk.pruebazk.config.MiConexion;

public class ZkdPrestacionesTest {
	
	private DataSource dataSource;
	private PrestacionesDAO dao;
	
	private Connection con;	
	
	//Para que realice esto antes de cada método. Crear la conexion con la base de datos
	@BeforeEach
	void setupBeforeEach() {
		MiConexion con = new MiConexion();
		//this.con = con.getConnection();
		
		dataSource = con.getDataSource();
		
		dao = new PrestacionesDAOImpl();
	}
	
	@Test
	void insertarTest() {
		ArrayList<Prestaciones> lista = new ArrayList<>();
		Prestaciones p1 = new Prestaciones("Pepito", "Grillo", 35);
		Prestaciones p2 = new Prestaciones("Francisco", "Javier", 22);
		lista.add(p1);
		lista.add(p2);
		
		//result = dao.insert(p1);
		int result = 0;
		
		for(int i=0; i<lista.size(); i++) {
			result = dao.insert(lista.get(i));
			assertTrue(result > 0);
		}		
	}
	
	@Test
	void actualizarTest() {
		Prestaciones p = new Prestaciones("Pepito", "Rico Pepino", 25);
		int result = dao.update(p);
		assertTrue(result > 0);
	}
	
	@Test
	void eliminarTest() {
		String nombre = "Prueba";
		int result = dao.delete(nombre);
		assertTrue(result > 0);
	}    
	
	@Test
	void listarTest() {
		List<Prestaciones> list = dao.listar();
		
		for(Prestaciones cada: list) {
			System.out.println(cada);
		}
	}
	
	@Test
	void listarTest1() {
		List<Prestaciones> list = dao.listAll();
		int i= 1;
		for(Prestaciones cada: list) { 
			System.out.println(i + cada.toString());
			i++;
		}
	}
}
