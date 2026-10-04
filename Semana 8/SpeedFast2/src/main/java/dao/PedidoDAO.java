package dao;

import model.Pedido;
import java.util.List;

public interface PedidoDAO {
    boolean create(Pedido pedido);
    List<Pedido> readAll();
    Pedido buscarPorId(int id);
    boolean update(Pedido pedido);
    boolean delete(int id);
}