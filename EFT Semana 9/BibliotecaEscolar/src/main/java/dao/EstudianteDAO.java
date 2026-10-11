package dao;

import model.Estudiante;

import java.sql.SQLException;
import java.util.List;

public interface EstudianteDAO {
    void insertar(Estudiante estudiante) throws SQLException;
    List<Estudiante> listar() throws SQLException;
    Estudiante buscarPorId(int id) throws SQLException;
    Estudiante buscarPorRut(String rut) throws SQLException;
    boolean actualizar(Estudiante estudiante) throws SQLException;
    boolean eliminar(int id) throws SQLException;
}