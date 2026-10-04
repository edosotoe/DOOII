package dao.impl;

import dao.ConexionDB;
import dao.EntregaDAO;
import model.Entrega;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAOImpl implements EntregaDAO {

    @Override
    public boolean create(Entrega entrega) {
        String sql = "INSERT INTO entrega (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";
        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, entrega.getIdPedido());
            stmt.setInt(2, entrega.getIdRepartidor());
            stmt.setDate(3, Date.valueOf(entrega.getFecha()));
            stmt.setTime(4, Time.valueOf(entrega.getHora()));
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entrega.setIdEntrega(generatedKeys.getInt(1));
                }
            }
            return true;

        } catch (SQLException e) {
            System.out.println("Error al crear la entrega: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Entrega> readAll() {
        List<Entrega> entregas = new ArrayList<>();
        String sql = "SELECT id, id_pedido, id_repartidor, fecha, hora FROM entrega";

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Entrega entrega = new Entrega(
                        rs.getInt("id_pedido"),
                        rs.getInt("id_repartidor"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getTime("hora").toLocalTime()
                );
                entrega.setIdEntrega(rs.getInt("id"));
                entregas.add(entrega);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar las entregas: " + e.getMessage());
        }
        return entregas;
    }

    @Override
    public boolean update(Entrega entrega) {
        String sql = "UPDATE entrega SET id_pedido = ?, id_repartidor = ?, fecha = ?, hora = ? WHERE id = ?";
        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, entrega.getIdPedido());
            stmt.setInt(2, entrega.getIdRepartidor());
            stmt.setDate(3, Date.valueOf(entrega.getFecha()));
            stmt.setTime(4, Time.valueOf(entrega.getHora()));
            stmt.setInt(5, entrega.getIdEntrega());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar la entrega: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM entrega WHERE id = ?";
        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar la entrega: " + e.getMessage());
            return false;
        }
    }
}