package app;

import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;
import service.ControladorDeEnvios;
        

public class Main {
    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        
        Pedido pedido1 = new PedidoComida(1, "Avenida Wakanda 111", 4);
        Pedido pedido2 = new PedidoEncomienda(2, "Calle Baxter 222", 6);
        Pedido pedido3 = new PedidoExpress(3, "Pasaje Profesor Xavier 333", 7);
        
        ControladorDeEnvios controlador = new ControladorDeEnvios();

/*
    
// Incorporación de arreglo para las referencias polimorficas    
        Pedido[] pedidos = { pedido1, pedido2, pedido3 };
        String[] nombres = { "Reed Richards", "Bruce Banner", "Peter Parker" };
        
        
        for (int i = 0; i < pedidos.length; i++) {
            System.out.println("Pedido #" + String.format("%03d", pedidos[i].getIdPedido())
                    + " - " + pedidos[i].getTipoPedido());
            pedidos[i].mostrarResumen();
            System.out.println("Tiempo estimado de entrega: " + pedidos[i].calcularTiempoEntrega() + " minutos");
            pedidos[i].asignarRepartidor();
            pedidos[i].asignarRepartidor(nombres[i]);
            System.out.println();
        }
*/

        // --- Pedido 1: Asignación automática
        System.out.println("Pedido #" + pedido1.getIdPedido());
        pedido1.mostrarResumen();
        pedido1.asignarRepartidor();
        System.out.println("Tiempo estimado: " + pedido1.calcularTiempoEntrega() + " minutos");
        controlador.despacharPedido(pedido1, "Bruce Banner");
        System.out.println();

        // --- Pedido 2: Asignación manual
        System.out.println("Pedido #" + pedido2.getIdPedido());
        pedido2.mostrarResumen();
        System.out.println("Repartidor asignado: Sue Storm");
        System.out.println("Tiempo estimado: " + pedido2.calcularTiempoEntrega() + " minutos");
        controlador.despacharPedido(pedido2, "Sue Storm");
        System.out.println();

        // --- Pedido 3: Pedido Cancelado
        pedido3.cancelar();
        System.out.println();

        // --- Historial final
        controlador.verHistorial();       

    }
}