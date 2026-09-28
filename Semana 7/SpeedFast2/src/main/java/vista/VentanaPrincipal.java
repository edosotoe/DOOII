package vista;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import model.Entrega;
import model.Pedido;
import model.Repartidor;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("SpeedFast - Gestión de Entregas");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton btnRegistrarPedido = new JButton("Registrar Pedido");
        JButton btnListarPedidos = new JButton("Listar Pedidos");
        JButton btnRegistrarRepartidor = new JButton("Registrar Repartidor");
        JButton btnAsignar = new JButton("Asignar Repartidor / Iniciar Entrega");

        btnRegistrarPedido.addActionListener(e -> new VentanaRegistroPedido().setVisible(true));
        btnListarPedidos.addActionListener(e -> new VentanaListaPedidos().setVisible(true));
        btnRegistrarRepartidor.addActionListener(e -> new VentanaRegistroRepartidor().setVisible(true));
        btnAsignar.addActionListener(e -> asignarRepartidor());

        panelBotones.add(btnRegistrarPedido);
        panelBotones.add(btnListarPedidos);
        panelBotones.add(btnRegistrarRepartidor);
        panelBotones.add(btnAsignar);

        add(panelBotones, BorderLayout.CENTER);
    }

    private void asignarRepartidor() {
        String idTexto = JOptionPane.showInputDialog(this, "Ingrese el ID del pedido:");
        if (idTexto == null || idTexto.isBlank()) {
            return;
        }

        int idPedido;
        try {
            idPedido = Integer.parseInt(idTexto.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Pedido pedido = new PedidoDAO().buscarPorId(idPedido);
        if (pedido == null) {
            JOptionPane.showMessageDialog(this, "No existe un pedido con ese ID.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Repartidor> repartidores = new RepartidorDAO().listarTodos();
        if (repartidores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay repartidores registrados todavía.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Repartidor[] opciones = repartidores.toArray(new Repartidor[0]);
        Repartidor seleccionado = (Repartidor) JOptionPane.showInputDialog(
                this,
                "Seleccione un repartidor:",
                "Asignar Repartidor",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccionado == null) {
            return;
        }

        pedido.asignarRepartidor(seleccionado.getNombre());
        pedido.despachar();

        Entrega entrega = new Entrega(idPedido, seleccionado.getId(), LocalDate.now(), LocalTime.now());
        new EntregaDAO().guardar(entrega);

        new PedidoDAO().actualizarEstado(idPedido, "En reparto");

        JOptionPane.showMessageDialog(this,
                "Pedido #" + idPedido + " asignado a " + seleccionado.getNombre(),
                "Entrega registrada", JOptionPane.INFORMATION_MESSAGE);
    }
}