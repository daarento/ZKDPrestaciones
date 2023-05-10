package com.springzk.pruebazk.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.ext.Selectable;


import com.springzk.pruebazk.model.Prestaciones;
import com.springzk.pruebazk.dao.PrestacionesDAO;
import com.springzk.pruebazk.dao.PrestacionesDAOImpl;
import com.springzk.pruebazk.model.Historial;
import com.springzk.pruebazk.config.MiConexion;
import com.springzk.pruebazk.dao.EntidadDAO;
import com.springzk.pruebazk.dao.EntidadDAOImpl;
import com.springzk.pruebazk.dao.HistorialDAO;
import com.springzk.pruebazk.dao.HistorialDAOImpl;
import com.springzk.pruebazk.model.Provincia;
import com.springzk.pruebazk.dao.ProvinciaDAO;
import com.springzk.pruebazk.dao.ProvinciaDAOImpl;


public class IndexController extends SelectorComposer<Component>{

	private static final long serialVersionUID = 1L;
	
	@Wire
	private Grid editButtons;
	
	@Wire
	private Button updateButton;
	
	@Wire
	private Button cerrarButton;
	
	@Wire
	private Button cerrarHistorialButton;
	
	@Wire
	private Textbox buscador;
	
	@Wire
	private Listbox prestListbox;
	
	@Wire
	private Listbox historialListbox;
	
	@Wire
	private Longbox seguridadsocialLabel;
	
	@Wire
	private Textbox dniLabel;
	
	@Wire
	private Textbox nombreLabel;
	
	@Wire
	private Textbox apellidosLabel;
	
	@Wire
	private Textbox provinciaLabel;
	
	@Wire
	private Textbox calleLabel;
	
	@Wire
	private Intbox numeroLabel;
	
	@Wire
	private Intbox codigopostalLabel;
	
	@Wire
	private Textbox ibanLabel;
	
	@Wire
	private Textbox entidadLabel;
	
	@Wire
	private Doublebox cuantiaLabel;
	
	@Wire
	private Doublebox atrasoLabel;
	
	@Wire
    private Component detallesBox;
	
	@Wire
	private Checkbox inactivoSwitch;
	
	@Wire
	private Label fechaLabel;
	
	@Wire
	private Label accionLabel;
	
	@Wire
	private Label contenidoLabel;
	
	@Wire
	private Label tCuantiaActivo;
	
	@Wire
	private Label tAtrasoActivo;
	
	@Wire
	private Label tCuantia;
	
	@Wire
	private Label tAtraso;
	
	@Wire
	private Combobox provinciaCombo;
	
	private float totalCuantiaAct;
	private float totalAtrasoAct;
	private float totalCuantia;
	private float totalAtraso;
	
	@Wire
	private List<Prestaciones> result;
	
	@Wire
	private List<Historial> hist;
	
	@Wire
	private List<Provincia> provinciaList;
	
	private Prestaciones seleccionado;
	private PrestacionesDAO dao = new PrestacionesDAOImpl();
	private HistorialDAO historialDao = new HistorialDAOImpl();
	private EntidadDAO entidadDao = new EntidadDAOImpl();
	private ProvinciaDAO provinciaDao = new ProvinciaDAOImpl();
	
	private String dniprestListbox;
		
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		//Mostrar lista al entrar en la aplicación web
		result = dao.listAll();
		prestListbox.setModel(new ListModelList<Prestaciones>(result));
		
		provinciaList = provinciaDao.listProvincia();
		provinciaCombo.setModel(new ListModelList<Provincia>(provinciaList));
		
		detallesBox.setVisible(false);
		updateButton.setVisible(false);
		cerrarButton.setVisible(false);
		
		//Historial
		cerrarHistorialButton.setVisible(false);
		historialListbox.setVisible(false);
		editButtons.setVisible(false);
		
