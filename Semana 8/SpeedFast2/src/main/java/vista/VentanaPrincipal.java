package vista;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import dao.impl.PedidoDAOImpl;
import dao.impl.RepartidorDAOImpl;
import dao.impl.EntregaDAOImpl;
import model.Entrega;
import model.EstadoPedido;
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

        JPanel panelBotones = new JPanel(new GridLayout(6, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton btnRegistrarPedido = new JButton("Registrar Pedido");
        JButton btnListarPedidos = new JButton("Listar Pedidos");
        JButton btnRegistrarRepartidor = new JButton("Registrar Repartidor");
        JButton btnListarRepartidores = new JButton("Listar Repartidores");
        JButton btnAsignar = new JButton("Asignar Repartidor / Iniciar Entrega");
        JButton btnListarEntregas = new JButton("Listar Entregas");

        btnRegistrarPedido.addActionListener(e -> new VentanaRegistroPedido().setVisible(true));
        btnListarPedidos.addActionListener(e -> new VentanaListaPedidos().setVisible(true));
        btnRegistrarRepartidor.addActionListener(e -> new VentanaRegistroRepartidor().setVisible(true));
        btnListarRepartidores.addActionListener(e -> new VentanaListaRepartidores().setVisible(true));
        btnAsignar.addActionListener(e -> asignarRepartidor());
        btnListarEntregas.addActionListener(e -> new VentanaListaEntregas().setVisible(true));

        panelBotones.add(btnRegistrarPedido);
        panelBotones.add(btnListarPedidos);
        panelBotones.add(btnRegistrarRepartidor);
        panelBotones.add(btnListarRepartidores);
        panelBotones.add(btnAsignar);
        panelBotones.add(btnListarEntregas);

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

        Pedido pedido = new PedidoDAOImpl().buscarPorId(idPedido);
        if (pedido == null) {
            JOptionPane.showMessageDialog(this, "No existe un pedido con ese ID.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Repartidor> repartidores = new RepartidorDAOImpl().readAll();
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
        new EntregaDAOImpl().create(entrega);

        pedido.setEstado(EstadoPedido.EN_REPARTO);
        new PedidoDAOImpl().update(pedido);

        JOptionPane.showMessageDialog(this,
                "Pedido #" + idPedido + " asignado a " + seleccionado.getNombre(),
                "Entrega registrada", JOptionPane.INFORMATION_MESSAGE);
    }
}