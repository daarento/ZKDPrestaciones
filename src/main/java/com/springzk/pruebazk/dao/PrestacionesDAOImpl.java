package com.springzk.pruebazk.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

import java.text.DecimalFormat;

import com.springzk.pruebazk.model.Historial;
import com.springzk.pruebazk.dao.HistorialDAOImpl;
import com.springzk.pruebazk.config.MiConexion;
import com.springzk.pruebazk.model.Prestaciones;

public class PrestacionesDAOImpl implements PrestacionesDAO{
	
	private Connection con;
	private DataSource dataSource;
	
	private JdbcTemplate jdbcTemplate;
	
	private List<Prestaciones> prestacionesList= new LinkedList<Prestaciones>();
	private int id = 1;
	
	public PrestacionesDAOImpl() {
		MiConexion conn = new MiConexion();
		//con = conn.getConnection();
		
		dataSource = conn.getDataSource();
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		
		prestacionesList.add(new Prestaciones(id++, "Daniel", "Arencibia", 27));
		prestacionesList.add(new Prestaciones(id++, "Raúl", "Arencibia", 23));
		prestacionesList.add(new Prestaciones(id++, "Yasmina", "El Abid", 24));
		prestacionesList.add(new Prestaciones(id++, "Marta", "Torralbo", 60));
		prestacionesList.add(new Prestaciones(id++, "Braulio", "Arencibia", 60));
		prestacionesList.add(new Prestaciones(id++, "Cronos", "Border Collie", 2));
	}
	
	/*
	 * HAY QUE CAMBIAR LOS METODOS AL USO DE JDBCTEMPLATE 
	 * YA QUE ESTÁN HECHOS CON CONNECTION (QUE TAMBIÉN FUNCIONA)
	 * PERO ES MÁS SEGURO UTILIZAR JDBCTEMPLATE
	 */

	@Override
	public int insert(Prestaciones p) {
		// TODO Auto-generated method stub
		if(p.getSeguridadsocial() != 0) {
			String sql = "INSERT INTO datos (seguridadsocial, dni, nombre, apellidos, provincia, calle, numero, codigopostal, iban, entidad, cuantia, atraso) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

			return jdbcTemplate.update(sql, p.getSeguridadsocial(), p.getDni().toUpperCase(), p.getNombre().toUpperCase(), p.getApellidos().toUpperCase(), p.getProvincia().toUpperCase(), p.getCalle().toUpperCase(), p.getNumero(), p.getCodigopostal(), p.getIban().toUpperCase(), p.getEntidad().toUpperCase(), p.getCuantia(), p.getAtraso());
		
		} else { //si solo contiene 3 cabeceras (dni, nombre y apellidos) insertar el registro en la base de datos como inactivo
			String sql = "INSERT INTO datos (dni, nombre, apellidos, inactivo) VALUES (?,?,?,?)";
			return jdbcTemplate.update(sql, p.getDni().toUpperCase(), p.getNombre().toUpperCase(), p.getApellidos().toUpperCase(), true);
		}
	}

	@Override
	public int update(Prestaciones p) {
		// TODO Auto-generated method stub
		String sql = "UPDATE datos SET seguridadsocial = ?, nombre = ?, apellidos = ?, provincia = ?, calle = ?, numero = ?, codigopostal = ?, iban = ?, entidad = ?, cuantia = ?, atraso = ? WHERE dni = ?";
		
		return jdbcTemplate.update(sql, p.getSeguridadsocial(), p.getNombre().toUpperCase(), p.getApellidos().toUpperCase(), p.getProvincia().toUpperCase(), p.getCalle().toUpperCase(), p.getNumero(), p.getCodigopostal(), p.getIban().toUpperCase(), p.getEntidad().toUpperCase(), p.getCuantia(), p.getAtraso(), p.getDni().toUpperCase());
	}

	@Override
	public int delete(String dni) {
		// TODO Auto-generated method stub
		String sql = "DELETE FROM datos WHERE dni = '" + dni + "'";
		return jdbcTemplate.update(sql);
	}

	@Override
	public List<Prestaciones> listAll() {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM datos";
		
		RowMapper<Prestaciones> rowMapper = new RowMapper<Prestaciones>() {

			@Override
			public Prestaciones mapRow(ResultSet rs, int rowNum) throws SQLException {
				Long seguridadsocial = rs.getLong("seguridadsocial");
				String dni = rs.getString("dni");
				String nombre = rs.getString("nombre");
				String apellidos = rs.getString("apellidos");
				String provincia = rs.getString("provincia");
				String calle = rs.getString("calle");
				int numero = rs.getInt("numero");
				int codigopostal = rs.getInt("codigopostal");
				String iban = rs.getString("iban");
				String entidad = rs.getString("entidad");
				
				float cuantia = rs.getFloat("cuantia");
				String sCuantia = String.format("%.2f", cuantia).replace(",", ".");
				float fCuantia = Float.parseFloat(sCuantia);
				
				float atraso = rs.getFloat("atraso");
				String sAtraso = String.format("%.2f", atraso).replace(",", ".");
				float fAtraso = Float.parseFloat(sAtraso);
				
				boolean inactivo = rs.getBoolean("inactivo");
				
				return new Prestaciones(seguridadsocial, dni, nombre, apellidos, provincia, calle, numero, codigopostal, iban, entidad, fCuantia, fAtraso, inactivo);
				}
		};
		
		return jdbcTemplate.query(sql, rowMapper);
	}
	
