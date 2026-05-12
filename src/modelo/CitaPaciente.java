package modelo;

import excepciones.CamposVaciosException;
import excepciones.HistoriaClinicaException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Iterator;
import java.util.Objects;



public class CitaPaciente  {

    private int idCita; // Clave principal en la base de datos
    private String historiaClinica;
    private String nombre;
    private String apellidos;
    private String numTelefono;
    private String especialidad;
    private LocalDate fechaCita;
    private int numCita;

    // Constructor vacío
    public CitaPaciente() {
    }

    public CitaPaciente(String historiaClinica, String nombre, String apellidos,String numTelefono, String especialidad, String fechaCita, int numCita)
    		throws HistoriaClinicaException, CamposVaciosException, DateTimeParseException {
		setHistoriaClinica(historiaClinica);
		setNombre(nombre);
		setApellidos(apellidos);
		setNumTelefono(numTelefono);
		setEspecialidad(especialidad);
		setFechaCita(fechaCita);
		setNumCita(numCita);
	}
    public CitaPaciente(int idCita, String historiaClinica, String nombre, String apellidos,String numTelefono, String especialidad, String fechaCita, int numCita)
    		throws HistoriaClinicaException, CamposVaciosException, DateTimeParseException{
    	setHistoriaClinica(historiaClinica);
		setNombre(nombre);
		setApellidos(apellidos);
		setNumTelefono(numTelefono);
		setEspecialidad(especialidad);
		setFechaCita(fechaCita);
		setNumCita(numCita);
		this.idCita=idCita;
    }

	public int getIdCita() {
		return idCita;
	}

	

	public String getHistoriaClinica() {
		return historiaClinica;
	}

	public void setHistoriaClinica(String historiaClinica) throws CamposVaciosException, HistoriaClinicaException {
		compruebaHistoriaClinica(historiaClinica);
		this.historiaClinica = historiaClinica;
	}

	private void compruebaHistoriaClinica(String historiaClinica) throws HistoriaClinicaException {
		historiaClinica=historiaClinica.trim();
		int acu=0;
		
		for (int i = 1; i < historiaClinica.length(); i++) {
			if (i%2==0) {
				acu+=Integer.parseInt(historiaClinica.substring(i-1,i))*3;
			}else {
				acu+=Integer.parseInt(historiaClinica.substring(i-1,i));
				}
		}
		
		acu=10-(acu%10);
		
		if (acu==10) {
			acu=0;
		}
		Long DC=Long.parseLong(historiaClinica)%10;
		
			if (!(DC==acu)) {
				throw new HistoriaClinicaException();
			}
		
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) throws CamposVaciosException {
		if (nombre.isEmpty()) {
			throw new CamposVaciosException();
		}
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) throws CamposVaciosException {
		if (apellidos.isEmpty()) {
			throw new CamposVaciosException();
		}
		this.apellidos = apellidos;
	}

	public String getNumTelefono() {
		return numTelefono;
	}

	public void setNumTelefono(String numTelefono) throws CamposVaciosException {
		
		this.numTelefono = numTelefono;
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(String especialidad) throws CamposVaciosException {
		if (especialidad.isEmpty()) {
			throw new CamposVaciosException();
		}
		this.especialidad = especialidad;
	}

	public LocalDate getFechaCita() {
		return fechaCita;
	}

	public void setFechaCita(String fechaCita) {
		this.fechaCita = LocalDate.parse(fechaCita);
	}

	public int getNumCita() {
		return numCita;
	}

	public void setNumCita(int numCita) {
		
		this.numCita = numCita;
	}

	@Override
	public String toString() {
		return "CitaPaciente [idCita=" + idCita + ", historiaClinica=" + historiaClinica + ", nombre=" + nombre
				+ ", apellidos=" + apellidos + ", numTelefono=" + numTelefono + ", especialidad=" + especialidad
				+ ", fechaCita=" + fechaCita + ", numCita=" + numCita + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(historiaClinica, numCita);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CitaPaciente other = (CitaPaciente) obj;
		return Objects.equals(historiaClinica, other.historiaClinica) && numCita == other.numCita;
	}
    

}