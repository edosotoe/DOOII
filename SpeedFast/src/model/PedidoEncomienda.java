package model;

public class PedidoEncomienda extends Pedido {
    
    public PedidoEncomienda(int idPedido, String direccionEntrega) {
        super(idPedido, direccionEntrega, "Encomienda");
    }
    
    @Override
    public void asignarRepartidor() {
        System.out.println("[Pedido Encomienda]");
        System.out.println("Direccion de entrega: " + getDireccionEntrega());
        System.out.println("Asignando repartidor para tu encomienda...");
        System.out.println("-> Validando peso y embalaje... OK");
    }
    
    // SOBRECARGA
    public void asignarRepartidor(String nombreRepartidor)     {
        System.out.println("-> Pedido asignado a " + nombreRepartidor);
    }
}