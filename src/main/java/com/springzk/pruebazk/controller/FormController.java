package com.springzk.pruebazk.controller;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.ListModelList;

import com.springzk.pruebazk.dao.EntidadDAO;
import com.springzk.pruebazk.dao.EntidadDAOImpl;
import com.springzk.pruebazk.dao.PrestacionesDAO;
import com.springzk.pruebazk.dao.PrestacionesDAOImpl;
import com.springzk.pruebazk.model.Prestaciones;
import com.springzk.pruebazk.model.Provincia;
import com.springzk.pruebazk.dao.ProvinciaDAO;
import com.springzk.pruebazk.dao.ProvinciaDAOImpl;

public class FormController extends SelectorComposer<Component>{

	private static final long serialVersionUID = 1L;
		
	@Wire
	Longbox seguridadsocialLongbox;
	
	@Wire
	Textbox dniTextbox;
	
	@Wire
	Textbox nombreTextbox;
	
	@Wire
	Textbox apellidosTextbox;
	
	/*@Wire
	Textbox provinciaTextbox;*/
	
	@Wire
	Combobox provinciaCombo;
	
	@Wire
	Textbox calleTextbox;
	
	@Wire
	Intbox numeroIntbox;
	
	@Wire
	Intbox codigopostalIntbox;
	
	@Wire
	Textbox ibanTextbox;
	
	@Wire
	Textbox entidadTextbox;
	
	@Wire
	Doublebox cuantiaDoublebox;
	
	@Wire
	Doublebox atrasoDoublebox;
	
	@Wire
	Label labelNombre;
	
	@Wire
	private List<Provincia> listadoProvincias;
	
	private PrestacionesDAO dao = new PrestacionesDAOImpl();
	private EntidadDAO entidadDao = new EntidadDAOImpl();
	private ProvinciaDAO provinciaDao = new ProvinciaDAOImpl();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	    
		listadoProvincias = provinciaDao.listProvincia();
		provinciaCombo.setModel(new ListModelList<Provincia>(listadoProvincias));
		/* RECOGER INFO DE OTRO .ZUL
		String nombre = (String) Sessions.getCurrent().getAttribute("nombrecito");
		labelNombre.setValue(nombre);
		System.out.println("recogido: " + nombre);*/
	}
	
	@Listen("onClick = #insertarButton")
	public void insert() throws IllegalStateException{
		Long seguridadsocial = seguridadsocialLongbox.longValue();
		
		String dni = dniTextbox.getValue();
		
		String nombre = nombreTextbox.getValue();
		
		String apellidos = apellidosTextbox.getValue();
		
		String provincia = provinciaCombo.getValue();
		
		String calle = calleTextbox.getValue();
		
		int numero = numeroIntbox.intValue();
		
		int codigopostal = codigopostalIntbox.intValue();
		
		String iban = ibanTextbox.getValue();
		
		//String entidad = entidadTextbox.getValue();
		
		float cuantia = (float) cuantiaDoublebox.doubleValue();
		
		float atraso = (float) atrasoDoublebox.doubleValue();
		
		if(seguridadsocial == 0) {
			seguridadsocialLongbox.setErrorMessage("Introduzca el número de la seguridad social.");
		}
		
		if(dni.isEmpty() || (dni==null)) {
			dniTextbox.setErrorMessage("Introduzca el DNI.");
		} else if(dni.length() > 9) {
			dniTextbox.setErrorMessage("DNI inválido.");
		}
		
		if(nombre.isEmpty() || (nombre == null)){ 
			//alert("No puede haber campos vacíos.");
			nombreTextbox.setErrorMessage("Introduzca un nombre.");
		} 
		
		if(apellidos.isEmpty() || (apellidos == null)){ 
			//alert("No puede haber campos vacíos.");
			apellidosTextbox.setErrorMessage("Introduzca el/los apellido/s.");
		}
		
		if(provincia.isEmpty() || (provincia == null)){ 
			//alert("No puede haber campos vacíos.");
			provinciaCombo.setErrorMessage("Seleccione una provincia.");
		}
		
		if(calle.isEmpty() || (calle == null)){ 
			//alert("No puede haber campos vacíos.");
			calleTextbox.setErrorMessage("Introduzca una calle.");
		}
		
		if(numero <= 0) {
			//alert("Debe introducir una edad.");
			numeroIntbox.setErrorMessage("Introduzca un valor.");
		}
		
		String sCodpostal = String.valueOf(codigopostal);
		if(sCodpostal.length() == 0) {
			//alert("Debe introducir una edad.");
			codigopostalIntbox.setErrorMessage("Introduzca un valor válido.");
		} else if(sCodpostal.length() < 5 || sCodpostal.length() > 5) {
			codigopostalIntbox.setErrorMessage("El código postal dispone de 5 dígitos.");
		}
		
		if(iban == null || iban.isEmpty()) {
			ibanTextbox.setErrorMessage("Introduzca un IBAN");
		} else if(iban.length() < 24 || iban.length() > 24) {
			ibanTextbox.setErrorMessage("Introduzca los 24 carácteres (incluyendo las letras).");
		}
		
		/*if(entidad == null || entidad.isEmpty()) {
			entidadTextbox.setErrorMessage("Introduzca una entidad.");
		} */
		
		if(cuantia < 0) {
			cuantiaDoublebox.setErrorMessage("No se admiten valores negativos.");
		}
		
		if(atraso < 0) {
			atrasoDoublebox.setErrorMessage("No se admiten valores negativos.");
		}
		
		if((seguridadsocial != 0) && (dni.length() == 9) && (!nombre.isEmpty()) && (!apellidos.isEmpty())
				&& (!provincia.isEmpty()) && (!calle.isEmpty()) && (numero > 0) && (sCodpostal.length() == 5)
				&& (iban.length() == 24) /*&& (!entidad.isEmpty())*/) {
			System.out.println("Registro insertado con d: " + dni + " ss: " + seguridadsocial);
			
			String entidad = "";
			if(iban.length() != 0 && (iban.isEmpty())) {
				String codigo = iban.substring(4, 8);
				entidad = entidadDao.mostrarEntidad(codigo).getNombre();	
			}
			
			Prestaciones p = new Prestaciones(seguridadsocial, dni, nombre, apellidos, provincia, calle, numero, codigopostal, iban, entidad, cuantia, atraso);
			dao.insert(p);
			
			Executions.sendRedirect("/");
		}
	}
	
	@Listen("onChange = #ibanTextbox")
	public void actualizarEntidad() {
		String iban = ibanTextbox.getValue();
		String codigo = iban.substring(4, 8);
		String ent = entidadDao.mostrarEntidad(codigo).getNombre();
		entidadTextbox.setValue(ent);
	}
}