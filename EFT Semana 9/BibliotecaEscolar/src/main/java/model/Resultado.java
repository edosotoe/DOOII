package model;

public class Resultado {
    private final boolean exito;
    private final String mensaje;

    private Resultado(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }

    public static Resultado ok(String mensaje) {
        return new Resultado(true, mensaje);
    }

    public static Resultado error(String mensaje) {
        return new Resultado(false, mensaje);
    }

    public boolean isExito() { return exito; }
    public String getMensaje() { return mensaje; }
}