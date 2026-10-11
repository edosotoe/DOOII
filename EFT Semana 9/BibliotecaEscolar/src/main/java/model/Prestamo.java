package model;

import java.time.LocalDate;

public class Prestamo {
    public static final int DIAS_PRESTAMO = 7;

    private int id;
    private int idEstudiante;
    private int idLibro;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private boolean devuelto;
    private String nombreEstudiante;
    private String tituloLibro;

    public Prestamo() { }

    public Prestamo(int idEstudiante, int idLibro) {
        this.idEstudiante = idEstudiante;
        this.idLibro = idLibro;
        this.fechaPrestamo = LocalDate.now();
        this.fechaDevolucion = fechaPrestamo.plusDays(DIAS_PRESTAMO);
        this.devuelto = false;
    }

    public LocalDate getFechaVencimiento() {
        return fechaPrestamo.plusDays(DIAS_PRESTAMO);
    }


    public int diasDeAtraso() {
        LocalDate referencia = devuelto ? fechaDevolucion : LocalDate.now();
        LocalDate fecha = getFechaVencimiento();
        int dias = 0;
        while (fecha.isBefore(referencia)) {
            fecha = fecha.plusDays(1);
            dias++;
        }
        return dias;
    }

    public boolean estaAtrasado() {
        return diasDeAtraso() > 0;
    }

    public String getEstado() {
        int dias = diasDeAtraso();
        if (devuelto) {
            return dias > 0 ? "Devuelto con atraso (" + dias + " días)" : "Devuelto";
        }
        return dias > 0 ? "ATRASADO (" + dias + " días)" : "Vigente";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante; }
    public int getIdLibro() { return idLibro; }
    public void setIdLibro(int idLibro) { this.idLibro = idLibro; }
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }
    public boolean isDevuelto() { return devuelto; }
    public void setDevuelto(boolean devuelto) { this.devuelto = devuelto; }
    public String getNombreEstudiante() { return nombreEstudiante; }
    public void setNombreEstudiante(String nombreEstudiante) { this.nombreEstudiante = nombreEstudiante; }
    public String getTituloLibro() { return tituloLibro; }
    public void setTituloLibro(String tituloLibro) { this.tituloLibro = tituloLibro; }
}