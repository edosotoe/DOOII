package vista;

import controlador.ReporteControl;
import model.Estudiante;
import model.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class ReporteVista extends JFrame {

    private final JButton btnMasPrestados = new JButton("Libros más prestados");
    private final JButton btnEnPrestamo = new JButton("Libros en préstamo");
    private final JComboBox<Estudiante> cmbEstudiante = new JComboBox<>();
    private final JButton btnHistorial = new JButton("Ver historial");
    private final JLabel lblReporte = new JLabel("Elige un reporte.");
    private final JTable tblReporte = new JTable();

    private DefaultTableModel model;
    private final ReporteControl controlador;
    private final Usuario usuario;

    public ReporteVista(Usuario usuario) {
        this.usuario = usuario;
        this.controlador = new ReporteControl(usuario);

        setTitle("Biblioteca Escolar - Reportes");
        setSize(800, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        armarVentana();
        inicializarTabla();
        inicializarBotones();
        cargarEstudiantes();
        aplicarRestriccionesPorRol();
    }

    private void armarVentana() {
        JPanel reportesGenerales = new JPanel();
        reportesGenerales.add(btnMasPrestados);
        reportesGenerales.add(btnEnPrestamo);

        JPanel reporteHistorial = new JPanel();
        reporteHistorial.add(new JLabel("Historial de:"));
        reporteHistorial.add(cmbEstudiante);
        reporteHistorial.add(btnHistorial);

        JPanel superior = new JPanel(new GridLayout(2, 1, 5, 5));
        superior.add(reportesGenerales);
        superior.add(reporteHistorial);

        JPanel general = new JPanel(new BorderLayout(10, 10));
        general.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        general.add(superior, BorderLayout.NORTH);
        general.add(new JScrollPane(tblReporte), BorderLayout.CENTER);
        general.add(lblReporte, BorderLayout.SOUTH);
        setContentPane(general);
    }

    private void inicializarTabla() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // la tabla es solo de lectura
            }
        };
        tblReporte.setModel(model);
    }

    private void inicializarBotones() {
        btnMasPrestados.addActionListener(e -> mostrarLibrosMasPrestados());
        btnEnPrestamo.addActionListener(e -> mostrarLibrosEnPrestamo());
        btnHistorial.addActionListener(e -> mostrarHistorial());
    }

    private void cargarEstudiantes() {
        cmbEstudiante.removeAllItems();
        for (Estudiante estudiante : controlador.listarEstudiantes()) {
            cmbEstudiante.addItem(estudiante);
        }
    }

    private void aplicarRestriccionesPorRol() {
        if (!usuario.esBibliotecario()) {
            btnMasPrestados.setEnabled(false);
            btnEnPrestamo.setEnabled(false);
            btnHistorial.setEnabled(false);
            lblReporte.setText("Los reportes son solo para el bibliotecario.");
        }
    }


    private void mostrarLibrosMasPrestados() {
        model.setColumnIdentifiers(new String[]{"Título", "Autor", "Préstamos"});
        controlador.cargarLibrosMasPrestados(model);
        lblReporte.setText("Libros más prestados: " + model.getRowCount() + " libro(s) con préstamos.");
    }

    private void mostrarLibrosEnPrestamo() {
        model.setColumnIdentifiers(new String[]{"Estudiante", "Libro", "Prestado", "Vence", "Estado"});
        controlador.cargarLibrosEnPrestamo(model);
        lblReporte.setText("Libros actualmente en préstamo: " + model.getRowCount() + ".");
    }

    private void mostrarHistorial() {
        Estudiante estudiante = (Estudiante) cmbEstudiante.getSelectedItem();
        if (estudiante == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un estudiante.");
            return;
        }
        model.setColumnIdentifiers(new String[]{"Libro", "Prestado", "Vence", "Devuelto", "Estado"});
        controlador.cargarHistorial(model, estudiante.getId());
        lblReporte.setText("Historial de " + estudiante.getNombre() + ": " + model.getRowCount() + " préstamo(s).");
    }
}