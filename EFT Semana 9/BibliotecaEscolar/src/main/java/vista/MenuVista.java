package vista;

import controlador.LoginControl;
import model.Usuario;
import javax.swing.*;
import java.awt.*;

public class MenuVista extends JFrame {

    private final LoginControl loginControl;
    private final Usuario usuario;

    public MenuVista(LoginControl loginControl) {
        this.loginControl = loginControl;
        this.usuario = loginControl.getUsuarioActual();

        setTitle("Biblioteca Escolar - Menú principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 420);
        setLocationRelativeTo(null);
        setResizable(false);
        armarVentana();
    }

    private void armarVentana() {
        JLabel lblTitulo = new JLabel("Menú principal", SwingConstants.CENTER);
        JLabel lblSesion = new JLabel("Sesión: " + usuario.getDescripcion(), SwingConstants.CENTER);
        JPanel encabezado = new JPanel(new GridLayout(2, 1, 5, 5));
        encabezado.add(lblTitulo);
        encabezado.add(lblSesion);

        String textoLibros = usuario.esBibliotecario() ? "Gestión de libros" : "Consultar libros";
        JButton btnLibros = new JButton(textoLibros);
        JButton btnEstudiantes = new JButton("Gestión de estudiantes");
        JButton btnPrestamos = new JButton("Préstamos y devoluciones");
        JButton btnReportes = new JButton("Reportes");
        JButton btnCerrarSesion = new JButton("Cerrar sesión");

        btnEstudiantes.setEnabled(usuario.esBibliotecario());
        btnReportes.setEnabled(usuario.esBibliotecario());

        JPanel botones = new JPanel(new GridLayout(5, 1, 10, 10));
        botones.add(btnLibros);
        botones.add(btnEstudiantes);
        botones.add(btnPrestamos);
        botones.add(btnReportes);
        botones.add(btnCerrarSesion);

        JPanel contenido = new JPanel(new BorderLayout(10, 20));
        contenido.setBorder(BorderFactory.createEmptyBorder(20, 50, 25, 50));
        contenido.add(encabezado, BorderLayout.NORTH);
        contenido.add(botones, BorderLayout.CENTER);
        setContentPane(contenido);

        btnLibros.addActionListener(e -> new LibroVista(usuario).setVisible(true));
        btnEstudiantes.addActionListener(e -> new EstudianteVista(usuario).setVisible(true));
        btnPrestamos.addActionListener(e -> new PrestamoVista(usuario, loginControl.getEstudianteActual()).setVisible(true));
        btnReportes.addActionListener(e -> new ReporteVista(usuario).setVisible(true));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
    }

    private void pantallaPendiente(String nombre) {
        JOptionPane.showMessageDialog(this, "La pantalla de " + nombre + " se agrega en el siguiente paso.");
    }

    private void cerrarSesion() {
        loginControl.cerrarSesion();
        new LoginVista().setVisible(true);
        dispose();
    }
}