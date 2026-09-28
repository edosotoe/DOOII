package service;

import interfaces.Rastreable;
import model.Pedido;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ControladorDeEnvios implements Rastreable {
    private final ArrayList<String> historial = new ArrayList<>();
    private final AtomicInteger totalEntregados = new AtomicInteger(0);

    public synchronized void despacharPedido(Pedido pedido, String nombreRepartidor) {
        pedido.asignarRepartidor(nombreRepartidor);
        pedido.despachar();
        historial.add(pedido.getTipoPedido() + " #" + pedido.getIdPedido()
                + " - entregado por: " + nombreRepartidor);
        totalEntregados.incrementAndGet();
    }

    public int getTotalEntregados() {
        return totalEntregados.get();
    }

    @Override
    public void verHistorial() {
        System.out.println("Historial: ");
        for (String entrega : historial) {
            System.out.println("- " + entrega);
        }
    }
}