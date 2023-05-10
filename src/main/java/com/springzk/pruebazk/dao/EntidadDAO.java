package com.springzk.pruebazk.dao;

import java.util.List;

import com.springzk.pruebazk.model.Entidad;

public interface EntidadDAO {
	public Entidad mostrarEntidad(String codigo);
	
	public List<Entidad> listEntidades();
}
