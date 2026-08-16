package model;

public class PedidoExpress extends Pedido {
    
    public PedidoExpress(int idPedido, String direccionEntrega) {
        super(idPedido, direccionEntrega, "Express");
    }
    
    @Override
    public void asignarRepartidor() {
        System.out.println("[Pedido Express]");
        System.out.println("Direccion de entrega: " + getDireccionEntrega());
        System.out.println("Asignando al repartidor mas cercano...");
        System.out.println("-> Repartidor más cercano con disponibilidad inmediata encontrado");
    }
    
    // SOBRECARGA
    public void asignarRepartidor(String nombreRepartidor)     {
        System.out.println("-> Pedido asignado a " + nombreRepartidor);
    }
}