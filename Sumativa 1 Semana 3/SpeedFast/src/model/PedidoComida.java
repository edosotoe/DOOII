package model;

public class PedidoComida extends Pedido {
    
    public PedidoComida(int idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, "Comida", distanciaKm);
    }
    
    @Override
    public void asignarRepartidor() {
        System.out.println("[Pedido Comida]");
        System.out.println("Direccion de entrega: " + getDireccionEntrega());
        System.out.println("Asignando repartidor con mochila térmica...");
        System.out.println("-> Verificando mochila térmica... OK");
    }
    
// SOBRECARGA
    @Override
    public void asignarRepartidor(String nombreRepartidor)     {
        System.out.println("-> Pedido asignado a " + nombreRepartidor);
    }
    
    @Override
    public int calcularTiempoEntrega() {
        return (int) (15 + 2 * getDistanciaKm());
    }
}
