package vista;

import controlador.EstudianteControl;
import model.Resultado;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EstudianteVista extends JFrame {

    private final JTextField txtNombre = new JTextField();
    private final JTextField txtRut = new JTextField();
    private final JTextField txtCurso = new JTextField();
    private final JTextField txtCorreo = new JTextField();
    private final JButton btnAgregar = new JButton("Agregar");
    private final JButton btnEditar = new JButton("Editar");
    private final JButton btnEliminar = new JButton("Eliminar");
    private final JButton btnLimpiar = new JButton("Limpiar");
    private final JTable tblEstudiantes = new JTable();

    private DefaultTableModel model;
    private final EstudianteControl controlador;
    private final Usuario usuario;
    private int estudianteSeleccionado = -1;

    public EstudianteVista(Usuario usuario) {
        this.usuario = usuario;
        this.controlador = new EstudianteControl(usuario);

        setTitle("Biblioteca Escolar - Estudiantes");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        armarVentana();
        inicializarTabla();
        inicializarBotones();
        aplicarRestriccionesPorRol();

        controlador.cargarTabla(model);
    }

    private void armarVentana() {
        JPanel formulario = new JPanel(new GridLayout(4, 2, 5, 5));
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("RUT (ej: 12345678-9):"));
        formulario.add(txtRut);
        formulario.add(new JLabel("Curso:"));
        formulario.add(txtCurso);
        formulario.add(new JLabel("Correo:"));
        formulario.add(txtCorreo);

        JPanel botones = new JPanel();
        botones.add(btnAgregar);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);

        JPanel superior = new JPanel(new BorderLayout(5, 5));
        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botones, BorderLayout.SOUTH);

        JPanel general = new JPanel(new BorderLayout(10, 10));
        general.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        general.add(superior, BorderLayout.NORTH);
        general.add(new JScrollPane(tblEstudiantes), BorderLayout.CENTER);
        setContentPane(general);
    }

    private void inicializarTabla() {
        String[] columnas = {"ID", "Nombre", "RUT", "Curso", "Correo"};
        model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblEstudiantes.setModel(model);
        tblEstudiantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tblEstudiantes.getColumnModel().getColumn(0).setMinWidth(0);
        tblEstudiantes.getColumnModel().getColumn(0).setMaxWidth(0);

        tblEstudiantes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tblEstudiantes.getSelectedRow();
                if (fila >= 0) {
                    estudianteSeleccionado = fila;
                    txtNombre.setText(model.getValueAt(fila, 1).toString());
                    txtRut.setText(model.getValueAt(fila, 2).toString());
                    txtCurso.setText(model.getValueAt(fila, 3).toString());
                    txtCorreo.setText(model.getValueAt(fila, 4).toString());
                }
            }
        });
    }

    private void inicializarBotones() {
        btnAgregar.addActionListener(e -> agregarEstudiante());
        btnEditar.addActionListener(e -> editarEstudiante());
        btnEliminar.addActionListener(e -> eliminarEstudiante());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
    }

    private void aplicarRestriccionesPorRol() {
        if (!usuario.esBibliotecario()) {
            btnAgregar.setEnabled(false);
            btnEditar.setEnabled(false);
            btnEliminar.setEnabled(false);
            setTitle(getTitle() + " (Solo consulta)");
        }
    }

    private void agregarEstudiante() {
        Resultado resultado = controlador.registrar(txtNombre.getText(), txtRut.getText(),
                txtCurso.getText(), txtCorreo.getText());
        mostrarResultado(resultado);
    }

    private void editarEstudiante() {
        if (estudianteSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un estudiante para editar.");
            return;
        }
        int id = Integer.parseInt(model.getValueAt(estudianteSeleccionado, 0).toString());

        Resultado resultado = controlador.modificar(id, txtNombre.getText(), txtRut.getText(),
                txtCurso.getText(), txtCorreo.getText());
        mostrarResultado(resultado);
    }

    private void eliminarEstudiante() {
        int fila = tblEstudiantes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un estudiante para eliminar.");
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que deseas eliminar este estudiante?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );
        if (confirmacion == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(model.getValueAt(fila, 0).toString());
            Resultado resultado = controlador.eliminar(id);
            mostrarResultado(resultado);
        }
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
        txtNombre.setText("");
        txtRut.setText("");
        txtCurso.setText("");
        txtCorreo.setText("");
        estudianteSeleccionado = -1;
        tblEstudiantes.clearSelection();
    }
}