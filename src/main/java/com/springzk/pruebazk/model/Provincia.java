package com.springzk.pruebazk.model;

import java.util.ArrayList;

public class Provincia {
	private Integer id;
	private String codigo, nombre;
	private ArrayList<String> contenido = new ArrayList<>();
	
	public Provincia() {}
	
	public Provincia(Integer id, String codigo, String nombre) {
		this.id = id;
		this.codigo = codigo;
		this.nombre = nombre;
	}
	
	public Provincia(String codigo, String nombre) {
		this.codigo = codigo;
		this.nombre = nombre;
	}
	
	public Provincia(String nombre) {
		this.nombre = nombre;
	}
	
	public Integer getId() {
		return id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setContenido(ArrayList<String> contenido) {
		this.contenido = contenido;
	}
	public ArrayList<String> getContenido(){
		return contenido;
	}	
	@Override
	public String toString() {
		return getCodigo() + " " + getNombre() + "\n";
	}
}
