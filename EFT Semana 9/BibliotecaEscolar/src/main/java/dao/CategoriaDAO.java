package dao;

import model.Categoria;

import java.sql.SQLException;
import java.util.List;

public interface CategoriaDAO {
    void insertar(Categoria categoria) throws SQLException;
    List<Categoria> listar() throws SQLException;
    Categoria buscarPorId(int id) throws SQLException;
    boolean actualizar(Categoria categoria) throws SQLException;
    boolean eliminar(int id) throws SQLException;
}