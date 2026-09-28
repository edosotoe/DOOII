package vista;

import model.Pedido;
import dao.PedidoDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaListaPedidos extends JFrame {
        private DefaultTableModel modeloTabla;

    public VentanaListaPedidos() {

        setTitle("Listado de Pedidos");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnas = {"ID", "Dirección", "Tipo", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modeloTabla);

        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(e -> cargarDatos());

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(btnRefrescar, BorderLayout.SOUTH);

        cargarDatos();
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        for (Pedido p : new PedidoDAO().listarTodos()) {
            modeloTabla.addRow(new Object[]{
                    p.getIdPedido(),
                    p.getDireccionEntrega(),
                    p.getTipoPedido(),
                    p.getDistanciaKm()
            });
        }
    }
}