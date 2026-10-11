package dao;

import model.Prestamo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface PrestamoDAO {
    boolean registrarPrestamo(Prestamo prestamo) throws SQLException;

    boolean registrarDevolucion(int idPrestamo, LocalDate fechaEntrega) throws SQLException;

    List<Prestamo> listar() throws SQLException;
    List<Prestamo> listarActivos() throws SQLException;
    List<Prestamo> listarPorEstudiante(int idEstudiante) throws SQLException;
    Prestamo buscarPorId(int id) throws SQLException;

    List<Object[]> librosMasPrestados() throws SQLException;

    boolean eliminar(int id) throws SQLException;
}