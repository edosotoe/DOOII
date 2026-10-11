package vista;

import controlador.PrestamoControl;
import model.Estudiante;
import model.Libro;
import model.Resultado;
import model.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PrestamoVista extends JFrame {

    private final JComboBox<Estudiante> cmbEstudiante = new JComboBox<>();
    private final JComboBox<Libro> cmbLibro = new JComboBox<>();
    private final JButton btnPrestar = new JButton("Registrar préstamo");
    private final JButton btnDevolver = new JButton("Registrar devolución");
    private final JLabel lblEstado = new JLabel("Listo.");
    private final JTable tblPrestamos = new JTable();

    private DefaultTableModel model;
    private final PrestamoControl controlador;
    private final Usuario usuario;

    public PrestamoVista(Usuario usuario, Estudiante estudianteSesion) {
        this.usuario = usuario;
        this.controlador = new PrestamoControl(usuario, estudianteSesion);

        setTitle("Biblioteca Escolar - Préstamos y devoluciones");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        armarVentana();
        inicializarTabla();
        inicializarBotones();
        cargarEstudiantes();
        cargarLibros();
        aplicarRestriccionesPorRol();

        controlador.cargarTabla(model);
    }

    private void armarVentana() {
        JPanel formulario = new JPanel(new GridLayout(2, 2, 5, 5));
        formulario.add(new JLabel("Estudiante:"));
        formulario.add(cmbEstudiante);
        formulario.add(new JLabel("Libro:"));
        formulario.add(cmbLibro);

        JPanel botones = new JPanel();
        botones.add(btnPrestar);

        JPanel superior = new JPanel(new BorderLayout(5, 5));
        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botones, BorderLayout.SOUTH);

        JPanel botonDevolucion = new JPanel();
        botonDevolucion.add(new JLabel("Selecciona un préstamo de la tabla:"));
        botonDevolucion.add(btnDevolver);

        JPanel inferior = new JPanel(new BorderLayout(5, 5));
        inferior.add(botonDevolucion, BorderLayout.CENTER);
        inferior.add(lblEstado, BorderLayout.SOUTH);

        JPanel general = new JPanel(new BorderLayout(10, 10));
        general.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        general.add(superior, BorderLayout.NORTH);
        general.add(new JScrollPane(tblPrestamos), BorderLayout.CENTER);
        general.add(inferior, BorderLayout.SOUTH);
        setContentPane(general);
    }

    private void inicializarTabla() {
        String[] columnas = {"ID", "Estudiante", "Libro", "Prestado", "Vence", "Devuelto", "Estado"};
        model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPrestamos.setModel(model);
        tblPrestamos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tblPrestamos.getColumnModel().getColumn(0).setMinWidth(0);
        tblPrestamos.getColumnModel().getColumn(0).setMaxWidth(0);
    }

    private void inicializarBotones() {
        btnPrestar.addActionListener(e -> registrarPrestamo());
        btnDevolver.addActionListener(e -> registrarDevolucion());
    }

    private void cargarEstudiantes() {
        cmbEstudiante.removeAllItems();
        for (Estudiante estudiante : controlador.listarEstudiantes()) {
            cmbEstudiante.addItem(estudiante);
        }
    }


    private void cargarLibros() {
        int seleccionado = cmbLibro.getSelectedIndex();
        cmbLibro.removeAllItems();
        for (Libro libro : controlador.listarLibros()) {
            cmbLibro.addItem(libro);
        }
        if (seleccionado >= 0 && seleccionado < cmbLibro.getItemCount()) {
            cmbLibro.setSelectedIndex(seleccionado);
        }
    }

    private void aplicarRestriccionesPorRol() {
        if (usuario.esEstudiante()) {
            cmbEstudiante.setEnabled(false);
            setTitle(getTitle() + " (Mis préstamos)");
        }
    }

    private void registrarPrestamo() {
        Estudiante estudiante = (Estudiante) cmbEstudiante.getSelectedItem();
        Libro libro = (Libro) cmbLibro.getSelectedItem();
        if (estudiante == null || libro == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un estudiante y un libro.");
            return;
        }
        iniciarOperacion("Registrando préstamo en segundo plano...");

        Runnable tarea = new TareaPrestamo(estudiante.getId(), libro.getId());
        Thread hilo = new Thread(tarea, "hilo-prestamo");
        hilo.start();
    }

    private void registrarDevolucion() {
        int fila = tblPrestamos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un préstamo de la tabla.");
            return;
        }
        int idPrestamo = Integer.parseInt(model.getValueAt(fila, 0).toString());
        iniciarOperacion("Registrando devolución en segundo plano...");

        Runnable tarea = new TareaDevolucion(idPrestamo);
        Thread hilo = new Thread(tarea, "hilo-devolucion");
        hilo.start();
    }

    private void iniciarOperacion(String mensaje) {
        btnPrestar.setEnabled(false);
        btnDevolver.setEnabled(false);
        lblEstado.setText(mensaje);
    }

    private void finalizarOperacion(Resultado resultado) {
        btnPrestar.setEnabled(true);
        btnDevolver.setEnabled(true);
        lblEstado.setText("Listo.");

        controlador.cargarTabla(model);
        cargarLibros();

        if (resultado.isExito()) {
            JOptionPane.showMessageDialog(this, resultado.getMensaje());
        } else {
            JOptionPane.showMessageDialog(this, resultado.getMensaje(), "No se pudo completar",
                    JOptionPane.WARNING_MESSAGE);
        }
    }


    private class TareaPrestamo implements Runnable {
        private final int idEstudiante;
        private final int idLibro;

        TareaPrestamo(int idEstudiante, int idLibro) {
            this.idEstudiante = idEstudiante;
            this.idLibro = idLibro;
        }

        @Override
        public void run() {
            Resultado resultado = controlador.registrarPrestamo(idEstudiante, idLibro);

            SwingUtilities.invokeLater(new MostrarResultado(resultado));
        }
    }

    private class TareaDevolucion implements Runnable {
        private final int idPrestamo;

        TareaDevolucion(int idPrestamo) {
            this.idPrestamo = idPrestamo;
        }

        @Override
        public void run() {
            Resultado resultado = controlador.registrarDevolucion(idPrestamo);
            SwingUtilities.invokeLater(new MostrarResultado(resultado));
        }
    }

    private class MostrarResultado implements Runnable {
        private final Resultado resultado;

        MostrarResultado(Resultado resultado) {
            this.resultado = resultado;
        }

        @Override
        public void run() {
            finalizarOperacion(resultado);
        }
    }
}