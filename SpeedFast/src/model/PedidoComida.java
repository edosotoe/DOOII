package model;

public class PedidoComida extends Pedido {
    
    public PedidoComida(int idPedido, String direccionEntrega) {
        super(idPedido, direccionEntrega, "Comida");
    }
    
    @Override
    public void asignarRepartidor() {
        System.out.println("[Pedido Comida]");
        System.out.println("Direccion de entrega: " + getDireccionEntrega());
        System.out.println("Asignando repartidor con mochila térmica...");
        System.out.println("-> Verificando mochila térmica... OK");
    }
    
// SOBRECARGA
public void asignarRepartidor(String nombreRepartidor)     {
    System.out.println("-> Pedido asignado a " + nombreRepartidor);
    }
}
