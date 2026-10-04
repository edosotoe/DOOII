package vista;

import model.Entrega;
import model.Pedido;
import model.Repartidor;
import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import dao.impl.EntregaDAOImpl;
import dao.impl.PedidoDAOImpl;
import dao.impl.RepartidorDAOImpl;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;

public class VentanaListaEntregas extends JFrame {
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private final EntregaDAO entregaDAO = new EntregaDAOImpl();
    private final PedidoDAO pedidoDAO = new PedidoDAOImpl();
    private final RepartidorDAO repartidorDAO = new RepartidorDAOImpl();

    public VentanaListaEntregas() {
        setTitle("Listado de Entregas");
        setSize(550, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnas = {"ID", "ID Pedido", "Repartidor", "Fecha", "Hora"};
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
        btnEditar.addActionListener(e -> editarEntrega());
        btnEliminar.addActionListener(e -> eliminarEntrega());

        panelBotones.add(btnRefrescar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        cargarDatos();
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (Entrega en : entregaDAO.readAll()) {
            String nombreRepartidor = buscarNombreRepartidor(en.getIdRepartidor());

            modeloTabla.addRow(new Object[]{
                    en.getIdEntrega(),
                    en.getIdPedido(),
                    nombreRepartidor,
                    en.getFecha().format(formato),
                    en.getHora()
            });
        }
    }

    private String buscarNombreRepartidor(int idRepartidor) {
        for (Repartidor r : repartidorDAO.readAll()) {
            if (r.getId() == idRepartidor) {
                return r.getNombre();
            }
        }
        return "desconocido";
    }

    private int obtenerIdSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una entrega de la tabla.",
                    "Nada seleccionado", JOptionPane.WARNING_MESSAGE);
            return -1;
        }
        return (int) modeloTabla.getValueAt(fila, 0);
    }

    private void editarEntrega() {
        int id = obtenerIdSeleccionado();
        if (id == -1) {
            return;
        }

        Entrega entregaActual = null;
        for (Entrega en : entregaDAO.readAll()) {
            if (en.getIdEntrega() == id) {
                entregaActual = en;
                break;
            }
        }
        if (entregaActual == null) {
            JOptionPane.showMessageDialog(this, "No se encontró la entrega.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JComboBox<Pedido> comboPedido = new JComboBox<>(pedidoDAO.readAll().toArray(new Pedido[0]));
        JComboBox<Repartidor> comboRepartidor = new JComboBox<>(repartidorDAO.readAll().toArray(new Repartidor[0]));

        seleccionarPedidoPorId(comboPedido, entregaActual.getIdPedido());
        seleccionarRepartidorPorId(comboRepartidor, entregaActual.getIdRepartidor());

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Pedido:"));
        panel.add(comboPedido);
        panel.add(new JLabel("Repartidor:"));
        panel.add(comboRepartidor);

        int resultado = JOptionPane.showConfirmDialog(this, panel,
                "Editar Entrega #" + id, JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            Pedido pedidoSeleccionado = (Pedido) comboPedido.getSelectedItem();
            Repartidor repartidorSeleccionado = (Repartidor) comboRepartidor.getSelectedItem();

            if (pedidoSeleccionado == null || repartidorSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Debes seleccionar un pedido y un repartidor.",
                        "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // La fecha y hora originales se mantienen intactas; no son editables.
            Entrega entregaActualizada = new Entrega(
                    pedidoSeleccionado.getIdPedido(),
                    repartidorSeleccionado.getId(),
                    entregaActual.getFecha(),
                    entregaActual.getHora()
            );
            entregaActualizada.setIdEntrega(id);

            boolean actualizado = entregaDAO.update(entregaActualizada);
            if (actualizado) {
                JOptionPane.showMessageDialog(this, "Entrega actualizada correctamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar la entrega.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarEntrega() {
        int id = obtenerIdSeleccionado();
        if (id == -1) {
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres eliminar la entrega #" + id + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean eliminado = entregaDAO.delete(id);
            if (eliminado) {
                JOptionPane.showMessageDialog(this, "Entrega eliminada correctamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar la entrega.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void seleccionarPedidoPorId(JComboBox<Pedido> combo, int idPedido) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getIdPedido() == idPedido) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void seleccionarRepartidorPorId(JComboBox<Repartidor> combo, int idRepartidor) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getId() == idRepartidor) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }
}