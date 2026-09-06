package service;

import model.Pedido;
import java.util.List;
import java.util.Random;

public class Repartidor implements Runnable {
    private String nombre; 
    private List<Pedido> pedidosAsignados;
    private ControladorDeEnvios controlador;
    
    public Repartidor(String nombre, List<Pedido> pedidosAsignados, ControladorDeEnvios controlador) {
        this.nombre = nombre;
        this.pedidosAsignados = pedidosAsignados;
        this.controlador = controlador;
    }

    public String getNombre() {
        return nombre;
    }
    
    @Override
    public void run() {
        for (Pedido pedido : pedidosAsignados) {
            try {
                System.out.println("[Repartidor: " + nombre + "] Entregando Pedido "
                + pedido.getTipoPedido() + " #" + pedido.getIdPedido() + "...");

                int tiempoSimulado = 1000 + new Random().nextInt(2000);
                Thread.sleep(tiempoSimulado);

                controlador.despacharPedido(pedido, nombre);
                System.out.println("[Repartidor: " + nombre + "] Pedido #" + pedido.getIdPedido() + " entregado.");
 
            } catch (InterruptedException e) {
                System.out.println("[Repartidor: " + nombre + "] Entrega interrumpida");
                Thread.currentThread().interrupt();
            }
        }
        
        System.out.println("[Repartidor: " + nombre + "] Finalizó todas sus entregas");
    }
    
}
