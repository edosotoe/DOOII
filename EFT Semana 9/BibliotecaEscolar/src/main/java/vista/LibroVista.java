package vista;

import controlador.LibroControl;
import model.Categoria;
import model.Resultado;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LibroVista extends JFrame {

    private final JTextField txtTitulo = new JTextField();
    private final JTextField txtAutor = new JTextField();
    private final JTextField txtIsbn = new JTextField();
    private final JTextField txtEditorial = new JTextField();
    private final JSpinner spnStock = new JSpinner();
    private final JComboBox<Categoria> cmbCategoria = new JComboBox<>();
    private final JButton btnAgregar = new JButton("Agregar");
    private final JButton btnEditar = new JButton("Editar");
    private final JButton btnEliminar = new JButton("Eliminar");
    private final JButton btnLimpiar = new JButton("Limpiar");
    private final JTextField txtBuscar = new JTextField(20);
    private final JButton btnBuscar = new JButton("Buscar");
    private final JTable tblLibros = new JTable();

    private DefaultTableModel model;
    private final LibroControl controlador;
    private final Usuario usuario;
    private int libroSeleccionado = -1;

    public LibroVista(Usuario usuario) {
        this.usuario = usuario;
        this.controlador = new LibroControl(usuario);

        setTitle("Biblioteca Escolar - Libros");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        spnStock.setModel(new SpinnerNumberModel(1, 0, 10000, 1));

        armarVentana();
        inicializarTabla();
        inicializarBotones();
        cargarCategorias();
        aplicarRestriccionesPorRol();

        controlador.cargarTabla(model);
    }

    private void armarVentana() {
        JPanel formulario = new JPanel(new GridLayout(6, 2, 5, 5));
        formulario.add(new JLabel("Título:"));
        formulario.add(txtTitulo);
        formulario.add(new JLabel("Autor:"));
        formulario.add(txtAutor);
        formulario.add(new JLabel("ISBN:"));
        formulario.add(txtIsbn);
        formulario.add(new JLabel("Editorial:"));
        formulario.add(txtEditorial);
        formulario.add(new JLabel("Stock:"));
        formulario.add(spnStock);
        formulario.add(new JLabel("Categoría:"));
        formulario.add(cmbCategoria);

        JPanel botones = new JPanel();
        botones.add(btnAgregar);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);

        JPanel superior = new JPanel(new BorderLayout(5, 5));
        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botones, BorderLayout.SOUTH);

        JPanel busqueda = new JPanel();
        busqueda.add(new JLabel("Buscar por título, autor o ISBN:"));
        busqueda.add(txtBuscar);
        busqueda.add(btnBuscar);

        JPanel general = new JPanel(new BorderLayout(10, 10));
        general.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        general.add(superior, BorderLayout.NORTH);
        general.add(new JScrollPane(tblLibros), BorderLayout.CENTER);
        general.add(busqueda, BorderLayout.SOUTH);
        setContentPane(general);
    }

    private void inicializarTabla() {
        String[] columnas = {"ID", "Título", "Autor", "ISBN", "Editorial", "Stock", "Categoría"};
        model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblLibros.setModel(model);
        tblLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tblLibros.getColumnModel().getColumn(0).setMinWidth(0);
        tblLibros.getColumnModel().getColumn(0).setMaxWidth(0);

        tblLibros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tblLibros.getSelectedRow();
                if (fila >= 0) {
                    libroSeleccionado = fila;
                    txtTitulo.setText(model.getValueAt(fila, 1).toString());
                    txtAutor.setText(model.getValueAt(fila, 2).toString());
                    txtIsbn.setText(model.getValueAt(fila, 3).toString());
                    txtEditorial.setText(model.getValueAt(fila, 4).toString());
                    spnStock.setValue(model.getValueAt(fila, 5));

                    String categoriaBD = String.valueOf(model.getValueAt(fila, 6));
                    for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
                        if (cmbCategoria.getItemAt(i).getNombre().equals(categoriaBD)) {
                            cmbCategoria.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void inicializarBotones() {
        btnAgregar.addActionListener(e -> agregarLibro());
        btnEditar.addActionListener(e -> editarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscar.addActionListener(e -> buscarLibros());
    }

    private void cargarCategorias() {
        cmbCategoria.removeAllItems();
        for (Categoria categoria : controlador.listarCategorias()) {
            cmbCategoria.addItem(categoria);
        }
        if (cmbCategoria.getItemCount() > 0) {
            cmbCategoria.setSelectedIndex(0);
        }
    }

    private void aplicarRestriccionesPorRol() {
        if (!usuario.esBibliotecario()) {
            btnAgregar.setEnabled(false);
            btnEditar.setEnabled(false);
            btnEliminar.setEnabled(false);
            setTitle(getTitle() + " (Solo consulta)");
        }
    }

    private void agregarLibro() {
        Categoria categoria = (Categoria) cmbCategoria.getSelectedItem();
        int stock = (int) spnStock.getValue();

        Resultado resultado = controlador.registrar(txtTitulo.getText(), txtAutor.getText(),
                txtIsbn.getText(), txtEditorial.getText(), stock, categoria);
        mostrarResultado(resultado);
    }

    private void editarLibro() {
        if (libroSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un libro para editar.");
            return;
        }
        int id = Integer.parseInt(model.getValueAt(libroSeleccionado, 0).toString()); // ID en la columna 0
        Categoria categoria = (Categoria) cmbCategoria.getSelectedItem();
        int stock = (int) spnStock.getValue();

        Resultado resultado = controlador.modificar(id, txtTitulo.getText(), txtAutor.getText(),
                txtIsbn.getText(), txtEditorial.getText(), stock, categoria);
        mostrarResultado(resultado);
    }

    private void eliminarLibro() {
        int fila = tblLibros.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un libro para eliminar.");
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que deseas eliminar este libro?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );
        if (confirmacion == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(model.getValueAt(fila, 0).toString());
            Resultado resultado = controlador.eliminar(id);
            mostrarResultado(resultado);
        }
    }

    private void buscarLibros() {
        controlador.cargarTabla(model, txtBuscar.getText());
        libroSeleccionado = -1;
    }

    private void mostrarResultado(Resultado resultado) {
        if (resultado.isExito()) {
            JOptionPane.showMessageDialog(this, resultado.getMensaje());
            controlador.cargarTabla(model);
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, resultado.getMensaje(), "Revisa los datos",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        txtTitulo.setText("");
        txtAutor.setText("");
        txtIsbn.setText("");
        txtEditorial.setText("");
        spnStock.setValue(1);
        if (cmbCategoria.getItemCount() > 0) {
            cmbCategoria.setSelectedIndex(0);
        }
        txtBuscar.setText("");
        libroSeleccionado = -1;
        tblLibros.clearSelection();
    }
}