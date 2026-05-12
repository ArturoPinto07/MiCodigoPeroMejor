package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import excepciones.AforoMaximoException;
import excepciones.CamposVaciosException;
import excepciones.HistoriaClinicaException;
import modelo.CitaPaciente;

public class CitasDao {
	Connection cn;
	public CitasDao(Connection cn) {
		this.cn=cn;
	}
	public List<CitaPaciente> getCitasPst(String sql) throws SQLException, DateTimeParseException, HistoriaClinicaException, CamposVaciosException{
		List<CitaPaciente> citas=new ArrayList<CitaPaciente>();
		PreparedStatement pst=cn.prepareStatement(sql);
		ResultSet rs=pst.executeQuery();
		while (rs.next()) {
			CitaPaciente cita;
			cita=new CitaPaciente(rs.getInt("idCita"), rs.getString("historiaClinica"), rs.getString("nombre"), rs.getString("apellidos"), 
					rs.getString("numTelefono"), rs.getString("especialidad"), String.valueOf(rs.getDate("fechaCita")),rs.getInt("numCita"));
			citas.add(cita);
			cita=null;
		}
		return citas;
	}
	
	////AGREGAR CITA
	public int agregarCitasPst(CitaPaciente citaPaciente) throws SQLException {
		int rows=0;
			String sql="INSERT INTO CITAS VALUES (?,?,?,?,?,?,?,?)";
			PreparedStatement pst=cn.prepareStatement(sql);
			pst.setInt(1, citaPaciente.getIdCita());
			pst.setString(2, citaPaciente.getHistoriaClinica());
			pst.setString(3, citaPaciente.getNombre());
			pst.setString(4, citaPaciente.getApellidos());
			pst.setString(5,citaPaciente.getNumTelefono());
			pst.setString(6, citaPaciente.getEspecialidad());
			pst.setDate(7, Date.valueOf(citaPaciente.getFechaCita()));
			pst.setInt(8, citaPaciente.getNumCita());
			rows=pst.executeUpdate();
		return rows;
	}
	public int getDisponibilidadPst (String especialidad,String fecha) throws SQLException {
		int numCitas=0;
		String sql="SELECT COUNT(*) FROM CITAS WHERE FECHACITA=? AND ESPECIALIDAD LIKE ?";
			PreparedStatement pst=cn.prepareStatement(sql);
			pst.setDate(1, Date.valueOf(fecha));
			pst.setString(2, especialidad);
			ResultSet rs=pst.executeQuery();
			if (rs.next()) {
				numCitas=rs.getInt(1);
			}
		return numCitas;
	}
	public int modificarCitaPst(CitaPaciente citaPaciente) throws SQLException {
		int rows=0;
			String sql="UPDATE citas set fechaCita=? WHERE idCita=?";
			PreparedStatement pst=cn.prepareStatement(sql);
			pst.setDate(1, Date.valueOf(citaPaciente.getFechaCita()));
			pst.setInt(2, citaPaciente.getIdCita());
			rows=pst.executeUpdate();
		return rows;
	}
	public int borrarCitaPst(CitaPaciente citaPaciente) throws SQLException {
		int rows=0;
			String sql="DELETE FROM CITAS WHERE idCita=?";
			PreparedStatement pst=cn.prepareStatement(sql);
			pst.setInt(1, citaPaciente.getIdCita());
			rows=pst.executeUpdate();
		return rows;	
		}
}