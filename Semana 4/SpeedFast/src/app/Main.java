package app;

import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;
import service.ControladorDeEnvios;
import service.Repartidor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
        

public class Main {
    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        
 
        ControladorDeEnvios controlador = new ControladorDeEnvios();
        
        List<Pedido> pedidosCamila = new ArrayList<>();
        pedidosCamila.add(new PedidoComida(101, "Avenida Wakanda 111", 4));
        pedidosCamila.add(new PedidoEncomienda(102,"Av. Nueva York 111", 6));
        
        List<Pedido> pedidosBruce = new ArrayList<>();
        pedidosBruce.add(new PedidoExpress(103, "Av. Asgard 222", 7));
        pedidosBruce.add(new PedidoComida(104,"Calle Baxter 222", 3));
        
        List<Pedido> pedidosSue = new ArrayList<>();
        pedidosSue.add(new PedidoEncomienda(105, "Pasaje Xavier 333", 5));
        pedidosSue.add(new PedidoExpress(106, "Callen Ingram 333", 8));
        
        Repartidor r1 = new Repartidor("Camila", pedidosCamila, controlador);
        Repartidor r2 = new Repartidor("Bruce", pedidosBruce, controlador);
        Repartidor r3 = new Repartidor("Sue", pedidosSue, controlador);
        
        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(r1);
        executor.submit(r2);
        executor.submit(r3);
        
        executor.shutdown();
        
        try {
            boolean terminado = executor.awaitTermination(1, TimeUnit.MINUTES);
            if (!terminado) {
                System.out.println("Se excedió el tiempo máximo de espera");
            }            
        } catch (InterruptedException e) {
            System.out.println("El proceso fue interrumpido");
            Thread.currentThread().interrupt();
        }
        
        
        
      System.out.println();
      controlador.verHistorial();       
      System.out.println();
      System.out.println("Total de pedidos entregados: " + controlador.getTotalEntregados());
    
    }
}