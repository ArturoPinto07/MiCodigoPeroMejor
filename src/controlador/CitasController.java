package controlador;

import excepciones.AforoMaximoException;
import excepciones.CamposVaciosException;
import excepciones.HistoriaClinicaException;
import modelo.CitaPaciente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import dao.CitasDao;
import dao.DbConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CitasController {
	Connection cn;
	List<CitaPaciente>citas=new ArrayList<CitaPaciente>();
	public CitasController(Connection cn) {
		this.cn=cn;
	}
	public List<CitaPaciente> getCitas(String sql) throws DateTimeParseException, SQLException, HistoriaClinicaException, CamposVaciosException{
		CitasDao citasDao=new CitasDao(cn);
		return citas=citasDao.getCitasPst(sql);
	}
	
	//AGREGAR
	public int agregarCitas(CitaPaciente citaPaciente) throws SQLException {
		int rows=0;
			CitasDao citasDao=new CitasDao(cn);
			rows=citasDao.agregarCitasPst(citaPaciente);
		return rows;
	}
	public int getDisponibilidad(String especialidad,String fecha) throws SQLException {
		int numCitas=0;
			CitasDao citasDao=new CitasDao(cn);
			numCitas=citasDao.getDisponibilidadPst(especialidad,fecha);
		return numCitas;
	}
	public int modificarCitas(CitaPaciente citaPaciente) throws SQLException {
		int rows=0;
			CitasDao citasDao=new CitasDao(cn);
			rows=citasDao.modificarCitaPst(citaPaciente);
		return rows;
	}
	public int borrarCitas(CitaPaciente citaPaciente) throws SQLException {
		int rows=0;
		CitasDao citasDao=new CitasDao(cn);
		rows=citasDao.borrarCitaPst(citaPaciente);
	return rows;
	}
}
