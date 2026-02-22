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
        tabla.getTableHeader().setBackground(new Color(210, 220, 240));
        tabla.getTableHeader().setForeground(new Color(20, 30, 60));
        tabla.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                if (!sel) {
                    String tipo = (String) t.getValueAt(row, 3);
                    switch (tipo) {
                        case "ENTRADA"      -> { c.setBackground(new Color(220, 255, 225)); c.setForeground(new Color(0, 100, 0)); }
                        case "SALIDA"       -> { c.setBackground(new Color(255, 225, 215)); c.setForeground(new Color(160, 50, 0)); }
                        case "CREACION"     -> { c.setBackground(new Color(215, 230, 255)); c.setForeground(new Color(20, 60, 160)); }
                        case "MODIFICACION" -> { c.setBackground(new Color(255, 245, 210)); c.setForeground(new Color(130, 90, 0)); }
                        case "ELIMINACION"  -> { c.setBackground(new Color(255, 215, 215)); c.setForeground(new Color(150, 0, 0)); }
                        default             -> { c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 255)); c.setForeground(new Color(20, 30, 60)); }
                    }
                } else {
                    c.setForeground(Color.WHITE);
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
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrar.setBackground(new Color(100, 110, 130));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setOpaque(true);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
