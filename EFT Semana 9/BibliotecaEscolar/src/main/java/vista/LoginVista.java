package vista;

import controlador.LoginControl;
import model.Resultado;
import javax.swing.*;
import java.awt.*;

public class LoginVista extends JFrame {

    private final LoginControl loginControl = new LoginControl();

    private final JTextField txtCorreo = new JTextField();
    private final JPasswordField txtContrasena = new JPasswordField();
    private final JButton btnIngresar = new JButton("Ingresar");

    public LoginVista() {
        setTitle("Biblioteca Escolar - Ingreso");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(380, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        armarVentana();
    }

    private void armarVentana() {
        JLabel lblTitulo = new JLabel("Biblioteca Escolar", SwingConstants.CENTER);

        JPanel formulario = new JPanel(new GridLayout(4, 1, 5, 5));
        formulario.add(new JLabel("Correo:"));
        formulario.add(txtCorreo);
        formulario.add(new JLabel("Contraseña:"));
        formulario.add(txtContrasena);

        JPanel contenido = new JPanel(new BorderLayout(10, 15));
        contenido.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        contenido.add(lblTitulo, BorderLayout.NORTH);
        contenido.add(formulario, BorderLayout.CENTER);
        contenido.add(btnIngresar, BorderLayout.SOUTH);
        setContentPane(contenido);

        btnIngresar.addActionListener(e -> ingresar());
    }

    private void ingresar() {
        String correo = txtCorreo.getText();
        String contrasena = new String(txtContrasena.getPassword());

        Resultado resultado = loginControl.iniciarSesion(correo, contrasena);

        if (resultado.isExito()) {
            new MenuVista(loginControl).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, resultado.getMensaje(),
                    "No se pudo ingresar", JOptionPane.ERROR_MESSAGE);
            txtContrasena.setText("");
        }
    }
}