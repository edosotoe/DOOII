package service;

import model.Pedido;
import java.util.ArrayList;
import java.util.List;

public class GestorPedidos {
    private final List<Pedido> pedidos = new ArrayList<>();

    public void agregarPedido(Pedido pedido) {
        pedidos.add(pedido);
    }

    public List<Pedido> obtenerPedidos() {
        return pedidos;
    }

    public Pedido buscarPorId(int id) {
        for (Pedido p : pedidos) {
            if (p.getIdPedido() == id) {
                return p;
            }
        }
        return null;
    }
}