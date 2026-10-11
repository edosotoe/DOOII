package dao;

import model.Libro;

import java.sql.SQLException;
import java.util.List;

public interface LibroDAO {
    void insertar(Libro libro) throws SQLException;
    List<Libro> listar() throws SQLException;
    List<Libro> buscar(String texto) throws SQLException;
    Libro buscarPorId(int id) throws SQLException;
    boolean actualizar(Libro libro) throws SQLException;
    boolean eliminar(int id) throws SQLException;
}