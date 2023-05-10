package com.springzk.pruebazk.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.springzk.pruebazk.config.MiConexion;
import com.springzk.pruebazk.model.Provincia;

public class ProvinciaDAOImpl implements ProvinciaDAO{
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	public ProvinciaDAOImpl() {
		MiConexion conn = new MiConexion();
		dataSource = conn.getDataSource();
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public Provincia mostrarProvincia(String codigo) {
		String sql = "SELECT nombre FROM provincia WHERE codigo ='" + codigo + "'";
		
		ResultSetExtractor<Provincia> extractor = new ResultSetExtractor<Provincia>() {

			@Override
			public Provincia extractData(ResultSet rs) throws SQLException, DataAccessException {
				if(rs.next()) {
					String nombre = rs.getString("nombre");
					return new Provincia(nombre);
				}
				return null;
			}
		};
		return jdbcTemplate.query(sql, extractor);
	}

	@Override
	public List<Provincia> listProvincia() {
		String sql = "SELECT nombre FROM provincia";
		
		RowMapper<Provincia> rowMapper = new RowMapper<Provincia>() {

			@Override
			public Provincia mapRow(ResultSet rs, int rowNum) throws SQLException {
				String nombre = rs.getString("nombre");
				return new Provincia(nombre);
			}			
		};
		return jdbcTemplate.query(sql, rowMapper);
	}
}