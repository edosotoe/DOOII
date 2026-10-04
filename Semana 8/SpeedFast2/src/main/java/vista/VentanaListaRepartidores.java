package vista;

import model.Repartidor;
import dao.RepartidorDAO;
import dao.impl.RepartidorDAOImpl;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaListaRepartidores extends JFrame {
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private final RepartidorDAO repartidorDAO = new RepartidorDAOImpl();

    public VentanaListaRepartidores() {
        setTitle("Listado de Repartidores");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnas = {"ID", "Nombre"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);

        JPanel panelBotones = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton btnRefrescar = new JButton("Refrescar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        btnRefrescar.addActionListener(e -> cargarDatos());
        btnEditar.addActionListener(e -> editarRepartidor());
        btnEliminar.addActionListener(e -> eliminarRepartidor());

        panelBotones.add(btnRefrescar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        cargarDatos();
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        for (Repartidor r : repartidorDAO.readAll()) {
            modeloTabla.addRow(new Object[]{r.getId(), r.getNombre()});
        }
    }

    private int obtenerIdSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un repartidor de la tabla.",
                    "Nada seleccionado", JOptionPane.WARNING_MESSAGE);
            return -1;
        }
        return (int) modeloTabla.getValueAt(fila, 0);
    }

    private void editarRepartidor() {
        int id = obtenerIdSeleccionado();
        if (id == -1) {
            return;
        }

        String nombreActual = (String) modeloTabla.getValueAt(tabla.getSelectedRow(), 1);
        String nuevoNombre = JOptionPane.showInputDialog(this,
                "Nombre del repartidor:", nombreActual);

        if (nuevoNombre == null) {
            return; // el usuario canceló
        }
        nuevoNombre = nuevoNombre.trim();
        if (nuevoNombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede quedar vacío.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Repartidor repartidor = new Repartidor(id, nuevoNombre);
        boolean actualizado = repartidorDAO.update(repartidor);

        if (actualizado) {
            JOptionPane.showMessageDialog(this, "Repartidor actualizado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarDatos();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el repartidor.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarRepartidor() {
        int id = obtenerIdSeleccionado();
        if (id == -1) {
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres eliminar al repartidor #" + id + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean eliminado = repartidorDAO.delete(id);
            if (eliminado) {
                JOptionPane.showMessageDialog(this, "Repartidor eliminado correctamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar el repartidor #" + id + " (puede tener entregas asociadas).",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}