		search();
		
	}
	
	@Listen("onClick = #searchButton; onOK = window") // buscador
	public void search() {
		totalCuantiaAct = 0;
		totalAtrasoAct = 0;
		
		totalCuantia = 0;
		totalAtraso = 0;
		
		String palabra = buscador.getValue();
		result = dao.search(palabra);
		prestListbox.setModel(new ListModelList<Prestaciones>(result));
		System.out.println("\nSe han encontrado: " + result.size() + " registros.");
		
		List<Prestaciones> listado = dao.listAll();
		
		for(Prestaciones cada: result) {
			if(!cada.isInactivo()) {
				totalCuantiaAct += cada.getCuantia();
				totalAtrasoAct += cada.getAtraso();
			}
		}
		
		for(Prestaciones cada: result) {
			totalCuantia += cada.getCuantia();
			totalAtraso += cada.getAtraso();
		}
		
		String sCuantiaAct = String.format("%.2f", totalCuantiaAct);
		tCuantiaActivo.setValue(sCuantiaAct);
		
		String sAtrasoAct = String.format("%.2f", totalAtrasoAct);
		tAtrasoActivo.setValue(sAtrasoAct);
		
		String sCuantia = String.format("%.2f", totalCuantia);
		tCuantia.setValue(sCuantia);
		
		String sAtraso = String.format("%.2f", totalAtraso);
		tAtraso.setValue(sAtraso);
	}
	
	public void actualizarTotales() {
		totalCuantiaAct = 0;
		totalAtrasoAct = 0;
		
		totalCuantia = 0;
		totalAtraso = 0;
		
		result.clear();
		result = dao.listAll();
		
		for(Prestaciones cada: result) {
			if(!cada.isInactivo()) {
				totalCuantiaAct += cada.getCuantia();
				totalAtrasoAct += cada.getAtraso();
			}
		}
		
		for(Prestaciones cada: result) {
			totalCuantia += cada.getCuantia();
			totalAtraso += cada.getAtraso();
		}
		
		String sCuantiaAct = String.format("%.2f", totalCuantiaAct);
		tCuantiaActivo.setValue(sCuantiaAct);
		
		String sAtrasoAct = String.format("%.2f", totalAtrasoAct);
		tAtrasoActivo.setValue(sAtrasoAct);
		
		String sCuantia = String.format("%.2f", totalCuantia);
		tCuantia.setValue(sCuantia);
		
		String sAtraso = String.format("%.2f", totalAtraso);
		tAtraso.setValue(sAtraso);
	}
	
	@Listen("onClick = #cerrarButton")
	public void cerrarEdicion() {
		detallesBox.setVisible(false);
		updateButton.setVisible(false);
		cerrarButton.setVisible(false);
	}
	
	@Listen("onClick = #cerrarHistorialButton")
	public void cerrarHistorial() {
		cerrarHistorialButton.setVisible(false);
		historialListbox.setVisible(false);
		editButtons.setVisible(false);
	}
	
	@Listen("onClick = #updateButton")
	public void update() {
			seleccionado = dao.show(dniprestListbox);
			System.out.println("inac: " + seleccionado.isInactivo());
			
			Prestaciones valorAntiguo = dao.show(dniprestListbox);
			
			if(seleccionado != null) {
				
				seleccionado.setSeguridadsocial(seguridadsocialLabel.getValue());
				seleccionado.setDni(dniLabel.getValue());
				seleccionado.setNombre(nombreLabel.getValue().toUpperCase());
				seleccionado.setApellidos(apellidosLabel.getValue().toUpperCase());
				seleccionado.setProvincia(provinciaCombo.getValue().toUpperCase());
				seleccionado.setCalle(calleLabel.getValue().toUpperCase());				
				seleccionado.setNumero(numeroLabel.getValue());
				seleccionado.setCodigopostal(codigopostalLabel.getValue());
				seleccionado.setIban(ibanLabel.getValue().toUpperCase());
				//seleccionado.setEntidad(entidadLabel.getValue().toUpperCase());
				seleccionado.setCuantia((float) cuantiaLabel.doubleValue());
				seleccionado.setAtraso((float) atrasoLabel.doubleValue());
				//seleccionado.setInactivo(inactivoSwitch.getValue());
				
				Long seguridadsocial = seguridadsocialLabel.getValue();
				if(seguridadsocial == null) {
					seguridadsocial = 0L;
				}
				String dni = dniLabel.getValue();
				if(dni == null) {
					dni = "";
				}
				String nombre = nombreLabel.getValue();
				if(nombre == null) {
					nombre = "";
				}
				String apellidos = apellidosLabel.getValue();
				if(apellidos == null) {
					apellidos = "";
				}
				String provincia = provinciaCombo.getValue();
				if(provincia == null) {
					provincia = "";
				}
				String calle = calleLabel.getValue();
				if(calle == null) {
					calle = "";
				}
				int numero = numeroLabel.getValue();
				if(numero == 0) {
					numero = 0;
				}
				int codigopostal = codigopostalLabel.getValue();
				if(codigopostal == 0) {
					codigopostal = 0;
				}
				String iban = ibanLabel.getValue();
				String entidad = "";
				
				if(iban.length() == 0) {
					iban = "";
					entidad = "";
					System.out.println("vacio > IBAN len: " + iban.length() + "; IBAN: " + iban);
				} 
				if(iban.length() > 0){
					iban = ibanLabel.getValue();
					EntidadDAO entidadDao = new EntidadDAOImpl();
					String codigo = iban.substring(4, 8);
					entidad = entidadDao.mostrarEntidad(codigo).getNombre();
					
					System.out.println("relleno > IBAN len: " + iban.length() + "; IBAN: " + iban);
				}
				
				//String entidad = entidadLabel.getValue();
				
				float cuantia = (float) cuantiaLabel.doubleValue();
				float atraso = (float) atrasoLabel.doubleValue();				
				
				Prestaciones p = new Prestaciones(seguridadsocial, dni, nombre, apellidos, provincia, calle, numero, codigopostal, iban, entidad, cuantia, atraso);
				dao.update(p);
				
				System.out.println("Seleccionado: " + dni + " cp: " + codigopostal + " iban: " + iban);
				
				Prestaciones valorActual = dao.show(dniprestListbox);
				
				StringBuilder sb = new StringBuilder();
				String cambio = "";
				
				//Si el dni es diferente se almacena en el arraylist
				if(valorAntiguo.getSeguridadsocial() != (valorActual.getSeguridadsocial())){ 
					sb.append("Seg. Social: " + valorAntiguo.getSeguridadsocial() + ",");
					System.out.println("Seguridad Social modificada.");
				}
				//Si el nombre es diferente
				if(!valorAntiguo.getNombre().equals(valorActual.getNombre())){
					sb.append("Nombre: " + valorAntiguo.getNombre().trim() + ",");
					System.out.println("Nombre modificado. " + valorAntiguo.getNombre());
					
				}
				//Si los apellidos son diferentes
				if(!valorAntiguo.getApellidos().equalsIgnoreCase(valorActual.getApellidos())){
					sb.append("Apellidos: " + valorAntiguo.getApellidos().trim() + ",");
					System.out.println("Apellidos modificados.");
				}
				//Si la provincia es diferente
				if(!valorAntiguo.getProvincia().equalsIgnoreCase(valorActual.getProvincia())){
					sb.append("Provincia: " + valorAntiguo.getProvincia().trim() + ",");
					System.out.println("Provincia modificada.");
				}
				//Si la calle es diferente
				if(!valorAntiguo.getCalle().equalsIgnoreCase(valorActual.getCalle())){
					sb.append("Calle: " + valorAntiguo.getCalle().trim() + ",");
					System.out.println("Calle modificada.");
				}
				//Si el número es diferente
				if(valorAntiguo.getNumero() != (valorActual.getNumero())){
					sb.append("Número: " + valorAntiguo.getNumero() + ",");
					System.out.println("Número modificado.");
				}
				//Si el código postal es diferente
				if(valorAntiguo.getCodigopostal() != (valorActual.getCodigopostal())){
					sb.append("Cód. Postal: " + valorAntiguo.getCodigopostal() + ",");
					System.out.println("Código postal modificado.");
				}
				//Si el iban es diferente
				if(!valorAntiguo.getIban().equalsIgnoreCase(valorActual.getIban())){
					sb.append("IBAN: " + valorAntiguo.getIban().trim() + ",");
					System.out.println("IBAN modificado.");
				}
				//Si el entidad es diferente
				if(!valorAntiguo.getEntidad().equalsIgnoreCase(valorActual.getEntidad())){
					sb.append("Entidad: " + valorAntiguo.getEntidad().trim() + ",");
					System.out.println("Entidad modificada.");
				}
				//Si la cuantía postal es diferente
				if(valorAntiguo.getCuantia() != (valorActual.getCuantia())){
					sb.append("Cuantía: " + valorAntiguo.getCuantia() + ",");
					System.out.println("Cuantía modificada.");
				}
				//Si el atraso postal es diferente
				if(valorAntiguo.getAtraso() != (valorActual.getAtraso())){
					sb.append("Atraso: " + valorAntiguo.getAtraso() + ",");
					System.out.println("Atraso modificado.");
				}
				
				String resultado = sb.toString();
				if(resultado.endsWith(",")) {
					resultado = resultado.substring(0, resultado.length() - 1); //si acaba en "," procedemos a quitarla
				}
				
				//Guardar en el Historial
	    		Historial historial = new Historial(dni, "MODIFICAR", resultado);
	    		
	    		if(resultado.length() == 0) {
	    			System.out.println("No hay modificaciones.");
	    		}
	    		
	    		else if(historialDao.insertar(historial) > 0) {
	    			System.out.println("Historial insertado. Acción: MODIFICAR.");
	    		}
	    		
				Clients.showNotification("Datos actualizados.");
			}
			result = dao.listAll();
			prestListbox.setModel(new ListModelList<Prestaciones>(result));
		//}
		detallesBox.setVisible(false);
		updateButton.setVisible(false);
		cerrarButton.setVisible(false);
	}
	
	@Listen("onDeleteItem = #prestListbox") // eliminar
    public void delete(final Event event) {
		String dni = (String) event.getData();
		//
		PreparedStatement ps = null;
		MiConexion miCon = new MiConexion();
		Connection con = miCon.getConnection();
		try {
	        ps = con.prepareStatement("SELECT inactivo FROM datos WHERE dni = ?");
	        ps.setString(1, dni);
	        
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				boolean inactivo = rs.getBoolean("inactivo"); //recogemos el valor booleano de la tabla inactivo
				if(!inactivo) {
					PreparedStatement psFalse = con.prepareStatement("UPDATE datos SET inactivo = ? WHERE dni = ?");
					psFalse.setBoolean(1, true);
					psFalse.setString(2, dni);
					psFalse.executeUpdate();
					
					PreparedStatement ps2 = con.prepareStatement("SELECT inactivo FROM datos WHERE dni = ?");
					ps2.setString(1, dni);
					ResultSet rs2 = ps2.executeQuery();
					
					boolean inactivoActualizado = false;
					if(rs2.next()) {
						inactivoActualizado = rs2.getBoolean("inactivo"); 
					}
					
					//recoger el nombre antes de modificar
					ps = con.prepareStatement("SELECT * FROM datos WHERE dni = ?");
					ps.setString(1, dni);
					ResultSet rs3 = ps.executeQuery();
					Prestaciones valorAntiguo = null;
					if(rs3.next()) {
						valorAntiguo = dao.show(dni);
					}
					
					//Guardar en el Historial
		    		Historial historial = new Historial(dni, "DESACTIVADO", "Se ha desactivado el registro.");
		    		if(historialDao.insertar(historial)>0) {
		    			System.out.println("Historial insertado. Acción: DESACTIVADO.");
		    		}
		    		//
					
					System.out.println("Se ha desactivado el registro '" + dni + "'. Estado actualizado > " +  inactivoActualizado);
					actualizarTotales();
					
				} else {
					PreparedStatement psTrue = con.prepareStatement("UPDATE datos SET inactivo = ? WHERE dni = ?");
					psTrue.setBoolean(1, false);
					psTrue.setString(2, dni);
					psTrue.executeUpdate();
					
					PreparedStatement ps2 = con.prepareStatement("SELECT inactivo FROM datos WHERE dni = ?");
					ps2.setString(1, dni);
					ResultSet rs2 = ps2.executeQuery();
					
					boolean inactivoActualizado = false;
					if(rs2.next()) {
						inactivoActualizado = rs2.getBoolean("inactivo"); 
					}
					
					//recoger el nombre antes de modificar
					ps = con.prepareStatement("SELECT * FROM datos WHERE dni = ?");
					ps.setString(1, dni);
					ResultSet rs3 = ps.executeQuery();
					Prestaciones valorAntiguo = null;
					if(rs3.next()) {
						valorAntiguo = dao.show(dni);
					}
					
					//Guardar en el Historial
		    		Historial historial = new Historial(dni, "ACTIVADO", "Se ha activado el registro.");
		    		if(historialDao.insertar(historial)>0) {
		    			System.out.println("Historial insertado. Acción: ACTIVADO.");
		    		}
		    		//
					System.out.println("Se ha activado el registro '" + dni + "'. Estado actualizado > " +  inactivoActualizado);
					actualizarTotales();
				}
			}
		}
		catch(Exception ex) {}
		//
		System.out.println("Seleccionado: " + event.getData());
		
		result = dao.listAll();
		prestListbox.setModel(new ListModelList<Prestaciones>(result));
    }
	
	@Listen("onShowItem = #prestListbox")
	public void mostrarDetalles(final Event event) {
		detallesBox.setVisible(true);
		updateButton.setVisible(true);
		cerrarButton.setVisible(true);
		editButtons.setVisible(true);
		
		dniprestListbox = (String) event.getData();
		Prestaciones p = dao.show(dniprestListbox);
		
		seguridadsocialLabel.setValue(p.getSeguridadsocial());
		dniLabel.setValue(p.getDni());
		nombreLabel.setValue(p.getNombre());
		apellidosLabel.setValue(p.getApellidos());
		//provinciaLabel.setValue(p.getProvincia());
		provinciaCombo.setValue(p.getProvincia());
		calleLabel.setValue(p.getCalle());
		numeroLabel.setValue(p.getNumero());
		codigopostalLabel.setValue(p.getCodigopostal());
		ibanLabel.setValue(p.getIban());
		entidadLabel.setValue(p.getEntidad());
		cuantiaLabel.setValue(p.getCuantia());
		atrasoLabel.setValue(p.getAtraso());
		
		System.out.println("click: " + dniprestListbox);
	}
	
	@Listen("onSelect = #prestListbox")
	public void mostrarHistorial() {
		
		Set<Prestaciones> select = ((Selectable<Prestaciones>) prestListbox.getModel()).getSelection();
		if(select != null && !select.isEmpty()) {
			Prestaciones selecc = select.iterator().next();
			hist = historialDao.mostrar(selecc.getDni());
			
			if(hist.size() != 0) {
				//Historial
				cerrarHistorialButton.setVisible(true);
				historialListbox.setVisible(true);
				editButtons.setVisible(true);
				
				historialListbox.setModel(new ListModelList<Historial>(hist));
			}
			if(hist.size() == 0) {
				cerrarHistorialButton.setVisible(false);
				historialListbox.setVisible(false);
				editButtons.setVisible(false);
				
				hist.clear();
				historialListbox.setModel(new ListModelList<Historial>(hist));
			}
			System.out.println("elemento histo: " + hist.toString());			
		}
	}
	//ENVIAR INFO AL HEADER (GET) DE OTRO .ZUL Y ENVIAR INFO ENTRE ARCHIVOS .ZUL (POST)
	/*@Listen("onClick = #enviarInfo")
	public void navegarAotraVentana() {
		String nombre = "Daniel Arencibia Torralbo";
	    Executions.sendRedirect("/formulario?nombre=" + nombre);
	    Executions.getCurrent().getSession().setAttribute("nombrecito", nombre);
	    
	    System.out.println("?nombre=" + nombre);
	}*/
	
	@Listen("onChange = #ibanLabel")
	public void actualizarEntidad() {
		String iban = ibanLabel.getValue();
		String codigo = iban.substring(4, 8);
		String ent = entidadDao.mostrarEntidad(codigo).getNombre();
		entidadLabel.setValue(ent);
	}
}