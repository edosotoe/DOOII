package vista;

import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;
import dao.PedidoDAO;
import javax.swing.*;
import java.awt.*;

public class VentanaRegistroPedido extends JFrame {

    private JTextField campoId;
    private JTextField campoDireccion;
    private JTextField campoDistancia;
    private JComboBox<String> comboTipo;

    public VentanaRegistroPedido() {

        setTitle("Registrar Pedido");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        campoId = new JTextField();
        campoDireccion = new JTextField();
        campoDistancia = new JTextField();
        comboTipo = new JComboBox<>(new String[]{"Comida", "Encomienda", "Express"});

        panelFormulario.add(new JLabel("ID:"));
        panelFormulario.add(campoId);
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
        String idTexto = campoId.getText().trim();
        String direccion = campoDireccion.getText().trim();
        String distanciaTexto = campoDistancia.getText().trim();

        if (idTexto.isEmpty() || direccion.isEmpty() || distanciaTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id;
        double distancia;
        try {
            id = Integer.parseInt(idTexto);
            distancia = Double.parseDouble(distanciaTexto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID y Distancia deben ser numéricos.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String tipo = (String) comboTipo.getSelectedItem();
        Pedido nuevoPedido = switch (tipo) {
            case "Comida" -> new PedidoComida(id, direccion, distancia);
            case "Encomienda" -> new PedidoEncomienda(id, direccion, distancia);
            case "Express" -> new PedidoExpress(id, direccion, distancia);
            default -> null;
        };

        boolean guardado = new PedidoDAO().guardar(nuevoPedido);

        if (guardado) {
            JOptionPane.showMessageDialog(this, "Pedido #" + id + " registrado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            campoId.setText("");
            campoDireccion.setText("");
            campoDistancia.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Ya existe un pedido con el ID " + id + ". Usa un ID distinto.",
                    "ID duplicado", JOptionPane.ERROR_MESSAGE);
        }
    }
}