package dao;

import model.Usuario;

import java.sql.SQLException;
import java.util.List;

public interface UsuarioDAO {
    void insertar(Usuario usuario) throws SQLException;
    List<Usuario> listar() throws SQLException;
    Usuario buscarPorId(int id) throws SQLException;
    Usuario buscarPorCorreo(String correo) throws SQLException;
    Usuario buscarPorRut(String rut) throws SQLException;
    boolean actualizar(Usuario usuario) throws SQLException;
    boolean eliminar(int id) throws SQLException;
}