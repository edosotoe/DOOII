package model;

import interfaces.Despachable;
import interfaces.Cancelable;

public abstract class Pedido implements Despachable, Cancelable {
    private int idPedido;
    private String direccionEntrega;
    private final TipoPedido tipoPedido;
    private double distanciaKm;
    private EstadoPedido estado;

    public Pedido(int idPedido, String direccionEntrega, TipoPedido tipoPedido, double distanciaKm) {
        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega;
        this.tipoPedido = tipoPedido;
        this.distanciaKm = distanciaKm;
        this.estado = EstadoPedido.PENDIENTE;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public TipoPedido getTipoPedido() {
        return tipoPedido;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public void setEstado (EstadoPedido estado) {
        this.estado = estado;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

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

    public abstract int calcularTiempoEntrega();

    @Override
    public String toString() {
        return "#" + idPedido + " - " + direccionEntrega;
    }

    @Override
    public void despachar() {
        System.out.println("Pedido despachado correctamente");
    }

    @Override
    public void cancelar() {
        System.out.println("Cancelando Pedido " + tipoPedido + " #" + idPedido + " ...");
        System.out.println("-> Pedido cancelado exitosamente");
    }
}