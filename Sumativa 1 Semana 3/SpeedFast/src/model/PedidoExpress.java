package model;

public class PedidoExpress extends Pedido {
    
    public PedidoExpress(int idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, "Express", distanciaKm);
    }
    
    @Override
    public void asignarRepartidor() {
        System.out.println("[Pedido Express]");
        System.out.println("Direccion de entrega: " + getDireccionEntrega());
        System.out.println("Asignando al repartidor mas cercano...");
        System.out.println("-> Repartidor más cercano con disponibilidad inmediata encontrado");
    }
    
    // SOBRECARGA
    @Override
    public void asignarRepartidor(String nombreRepartidor)     {
        System.out.println("-> Pedido asignado a " + nombreRepartidor);
    }
    
     @Override
    public int calcularTiempoEntrega() {
        int tiempo = 10;
        if (getDistanciaKm() > 5) {
            tiempo += 5;
        }
        return tiempo;
    }
}