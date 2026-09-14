package service;

import model.EstadoPedido;
import model.Pedido;
import model.ZonaDeCarga;
import java.util.Random;

public class Repartidor implements Runnable {
    private String nombre;
    private ZonaDeCarga zonaDeCarga;

    public Repartidor(String nombre, ZonaDeCarga zonaDeCarga) {
        this.nombre = nombre;
        this.zonaDeCarga = zonaDeCarga;
    }

    @Override
    public void run() {
        Pedido pedido;
        while ((pedido = zonaDeCarga.retirarPedido()) != null) {
            try {
                System.out.println("[Repartidor - " + nombre + "] Retirando pedido #" + pedido.getId() + "...");
                pedido.setEstado(EstadoPedido.EN_REPARTO.name());
                System.out.println("[Repartidor - " + nombre + "] Estado: " + pedido.getEstado());

                System.out.println("[Repartidor - " + nombre + "] Entregando pedido #" + pedido.getId() + "...");
                int tiempoSimulado = 1000 + new Random().nextInt(2000);
                Thread.sleep(tiempoSimulado);

                pedido.setEstado(EstadoPedido.ENTREGADO.name());
                System.out.println("[Repartidor - " + nombre + "] Estado: " + pedido.getEstado());

            } catch (InterruptedException e) {
                System.out.println("[Repartidor - " + nombre + "] Entrega interrumpida.");
                Thread.currentThread().interrupt();
                return;
                
            }
        }
        System.out.println("[Repartidor - " + nombre + "] No quedan más pedidos. Finalizando.");
    }
}