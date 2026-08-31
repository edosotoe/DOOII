package model;

public abstract class Pedido implements Despachable, Cancelable {
    private int idPedido;
    private String direccionEntrega;
    private String tipoPedido;
    private double distanciaKm;
    
    public Pedido(int idPedido, String direccionEntrega, String tipoPedido, double distanciaKm) {
        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega;
        this.tipoPedido = tipoPedido;
        this.distanciaKm = distanciaKm;
    }
    
    
    public int getIdPedido() {
        return idPedido;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public String getTipoPedido() {
        return tipoPedido;
    }
    
    public double getDistanciaKm() {
        return distanciaKm;
    }

// Metodo genérico

    public void asignarRepartidor() {
        System.out.println("Asignando un repartidor para tu pedido");
    }

    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("Pedido asignado a " + nombreRepartidor);
    }
    
    public void mostrarResumen() {
        System.out.println("Direccion: " + direccionEntrega);
        System.out.println("Distancia: " + (int) distanciaKm + " km");
    }

// Metodo abstracto    
    public abstract int calcularTiempoEntrega();

// Implementacion de las interfaces
    @Override
    public void despachar() {
        System.out.println("Pedido despachado correctamente");
    }
    
    @Override
    public void cancelar() {
        System.out.println("[Cancelacion de Pedido]");
        System.out.println("Cancelando Pedido " + tipoPedido + " #" + idPedido + " ...");
        System.out.println("-> Pedido cancelado exitosamente");
    }
    
    
}
