package vista;

import java.security.PublicKey;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import controlador.CitasController;
import dao.DbConnection;
import excepciones.HistoriaClinicaException;
import modelo.CitaPaciente;

public class Main {
	static Connection cn;
	static DbConnection dbConn;

	public static void main(String[] args) {
		try {
			FrmCitas1 frm = new FrmCitas1();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
