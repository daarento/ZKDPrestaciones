package com.springzk.pruebazk.dao;

import java.util.List;

import com.springzk.pruebazk.model.Provincia;

public interface ProvinciaDAO {
	public Provincia mostrarProvincia(String codigo);
	
	public List<Provincia> listProvincia();
}
