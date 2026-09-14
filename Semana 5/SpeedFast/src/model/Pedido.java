package model;

public class Pedido {
    private int id;
    private String direccionEntrega;
    private EstadoPedido estado;

    public Pedido(int id, String direccionEntrega) {
        this.id = id;
        this.direccionEntrega = direccionEntrega;
        this.estado = EstadoPedido.PENDIENTE;
    }

    public int getId() {
        return id;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    
    public void setEstado(String nuevoEstado) {
        this.estado = EstadoPedido.valueOf(nuevoEstado);
    }

    
    @Override
    public String toString() {
        return "Pedido #" + id + " (" + direccionEntrega + ") - Estado: " + estado;
    }
}