package com.springzk.pruebazk.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.springzk.pruebazk.model.Prestaciones;

public interface PrestacionesDAO {

	public int insert(Prestaciones p);
	
	public int update(Prestaciones p);
	
	public int updatePorNombre(Prestaciones p, String nombre_old); //este método eliminar cuando utilicemos la tabla "datos"
	
	public int delete(String nombre);
	
	public List<Prestaciones> listAll();
	
	public List<Prestaciones> search(String keyword);

	public List<Prestaciones> listar();
	
	public Prestaciones show(String dni);
	
	public int actividad(String dni);
}
