package excepciones;

public class HistoriaClinicaException extends Exception {

    public HistoriaClinicaException() {
        super("Codigo de historia clinica invalido: debe tener 13 digitos numericos");
    }

    public HistoriaClinicaException(String mensaje) {
        super(mensaje);
    }
}


