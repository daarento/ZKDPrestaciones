package com.springzk.pruebazk.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.springzk.pruebazk.model.Entidad;
import com.springzk.pruebazk.config.MiConexion;

public class EntidadDAOImpl implements EntidadDAO{
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	public EntidadDAOImpl() {
		MiConexion conn = new MiConexion();
		dataSource = conn.getDataSource();
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public Entidad mostrarEntidad(String codigo) {
		String sql = "SELECT * FROM entidad WHERE codigo ='" + codigo + "'";
		
		ResultSetExtractor<Entidad> extractor = new ResultSetExtractor<Entidad>() {

			@Override
			public Entidad extractData(ResultSet rs) throws SQLException, DataAccessException {
				if(rs.next()) {
					String codigo = rs.getString("codigo");
					String nombre = rs.getString("nombre");
					return new Entidad(codigo, nombre);
				}
				return null;
			}
		};
		return jdbcTemplate.query(sql, extractor);
	}

	@Override
	public List<Entidad> listEntidades() {
		String sql = "SELECT * FROM entidad";
		
		RowMapper<Entidad> rowMapper = new RowMapper<Entidad>() {

			@Override
			public Entidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				String codigo = rs.getString("codigo");
				String nombre = rs.getString("nombre");
				return new Entidad(codigo, nombre);
			}			
		};
		return jdbcTemplate.query(sql, rowMapper);
	}

}
