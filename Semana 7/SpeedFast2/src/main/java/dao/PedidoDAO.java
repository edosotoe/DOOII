package dao;

import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public boolean guardar(Pedido pedido) {
        String sql = "INSERT INTO pedido (id, direccion, tipo, estado) VALUES (?, ?, ?, ?)";

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, pedido.getIdPedido());
            stmt.setString(2, pedido.getDireccionEntrega());
            stmt.setString(3, pedido.getTipoPedido());
            stmt.setString(4, pedido.getEstado());
            stmt.executeUpdate();
            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Ya existe un pedido con ese ID: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.out.println("Error al guardar el pedido: " + e.getMessage());
            return false;
        }
    }

    public List<Pedido> listarTodos() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT id, direccion, tipo, estado FROM pedido";

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String direccion = rs.getString("direccion");
                String tipo = rs.getString("tipo");
                String estado = rs.getString("estado");

                Pedido pedido = switch (tipo) {
                    case "Comida" -> new PedidoComida(id, direccion, 0);
                    case "Encomienda" -> new PedidoEncomienda(id, direccion, 0);
                    case "Express" -> new PedidoExpress(id, direccion, 0);
                    default -> null;
                };

                if (pedido != null) {
                    pedido.setEstado(estado);
                    pedidos.add(pedido);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar los pedidos: " + e.getMessage());
        }
        return pedidos;
    }

    public Pedido buscarPorId(int id) {
        String sql = "SELECT id, direccion, tipo, estado FROM pedido WHERE id = ?";

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String direccion = rs.getString("direccion");
                    String tipo = rs.getString("tipo");
                    String estado = rs.getString("estado");

                    Pedido pedido = switch (tipo) {
                        case "Comida" -> new PedidoComida(id, direccion, 0);
                        case "Encomienda" -> new PedidoEncomienda(id, direccion, 0);
                        case "Express" -> new PedidoExpress(id, direccion, 0);
                        default -> null;
                    };

                    if (pedido != null) {
                        pedido.setEstado(estado);
                    }
                    return pedido;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar el pedido: " + e.getMessage());
        }
        return null;
    }

    public void actualizarEstado(int idPedido, String nuevoEstado) {
        String sql = "UPDATE pedido SET estado = ? WHERE id = ?";
        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idPedido);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al actualizar el estado del pedido: " + e.getMessage());
        }
    }
}