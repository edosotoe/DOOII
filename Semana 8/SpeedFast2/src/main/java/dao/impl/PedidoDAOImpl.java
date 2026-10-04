package dao.impl;

import dao.ConexionDB;
import dao.PedidoDAO;
import model.EstadoPedido;
import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {

    @Override
    public boolean create(Pedido pedido) {
        String sql = "INSERT INTO pedido (direccion, tipo, estado) VALUES (?, ?, ?)";
        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, pedido.getDireccionEntrega());
            stmt.setString(2, pedido.getTipoPedido().name());
            stmt.setString(3, pedido.getEstado().name());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pedido.setIdPedido(generatedKeys.getInt(1));
                }
            }
            return true;

        } catch (SQLException e) {
            System.out.println("Error al crear el pedido: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Pedido> readAll() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT id, direccion, tipo, estado FROM pedido";

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pedido pedido = construirDesdeFila(rs);
                if (pedido != null) {
                    pedidos.add(pedido);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar los pedidos: " + e.getMessage());
        }
        return pedidos;
    }

    @Override
    public Pedido buscarPorId(int id) {
        String sql = "SELECT id, direccion, tipo, estado FROM pedido WHERE id = ?";
        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return construirDesdeFila(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar el pedido: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Pedido pedido) {
        String sql = "UPDATE pedido SET direccion = ?, tipo = ?, estado = ? WHERE id = ?";
        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, pedido.getDireccionEntrega());
            stmt.setString(2, pedido.getTipoPedido().name());
            stmt.setString(3, pedido.getEstado().name());
            stmt.setInt(4, pedido.getIdPedido());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar el pedido: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM pedido WHERE id = ?";
        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("No se puede eliminar: el pedido tiene entregas asociadas.");
            return false;
        } catch (SQLException e) {
            System.out.println("Error al eliminar el pedido: " + e.getMessage());
            return false;
        }
    }

    private Pedido construirDesdeFila(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String direccion = rs.getString("direccion");
        String tipo = rs.getString("tipo");
        String estado = rs.getString("estado");

        Pedido pedido = switch (tipo) {
            case "COMIDA" -> new PedidoComida(id, direccion, 0);
            case "ENCOMIENDA" -> new PedidoEncomienda(id, direccion, 0);
            case "EXPRESS" -> new PedidoExpress(id, direccion, 0);
            default -> null;
        };

        if (pedido != null) {
            pedido.setEstado(EstadoPedido.valueOf(estado));
        }
        return pedido;
    }
}