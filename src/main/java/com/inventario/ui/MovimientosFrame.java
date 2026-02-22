package com.inventario.ui;

import com.inventario.dao.ProductoDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MovimientosFrame extends JFrame {

    private final ProductoDAO productoDAO = new ProductoDAO();
    private DefaultTableModel modelo;

    private static final String[] COLUMNAS = {"Fecha", "Producto", "Usuario", "Tipo", "Cantidad", "Observacion"};

    public MovimientosFrame() {
        setTitle("Historial de Movimientos");
        setSize(850, 500);
        setLocationRelativeTo(null);
        inicializarUI();
        cargar();
    }

    private void inicializarUI() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Historial de Movimientos de Inventario");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(titulo, BorderLayout.NORTH);

        modelo = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable tabla = new JTable(modelo);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setRowHeight(26);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                if (!sel) {
                    String tipo = (String) t.getValueAt(row, 3);
                    if ("ENTRADA".equals(tipo)) c.setBackground(new Color(220, 255, 225));
                    else if ("SALIDA".equals(tipo)) c.setBackground(new Color(255, 225, 220));
                    else c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 255));
                }
                return c;
            }
        });

        tabla.getColumnModel().getColumn(0).setPreferredWidth(140);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(160);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(70);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(70);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(250);

        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBtn.add(btnCerrar);
        panel.add(panelBtn, BorderLayout.SOUTH);

        add(panel);
    }

    private void cargar() {
        modelo.setRowCount(0);
        List<String[]> movs = productoDAO.listarMovimientos();
        for (String[] m : movs) {
            modelo.addRow(m);
        }
    }
}
