package vista;

import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;
import dao.PedidoDAO;
import dao.impl.PedidoDAOImpl;
import javax.swing.*;
import java.awt.*;

public class VentanaRegistroPedido extends JFrame {

    private JTextField campoDireccion;
    private JTextField campoDistancia;
    private JComboBox<String> comboTipo;

    public VentanaRegistroPedido() {

        setTitle("Registrar Pedido");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        campoDireccion = new JTextField();
        campoDistancia = new JTextField();
        comboTipo = new JComboBox<>(new String[]{"Comida", "Encomienda", "Express"});

        panelFormulario.add(new JLabel("Dirección:"));
        panelFormulario.add(campoDireccion);
        panelFormulario.add(new JLabel("Distancia (km):"));
        panelFormulario.add(campoDistancia);
        panelFormulario.add(new JLabel("Tipo:"));
        panelFormulario.add(comboTipo);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarPedido());

        add(panelFormulario, BorderLayout.CENTER);
        add(btnGuardar, BorderLayout.SOUTH);
    }

    private void guardarPedido() {
        String direccion = campoDireccion.getText().trim();
        String distanciaTexto = campoDistancia.getText().trim();

        if (direccion.isEmpty() || distanciaTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double distancia;
        try {
            distancia = Double.parseDouble(distanciaTexto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La distancia deben ser numérica.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String tipo = (String) comboTipo.getSelectedItem();
        Pedido nuevoPedido = switch (tipo) {
            case "Comida" -> new PedidoComida(0, direccion, distancia);
            case "Encomienda" -> new PedidoEncomienda(0, direccion, distancia);
            case "Express" -> new PedidoExpress(0, direccion, distancia);
            default -> null;
        };

        boolean guardado = new PedidoDAOImpl().create(nuevoPedido);

        if (guardado) {
            JOptionPane.showMessageDialog(this, "Pedido #" + nuevoPedido.getIdPedido() + " registrado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            campoDireccion.setText("");
            campoDistancia.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar el pedido",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}