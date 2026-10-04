package dao.impl;

import dao.ConexionDB;
import dao.RepartidorDAO;
import model.Repartidor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAOImpl implements RepartidorDAO {

    @Override
    public boolean create(Repartidor repartidor) {
        String sql = "INSERT INTO repartidor (nombre) VALUES (?)";
        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, repartidor.getNombre());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    repartidor.setId(generatedKeys.getInt(1));
                }
            }
            return true;

        } catch (SQLException e) {
            System.out.println("Error al crear el repartidor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Repartidor> readAll() {
        List<Repartidor> repartidores = new ArrayList<>();
        String sql = "SELECT id, nombre FROM repartidor";

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                repartidores.add(new Repartidor(rs.getInt("id"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar los repartidores: " + e.getMessage());
        }
        return repartidores;
    }

    @Override
    public boolean update(Repartidor repartidor) {
        String sql = "UPDATE repartidor SET nombre = ? WHERE id = ?";
        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, repartidor.getNombre());
            stmt.setInt(2, repartidor.getId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar el repartidor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM repartidor WHERE id = ?";
        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("No se puede eliminar: el repartidor tiene entregas asociadas.");
            return false;
        } catch (SQLException e) {
            System.out.println("Error al eliminar el repartidor: " + e.getMessage());
            return false;
        }
    }
}