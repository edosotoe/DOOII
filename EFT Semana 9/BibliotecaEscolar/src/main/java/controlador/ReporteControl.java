package controlador;

import dao.EstudianteDAO;
import dao.PrestamoDAO;
import dao.impl.EstudianteDAOImpl;
import dao.impl.PrestamoDAOImpl;
import model.Estudiante;
import model.Prestamo;
import model.Usuario;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class ReporteControl {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final PrestamoDAO prestamoDAO = new PrestamoDAOImpl();
    private final EstudianteDAO estudianteDAO = new EstudianteDAOImpl();
    private final Usuario usuario;

    public ReporteControl(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Object[]> librosMasPrestados() {
        if (!usuario.esBibliotecario()) {
            return new ArrayList<>();
        }
        try {
            return prestamoDAO.librosMasPrestados();
        } catch (SQLException e) {
            System.err.println("Error en el reporte de libros más prestados: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Prestamo> historialPorEstudiante(int idEstudiante) {
        if (!usuario.esBibliotecario()) {
            return new ArrayList<>();
        }
        try {
            return prestamoDAO.listarPorEstudiante(idEstudiante);
        } catch (SQLException e) {
            System.err.println("Error en el historial del estudiante: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Prestamo> librosEnPrestamo() {
        if (!usuario.esBibliotecario()) {
            return new ArrayList<>();
        }
        try {
            return prestamoDAO.listarActivos();
        } catch (SQLException e) {
            System.err.println("Error en el reporte de libros en préstamo: " + e.getMessage());
            return new ArrayList<>();
        }
    }


    public List<Estudiante> listarEstudiantes() {
        if (!usuario.esBibliotecario()) {
            return new ArrayList<>();
        }
        try {
            return estudianteDAO.listar();
        } catch (SQLException e) {
            System.err.println("Error al listar estudiantes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void cargarLibrosMasPrestados(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        for (Object[] fila : librosMasPrestados()) {
            modelo.addRow(fila);
        }
    }

    public void cargarLibrosEnPrestamo(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        for (Prestamo prestamo : librosEnPrestamo()) {
            modelo.addRow(new Object[]{
                    prestamo.getNombreEstudiante(),
                    prestamo.getTituloLibro(),
                    prestamo.getFechaPrestamo().format(FORMATO_FECHA),
                    prestamo.getFechaVencimiento().format(FORMATO_FECHA),
                    prestamo.getEstado()
            });
        }
    }


    public void cargarHistorial(DefaultTableModel modelo, int idEstudiante) {
        modelo.setRowCount(0);
        for (Prestamo prestamo : historialPorEstudiante(idEstudiante)) {
            String fechaEntrega = "";
            if (prestamo.isDevuelto()) {
                fechaEntrega = prestamo.getFechaDevolucion().format(FORMATO_FECHA);
            }
            modelo.addRow(new Object[]{
                    prestamo.getTituloLibro(),
                    prestamo.getFechaPrestamo().format(FORMATO_FECHA),
                    prestamo.getFechaVencimiento().format(FORMATO_FECHA),
                    fechaEntrega,
                    prestamo.getEstado()
            });
        }
    }
}