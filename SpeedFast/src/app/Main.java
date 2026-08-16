package app;

import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;
        

public class Main {
    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        
        PedidoComida pedido1 = new PedidoComida(1, "Avenida Wakanda 111");
        PedidoEncomienda pedido2 = new PedidoEncomienda(2, "Calle Baxter 222");
        PedidoExpress pedido3 = new PedidoExpress(3, "Pasaje Profesor Xavier 333");

        pedido1.asignarRepartidor();
        pedido1.asignarRepartidor("Reed Richards");
        System.out.println();

        pedido2.asignarRepartidor();
        pedido2.asignarRepartidor("Bruce Banner");
        System.out.println();

        pedido3.asignarRepartidor();
        pedido3.asignarRepartidor("Peter Parker");
    }
}