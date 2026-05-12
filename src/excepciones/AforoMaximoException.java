package excepciones;

public class AforoMaximoException extends Exception {

    public AforoMaximoException() {
        super("Aforo maximo alcanzado: esta especialidad no admite mas citas para ese dia");
    }

    public AforoMaximoException(String mensaje) {
        super(mensaje);
    }
}
