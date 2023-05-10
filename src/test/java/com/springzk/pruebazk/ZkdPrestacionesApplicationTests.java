package com.springzk.pruebazk;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.springzk.pruebazk.config.MiConexion;
import com.springzk.pruebazk.dao.EntidadDAO;
import com.springzk.pruebazk.dao.EntidadDAOImpl;
import com.springzk.pruebazk.dao.PrestacionesDAO;
import com.springzk.pruebazk.dao.PrestacionesDAOImpl;
import com.springzk.pruebazk.model.Entidad;
import com.springzk.pruebazk.model.Prestaciones;

@SpringBootTest
class ZkdPrestacionesApplicationTests {

	private DataSource dataSource;
	private PrestacionesDAO dao;
	private EntidadDAO entidadDao;
	
	private Connection con;	
	
	//Para que realice esto antes de cada método. Crear la conexion con la base de datos
	@BeforeEach
	void setupBeforeEach() {
		MiConexion con = new MiConexion();
		//this.con = con.getConnection();
		
		dataSource = con.getDataSource();
		
		dao = new PrestacionesDAOImpl();
		
		entidadDao = new EntidadDAOImpl();
	}
	
	@Test
	void insertarTest() {
		ArrayList<Prestaciones> lista = new ArrayList<>();
		Prestaciones p1 = new Prestaciones(467160035842L, "64742343B", "DAVID", "RODRIGUEZ SEVILLA", "SORIA", "CAMPO CATALUNYA", 25,
				42883, "ES3920387248254561449020", "BANKIA", 0, 0);
		Prestaciones p2 = new Prestaciones(344520631181L, "50336362B", "GREGORIA", "REBOLLO CLEMENTE", "LLEIDA", "PASSATGE REAL", 20,
				25768, "ES2420808294293889597119", "UNICAJA", 0, 0);
		lista.add(p1);
		lista.add(p2);
		
		//result = dao.insert(p1);
		int result = 0;
		
		for(int i=0; i<lista.size(); i++) {
			result = dao.insert(lista.get(i));
			assertTrue(result > 0);
			
			if(result > 0) {
				System.out.println("Se ha insertado el registro: " + lista.get(i).getDni());
			}
		}		
	}
	
	@Test
	void actualizarTest() {
		Prestaciones p1 = new Prestaciones(467160035842L, "64742343B", "DAVID DANIEL", "RODRIGUEZ SEVILLA", "SORIA", "CAMPO CATALUNYA", 54,
				42883, "ES3920387248254561449020", "SANTADER", 0, 0, true);
		int result = dao.update(p1);
		assertTrue(result > 0);
		
		if(result > 0) {
			System.out.println("Se ha actualizado el registro: " + p1.getDni());
		}
	}
	
	@Test
	void eliminarTest() {
		String dni = "64742343B";
		int result = dao.delete(dni);
		assertTrue(result > 0);
		
		if(result > 0) {
			System.out.println("Se ha eliminado el registro: " + dni);
		}
	}  
	
	@Test
	void listarTest() {
		List<Prestaciones> list = dao.listAll();
		int i= 1;
		for(Prestaciones cada: list) { 
			System.out.println(i + cada.toString());
			i++;
		}
	}
	
	@Test
	void separarIban() {
		String iban = "ES6020386518154733299986";
		String parte2 = iban.substring(4, 8);
		System.out.println("Código de la entidad: " + parte2);
		
		Entidad db = entidadDao.mostrarEntidad(parte2);
		System.out.println("Entidad: " + db.getNombre());
	}
	
	@Test
	void listarEntidades() {
		List<Entidad> lista = entidadDao.listEntidades();
		int i= 1;
		for(Entidad cada: lista) { 
			System.out.println(i + cada.toString());
			i++;
		}
	}
	
}
