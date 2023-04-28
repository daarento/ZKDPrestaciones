package com.springzk.pruebazk.dao;

import java.util.List;

import com.springzk.pruebazk.model.Historial;

public interface HistorialDAO {
	public int insertar(Historial historial);
	
	public List<Historial> mostrar(String dni);
	
	public Historial mostrar1(String dni);
	
	public int eliminar(String dni);
}
