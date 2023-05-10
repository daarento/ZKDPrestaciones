package com.springzk.pruebazk.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zul.Button;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;

import java.io.InputStream;
import java.io.FileInputStream;

import java.io.StringReader;

import com.springzk.pruebazk.config.MiConexion;
import com.springzk.pruebazk.model.Prestaciones;
import com.springzk.pruebazk.dao.PrestacionesDAO;
import com.springzk.pruebazk.dao.PrestacionesDAOImpl;
import com.opencsv.CSVReader;


public class FilesController extends SelectorComposer<Component>{

	private static final long serialVersionUID = 1L;
	
	@Wire
	private Button downXmlButton;
	
	@Wire
	private Button uploadCsvButton;
	
	@Wire
	private Listbox prestListbox;
	
	@Wire
	private List<Prestaciones> result;
	
	@WireVariable
	private HttpServletResponse response;
	
	private PrestacionesDAO prestacionesDao = new PrestacionesDAOImpl();;
	
	private MiConexion miConexion = new MiConexion();
	private Connection con = miConexion.getConnection();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	}
	
	@Listen("onClick = #downXmlButton")
	public void downloadXml() {
		try {
			 MiConexion miConexion = new MiConexion();
			 Connection con = miConexion.getConnection();
	         Statement stmt = con.createStatement();
	         ResultSet rs = stmt.executeQuery("SELECT * FROM datos");
	         
	         DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	         Document doc = docBuilder.newDocument();
	         Element rootElement = doc.createElement("prestaciones");
	         doc.appendChild(rootElement);
	         
	         while (rs.next()) {
	        	boolean inactivo = rs.getBoolean("inactivo"); //recogemos el valor de la tabla de la bbdd "inactivo"
	        	
	        	if(!inactivo) { //si el registro tiene valor inactivo = false, genera el xml
	        		Element registro = doc.createElement("registro");
		            rootElement.appendChild(registro);
		            
		            Element seguridadsocial = doc.createElement("seguridadsocial");
		            seguridadsocial.appendChild(doc.createTextNode(rs.getString("seguridadsocial")));
		            registro.appendChild(seguridadsocial);
		            
		            Element dni = doc.createElement("dni");
		            dni.appendChild(doc.createTextNode(rs.getString("dni")));
		            registro.appendChild(dni);
		            
		            Element nombre = doc.createElement("nombre");
		            nombre.appendChild(doc.createTextNode(rs.getString("nombre")));
		            registro.appendChild(nombre);
		            
		            Element apellidos = doc.createElement("apellidos");
		            apellidos.appendChild(doc.createTextNode(rs.getString("apellidos")));
		            registro.appendChild(apellidos);
		            
		            Element provincia = doc.createElement("provincia");
		            provincia.appendChild(doc.createTextNode(rs.getString("provincia")));
		            registro.appendChild(provincia);
		            
		            Element calle = doc.createElement("calle");
		            calle.appendChild(doc.createTextNode(rs.getString("calle")));
		            registro.appendChild(calle);
		            
		            Element numero = doc.createElement("numero");
		            numero.appendChild(doc.createTextNode(rs.getString("numero")));
		            registro.appendChild(numero);
		            
		            Element codigopostal = doc.createElement("codigopostal");
		            codigopostal.appendChild(doc.createTextNode(rs.getString("codigopostal")));
		            registro.appendChild(codigopostal);
		            
		            Element iban = doc.createElement("iban");
		            iban.appendChild(doc.createTextNode(rs.getString("iban")));
		            registro.appendChild(iban);
		            
		            Element entidad = doc.createElement("entidad");
		            entidad.appendChild(doc.createTextNode(rs.getString("entidad")));
		            registro.appendChild(entidad);
		            
		            Element cuantia = doc.createElement("cuantia");
		            cuantia.appendChild(doc.createTextNode(rs.getString("cuantia")));
		            registro.appendChild(cuantia);
		            
		            Element atraso = doc.createElement("atraso");
		            atraso.appendChild(doc.createTextNode(rs.getString("atraso")));
		            registro.appendChild(atraso);
	        	}
	         }
	         
	         String home = System.getProperty("user.home") + File.separator + "Downloads" + File.separator ;
	 		 String path = home + "prestaciones.xml";
	 		 
	 		 
	 		 
	         TransformerFactory transformerFactory = TransformerFactory.newInstance();
	         Transformer transformer = transformerFactory.newTransformer();
	         DOMSource source = new DOMSource(doc);
	         StreamResult result = new StreamResult(new File(path));
	         transformer.transform(source, result);
	         
	         System.out.println("Los registros se han almacenado en el archivo prestaciones.xml. En la ruta: " + path);
	         //termina la respuesta	         
	         rs.close();
	         stmt.close();
	         con.close();
	         
	         Clients.showNotification("Archivo descargado en: " + path, 
	        		 Notification.TYPE_INFO, downXmlButton, "after_start", 4000, true);
	         // Abrir el fichero al descargarse
	         /*File file = new File(path);
	         ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "start", path);
	         pb.start();*/
	         
	         File file = new File(path);
	         
	         /*try(InputStream is = new FileInputStream(file)){
	        	 AMedia media = new AMedia("prestaciones.xml", "xml", "application/xml", new FileInputStream(file));
	        	 Filedownload.save(media); //selecciona donde guardar el archivo
	         } catch (FileNotFoundException e) {
	        	    e.printStackTrace();
	         } catch (IOException e) {
	             e.printStackTrace();
	         }*/
	         
	         if(!file.exists()) {
	        	 String outputDir = "/output";
	             File outputFolder = new File(outputDir);
	             if (!outputFolder.exists()) {
	                 outputFolder.mkdirs(); // create the folder if it doesn't exist
	             }
	             String pathaux = outputFolder.getAbsolutePath() + File.separator + "prestaciones.xml";
	             File fileaux = new File(pathaux);
	             
	             try(InputStream is = new FileInputStream(fileaux)){
		        	 AMedia mediaaux = new AMedia("prestaciones.xml", "xml", "application/xml", new FileInputStream(fileaux));
		        	 Filedownload.save(mediaaux); //selecciona donde guardar el archivo
		         } catch (FileNotFoundException e) {
		        	    e.printStackTrace();
		         } catch (IOException e) {
		             e.printStackTrace();
		         }
	         } 
	         
	      } catch (Exception e) {
	    	  System.out.println("Ha ocurrido un error al intentar descargar/abrir el archivo xml: " + e);
	         e.printStackTrace();
	      }
	}

	@Listen("onClick = #uploadCsvButton")
	public void uploadCsv() {
		Fileupload.get(new EventListener<UploadEvent>(){

			@Override
			public void onEvent(UploadEvent event) throws Exception {
				// TODO Auto-generated method stub
				Media media = event.getMedia();
				String nombreCsv = event.getMedia().getName();
				File archivoCsv = new File(nombreCsv);
				String path = archivoCsv.getAbsolutePath();
				
				System.out.println("csv: " + nombreCsv + "\npath: " + path);
				
				if(nombreCsv.contains(".csv")) {
					String data = media.getStringData();
					
					CSVReader reader = new CSVReader(new StringReader(data));
					String[] linea;
					
					PreparedStatement ps = null;
					
					try {
						while ((linea = reader.readNext()) != null) {
						    // procesar cada línea del archivo CSV
							Prestaciones prest = new Prestaciones();
							if(linea.length == 3) {
								prest = new Prestaciones(linea[0], linea[1], linea[2]);
							} 
							else {
								prest = new Prestaciones(Long.parseLong(linea[0]), linea[1], linea[2], linea[3], linea[4], linea[5], Integer.parseInt(linea[6]), Integer.parseInt(linea[7]), linea[8], linea[9], Float.parseFloat(linea[10]), Float.parseFloat(linea[11]));
							}
							
							ps = con.prepareStatement("SELECT * FROM datos WHERE dni = ?");
					        ps.setString(1, prest.getDni());
					        ResultSet rs = ps.executeQuery();
					        
					        System.out.println("Prest: " + prest.getDni());
					        
					        if(rs.next()) {
					        	if(prestacionesDao.update(prest) > 0) {
					        		System.out.println("Registro con DNI '" + prest.getDni() + "' existente. Datos modificados con éxito.");
					        	} else {
					        		System.out.println("No se ha podido modificar el registro: '" + prest.getDni() + "'.");
					        	}
					        } else { //si no existe, insertar en la base de datos
					        	if(prestacionesDao.insert(prest) > 0) {
					        		System.out.println("Registro con DNI: '" + prest.getDni() + "' insertado con éxito.");
					        	} else {
					        		System.out.println("No se ha podido insertar el registro: '" + prest.getDni() + "'.");
					        	}
					        }
						}
					} catch(Exception ex) {
						System.out.println("Ha ocurrido un error al intentar subir/actualizar los datos del fichero csv: " + ex);
					} finally {
						result = prestacionesDao.listAll();
						prestListbox.setModel(new ListModelList<Prestaciones>(result));
	                }
				}
			}
		});
	}
}