	@Override
	public List<Prestaciones> listar(){
		List<Prestaciones> lista = new ArrayList<Prestaciones>();
		Prestaciones p = null;
		String sql = "SELECT * FROM prueba";
		PreparedStatement ps = null;
		
		try {
			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				String nombre = rs.getString("nombre");
				String apellidos = rs.getString("apellidos");
				Integer edad = rs.getInt("edad");
				
				p = new Prestaciones(nombre, apellidos, edad);
				lista.add(p);
			}							
			return lista;
		}catch(SQLException ex) {
			System.out.println("Error al intentar listar los registros.");
			return null;
		}
	}

	@Override
	public List<Prestaciones> search(String keyword) {
		List<Prestaciones> result = new LinkedList<Prestaciones>();
		if (keyword==null || "".equals(keyword)){
			result = listAll();
		}else{
			for (Prestaciones c: listAll()){
				if ((c.getNombre().toUpperCase().contains(keyword.toUpperCase())
					||c.getApellidos().toUpperCase().contains(keyword.toUpperCase()))){
					result.add(c);
				}
			}
		}
		return result;
	}

	@Override
	public int updatePorNombre(Prestaciones p, String nombre_old) {
		// TODO Auto-generated method stub
		String sql = "UPDATE prueba SET nombre = ?, apellidos = ?, edad = ? WHERE nombre = ?"; 
		return jdbcTemplate.update(sql, p.getNombre().toUpperCase(), p.getApellidos().toUpperCase(), p.getEdad(), nombre_old);
	}

	@Override
	public Prestaciones show(String dni) {
		String sql = "SELECT * FROM datos WHERE dni = '" + dni + "'";
		
		ResultSetExtractor<Prestaciones> extractor = new ResultSetExtractor<Prestaciones>() {
			@Override
			public Prestaciones extractData(ResultSet rs) throws SQLException, DataAccessException {
				if(rs.next()) {
					Long seguridadsocial = rs.getLong("seguridadsocial");
					String dni = rs.getString("dni");
					String nombre = rs.getString("nombre");
					String apellidos = rs.getString("apellidos");
					String provincia = rs.getString("provincia");
					String calle = rs.getString("calle");
					int numero = rs.getInt("numero");
					int codigopostal = rs.getInt("codigopostal");
					String iban = rs.getString("iban");
					String entidad = rs.getString("entidad");
					float cuantia = rs.getFloat("cuantia");
					float atraso = rs.getFloat("atraso");
					
					return new Prestaciones(seguridadsocial, dni, nombre, apellidos, provincia, calle, numero, codigopostal, iban, entidad, cuantia, atraso);
				}
				return null;
			}
		};
		return jdbcTemplate.query(sql, extractor);
	}

	
	@Override
	public int actividad(String dni) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		HistorialDAO historialDAO = new HistorialDAOImpl();
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
						valorAntiguo = show(dni);
					}
					//
					
					//Guardar en el Historial
		    		Historial historial = new Historial(dni, "DESACTIVADO", "Se ha desactivado el registro.");
		    		if(historialDAO.insertar(historial)>0) {
		    			System.out.println("Historial insertado. Acción: DESACTIVADO.");
		    		}
		    		//
					
					System.out.println("Se ha desactivado el registro '" + dni + "'. Estado actualizado > " +  inactivoActualizado);

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
					    //request.setAttribute("estado", inactivoActualizado); 
					}
					
					//recoger el nombre antes de modificar
					ps = con.prepareStatement("SELECT * FROM datos WHERE dni = ?");
					ps.setString(1, dni);
					ResultSet rs3 = ps.executeQuery();
					Prestaciones valorAntiguo = null;
					if(rs3.next()) {
						valorAntiguo = show(dni);
					}
					//
					
					//Guardar en el Historial
		    		Historial historial = new Historial(dni, "ACTIVADO", "Se ha activado el registro.");
		    		if(historialDAO.insertar(historial)>0) {
		    			System.out.println("Historial insertado. Acción: ACTIVADO.");
		    		}
		    		//
					
					
					System.out.println("Se ha activado el registro '" + dni + "'. Estado actualizado > " +  inactivoActualizado);
					//request.getSession().setAttribute("actividad", inactivoActualizado);
				}
			}
			return 1;
		}
		catch(Exception ex) {
			return 0;
		}
		//
	}

	
}