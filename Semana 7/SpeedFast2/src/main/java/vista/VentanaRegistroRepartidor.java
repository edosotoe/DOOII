package vista;

import model.Repartidor;
import dao.RepartidorDAO;
import javax.swing.*;
import java.awt.*;

public class VentanaRegistroRepartidor extends JFrame {

    private JTextField campoNombre;

    public VentanaRegistroRepartidor() {
        setTitle("Registrar Repartidor");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelFormulario = new JPanel(new GridLayout(1, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        campoNombre = new JTextField();

        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(campoNombre);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarRepartidor());

        add(panelFormulario, BorderLayout.CENTER);
        add(btnGuardar, BorderLayout.SOUTH);
    }

    private void guardarRepartidor() {
        String nombre = campoNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        new RepartidorDAO().guardar(new Repartidor(0, nombre));

        JOptionPane.showMessageDialog(this, "Repartidor \"" + nombre + "\" registrado correctamente.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

        campoNombre.setText("");
    }
}