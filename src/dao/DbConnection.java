package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

	static String bd = "hospital";
	 static String parametros="?useSSL=false&serverTimezone=UTC";
	//static String parametros = "?useSSL=false";
	static String user = "root";
	static String password = "";
	static String url = "jdbc:mysql://localhost:3306/" + bd + parametros;
	// Esta variable va a guardar la conexion
	Connection conn = null;

	public DbConnection() throws SQLException {
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("Se ha establecido la conexion");
	
	}

	/**
	 * Permite retornar la instancia de la conexion
	 */

	public Connection getConnection() {
		return conn;
	}

	public void disconnect() throws SQLException {		

		if (conn != null) {				
				System.err.println("Closing database: [" + conn + "] ....");
				conn.close();
				System.err.println("DB disconect.");							
		}

	}

}
