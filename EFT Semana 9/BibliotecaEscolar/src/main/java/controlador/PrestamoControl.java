package controlador;

import dao.EstudianteDAO;
import dao.LibroDAO;
import dao.PrestamoDAO;
import dao.impl.EstudianteDAOImpl;
import dao.impl.LibroDAOImpl;
import dao.impl.PrestamoDAOImpl;
import model.Estudiante;
import model.Libro;
import model.Prestamo;
import model.Resultado;
import model.Usuario;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class PrestamoControl {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final PrestamoDAO prestamoDAO = new PrestamoDAOImpl();
    private final EstudianteDAO estudianteDAO = new EstudianteDAOImpl();
    private final LibroDAO libroDAO = new LibroDAOImpl();
    private final Usuario usuario;
    private final Estudiante estudianteSesion;

    public PrestamoControl(Usuario usuario, Estudiante estudianteSesion) {
        this.usuario = usuario;
        this.estudianteSesion = estudianteSesion;
    }


    public Resultado registrarPrestamo(int idEstudiante, int idLibro) {
        if (idEstudiante <= 0 || idLibro <= 0) {
            return Resultado.error("Selecciona un estudiante y un libro.");
        }
        if (usuario.esEstudiante() && idEstudiante != estudianteSesion.getId()) {
            return Resultado.error("Solo puedes registrar préstamos a tu nombre.");
        }

        Prestamo prestamo = new Prestamo(idEstudiante, idLibro);
        try {
            boolean prestado = prestamoDAO.registrarPrestamo(prestamo);
            if (!prestado) {
                return Resultado.error("No hay stock disponible de ese libro.");
            }
            return Resultado.ok("Préstamo registrado. Vence el "
                    + prestamo.getFechaVencimiento().format(FORMATO_FECHA) + ".");
        } catch (SQLException e) {
            return Resultado.error("Error al registrar el préstamo: " + e.getMessage());
        }
    }

    public Resultado registrarDevolucion(int idPrestamo) {
        if (idPrestamo <= 0) {
            return Resultado.error("Selecciona un préstamo de la tabla.");
        }
        try {
            Prestamo prestamo = prestamoDAO.buscarPorId(idPrestamo);
            if (prestamo == null) {
                return Resultado.error("El préstamo no existe.");
            }
            if (usuario.esEstudiante() && prestamo.getIdEstudiante() != estudianteSesion.getId()) {
                return Resultado.error("Solo puedes devolver tus propios préstamos.");
            }
            if (prestamo.isDevuelto()) {
                return Resultado.error("Ese préstamo ya fue devuelto.");
            }

            int atraso = prestamo.diasDeAtraso();
            boolean devuelto = prestamoDAO.registrarDevolucion(idPrestamo, LocalDate.now());
            if (!devuelto) {
                return Resultado.error("Ese préstamo ya fue devuelto.");
            }
            if (atraso > 0) {
                return Resultado.ok("Devolución registrada con " + atraso + " día(s) de atraso.");
            }
            return Resultado.ok("Devolución registrada dentro del plazo.");
        } catch (SQLException e) {
            return Resultado.error("Error al registrar la devolución: " + e.getMessage());
        }
    }


    public List<Prestamo> listarPrestamos() {
        try {
            if (usuario.esEstudiante()) {
                return prestamoDAO.listarPorEstudiante(estudianteSesion.getId());
            }
            return prestamoDAO.listar();
        } catch (SQLException e) {
            System.err.println("Error al listar préstamos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void cargarTabla(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        for (Prestamo prestamo : listarPrestamos()) {
            String fechaEntrega = "";
            if (prestamo.isDevuelto()) {
                fechaEntrega = prestamo.getFechaDevolucion().format(FORMATO_FECHA);
            }
            modelo.addRow(new Object[]{
                    prestamo.getId(),
                    prestamo.getNombreEstudiante(),
                    prestamo.getTituloLibro(),
                    prestamo.getFechaPrestamo().format(FORMATO_FECHA),
                    prestamo.getFechaVencimiento().format(FORMATO_FECHA),
                    fechaEntrega,
                    prestamo.getEstado()
            });
        }
    }

    public List<Estudiante> listarEstudiantes() {
        List<Estudiante> lista = new ArrayList<>();
        if (usuario.esEstudiante()) {
            lista.add(estudianteSesion);
            return lista;
        }
        try {
            return estudianteDAO.listar();
        } catch (SQLException e) {
            System.err.println("Error al listar estudiantes: " + e.getMessage());
            return lista;
        }
    }

    public List<Libro> listarLibros() {
        try {
            return libroDAO.listar();
        } catch (SQLException e) {
            System.err.println("Error al listar libros: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Prestamo> listarPendientes() {
        List<Prestamo> pendientes = new ArrayList<>();
        for (Prestamo prestamo : listarPrestamos()) {
            if (!prestamo.isDevuelto()) {
                pendientes.add(prestamo);
            }
        }
        return pendientes;
    }
}