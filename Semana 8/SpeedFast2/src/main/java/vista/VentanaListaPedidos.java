package vista;

import model.Pedido;
import dao.PedidoDAO;
import model.EstadoPedido;
import dao.impl.PedidoDAOImpl;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaListaPedidos extends JFrame {
        private DefaultTableModel modeloTabla;
        private JTable tabla;
        private final PedidoDAO pedidoDAO = new PedidoDAOImpl();

    public VentanaListaPedidos() {

        setTitle("Listado de Pedidos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnas = {"ID", "Dirección", "Tipo", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);

        JPanel panelBotones = new JPanel(new GridLayout(1,3,10,10));
        JButton btnRefrescar = new JButton("Refrescar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        btnRefrescar.addActionListener(e -> cargarDatos());
        btnEditar.addActionListener(e -> editarPedido());
        btnEliminar.addActionListener(e -> eliminarPedido());

        panelBotones.add(btnRefrescar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        cargarDatos();
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        for (Pedido p : new PedidoDAOImpl().readAll()) {
            modeloTabla.addRow(new Object[]{
                    p.getIdPedido(),
                    p.getDireccionEntrega(),
                    p.getTipoPedido(),
                    p.getEstado()
            });
        }
    }

    private int obtenerIdSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un pedido de la tabla.",
                    "Nada seleccionado", JOptionPane.WARNING_MESSAGE);
            return -1;
        }
        return (int) modeloTabla.getValueAt(fila, 0);
    }

    private void editarPedido() {
        int id = obtenerIdSeleccionado();
        if (id == -1) {
            return;
        }

        Pedido pedido = pedidoDAO.buscarPorId(id);
        if (pedido == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el pedido.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField campoDireccion = new JTextField(pedido.getDireccionEntrega());
        JComboBox<EstadoPedido> comboEstado = new JComboBox<>(EstadoPedido.values());
        comboEstado.setSelectedItem(pedido.getEstado());

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Dirección:"));
        panel.add(campoDireccion);
        panel.add(new JLabel("Estado:"));
        panel.add(comboEstado);

        int resultado = JOptionPane.showConfirmDialog(this, panel,
                "Editar Pedido #" + id, JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            String nuevaDireccion = campoDireccion.getText().trim();
            if (nuevaDireccion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "La dirección no puede quedar vacía.",
                        "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            pedido.setDireccionEntrega(nuevaDireccion);
            pedido.setEstado((EstadoPedido) comboEstado.getSelectedItem());

            boolean actualizado = pedidoDAO.update(pedido);
            if (actualizado) {
                JOptionPane.showMessageDialog(this, "Pedido #" + id + " actualizado correctamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el pedido.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarPedido() {
        int id = obtenerIdSeleccionado();
        if (id == -1) {
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres eliminar el pedido #" + id + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean eliminado = pedidoDAO.delete(id);
            if (eliminado) {
                JOptionPane.showMessageDialog(this, "Pedido #" + id + " eliminado correctamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar el pedido #" + id + " (puede tener entregas asociadas).",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}