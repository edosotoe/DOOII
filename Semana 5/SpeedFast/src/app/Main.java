package app;

import model.Pedido;
import model.ZonaDeCarga;
import service.Repartidor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
        

public class Main {
    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        
 
        System.out.println("[Zona de carga inicializada]");
        ZonaDeCarga zonaDeCarga = new ZonaDeCarga();

        zonaDeCarga.agregarPedido(new Pedido(1, "Santiago Centro"));
        zonaDeCarga.agregarPedido(new Pedido(2, "Providencia"));
        zonaDeCarga.agregarPedido(new Pedido(3, "Ñuñoa"));
        zonaDeCarga.agregarPedido(new Pedido(4, "Recoleta"));
        zonaDeCarga.agregarPedido(new Pedido(5, "Las Condes"));

        Repartidor r1 = new Repartidor("Camila Contreras", zonaDeCarga);
        Repartidor r2 = new Repartidor("Bruce Banner", zonaDeCarga);
        Repartidor r3 = new Repartidor("Sue Storm", zonaDeCarga);

        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(r1);
        executor.submit(r2);
        executor.submit(r3);

        executor.shutdown();
        try {
            boolean terminado = executor.awaitTermination(1, TimeUnit.MINUTES);
            if (!terminado) {
                System.out.println("Se excedió el tiempo máximo de espera...");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.out.println("La espera de finalización fue interrumpida.");
            Thread.currentThread().interrupt();
        }

        System.out.println("[Zona de carga vacía]");
        System.out.println("Todos los pedidos han sido entregados correctamente.");
    }
}