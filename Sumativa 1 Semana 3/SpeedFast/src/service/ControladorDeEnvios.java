package service;

import java.util.ArrayList;
import model.Pedido;
import model.Rastreable;

public class ControladorDeEnvios implements Rastreable {
    private final ArrayList<String> historial = new ArrayList<>();
    
    public void despacharPedido(Pedido pedido, String nombreRepartidor) {
        pedido.asignarRepartidor(nombreRepartidor);
        pedido.despachar();
        historial.add(pedido.getTipoPedido() + " #" + pedido.getIdPedido()
            + " - entregado por " + nombreRepartidor);
    }
    
    @Override
    public void verHistorial() {
        System.out.println("--- Historial de Entregas Realizadas ---");
        for (String entrega : historial) {
            System.out.println("- " + entrega);
        }
    }
    
}
