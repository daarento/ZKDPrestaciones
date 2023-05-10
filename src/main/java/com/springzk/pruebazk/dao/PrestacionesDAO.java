package com.springzk.pruebazk.dao;

import java.util.List;

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
