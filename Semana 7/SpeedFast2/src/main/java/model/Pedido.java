package model;

import interfaces.Despachable;
import interfaces.Cancelable;

public abstract class Pedido implements Despachable, Cancelable {
    private int idPedido;
    private String direccionEntrega;
    private String tipoPedido;
    private double distanciaKm;
    private String estado;

    public Pedido(int idPedido, String direccionEntrega, String tipoPedido, double distanciaKm) {
        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega;
        this.tipoPedido = tipoPedido;
        this.distanciaKm = distanciaKm;
        this.estado = "Pendiente";
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

    public String getEstado() {
        return estado;
    }

    public void setEstado (String estado) {
        this.estado = estado;
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
    public void despachar() {
        System.out.println("Pedido despachado correctamente");
    }

    @Override
    public void cancelar() {
        System.out.println("Cancelando Pedido " + tipoPedido + " #" + idPedido + " ...");
        System.out.println("-> Pedido cancelado exitosamente");
    }
}