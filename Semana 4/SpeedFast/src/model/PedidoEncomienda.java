package model;

public class PedidoEncomienda extends Pedido {
    
    public PedidoEncomienda(int idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, "Encomienda", distanciaKm);
    }
    
    @Override
    public void asignarRepartidor() {
        System.out.println("[Pedido Encomienda]");
        System.out.println("Direccion de entrega: " + getDireccionEntrega());
        System.out.println("Asignando repartidor para tu encomienda...");
        System.out.println("-> Validando peso y embalaje... OK");
    }
    

    @Override
    public void asignarRepartidor(String nombreRepartidor)     {
        System.out.println("-> Pedido asignado a " + nombreRepartidor);
    }
    
     @Override
    public int calcularTiempoEntrega() {
        return (int) Math.round(20 + 1.5 * getDistanciaKm());
    }
    
}