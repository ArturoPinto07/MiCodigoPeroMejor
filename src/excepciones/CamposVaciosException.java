package excepciones;

public class CamposVaciosException extends Exception {

    public CamposVaciosException() {
        super("El campo no puede estar vacío");
    }

    public CamposVaciosException(String mensaje) {
        super(mensaje);
    }
}
