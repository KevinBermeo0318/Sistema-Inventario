package com.inventario.ui;

import com.inventario.dao.ProductoDAO;
import com.inventario.model.Producto;
import com.inventario.model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ProductoDialog extends JDialog {

    private boolean confirmado = false;
    private final Producto producto;
    private final Usuario usuarioActual;
    private final ProductoDAO productoDAO = new ProductoDAO();

    private JTextField txtCodigo, txtNombre, txtDescripcion, txtCategoria;
    private JSpinner spnCantidad, spnStockMin;
    private JSpinner spnPrecio;

    public ProductoDialog(Frame parent, Producto producto, Usuario usuario) {
        super(parent, producto == null ? "Agregar Producto" : "Editar Producto", true);
        this.producto = producto;
        this.usuarioActual = usuario;
        inicializarUI();
        pack();
        setLocationRelativeTo(parent);
    }

    private void inicializarUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 25, 15, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);
        Color labelColor = new Color(30, 40, 70);

        String[] etiquetas = {"Codigo:", "Nombre:", "Descripcion:", "Categoria:"};
        txtCodigo = new JTextField(20);
        txtNombre = new JTextField(20);
        txtDescripcion = new JTextField(20);
        txtCategoria = new JTextField(20);
        JTextField[] textos = {txtCodigo, txtNombre, txtDescripcion, txtCategoria};

        for (int i = 0; i < etiquetas.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.3;
            JLabel lbl = new JLabel(etiquetas[i]);
            lbl.setFont(labelFont);
            lbl.setForeground(labelColor);
            panel.add(lbl, gbc);
            gbc.gridx = 1; gbc.weightx = 0.7;
            textos[i].setFont(new Font("Segoe UI", Font.PLAIN, 13));
            textos[i].setForeground(Color.BLACK);
            panel.add(textos[i], gbc);
        }

        spnCantidad = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 1));
        spnStockMin = new JSpinner(new SpinnerNumberModel(5, 0, 9999, 1));
        spnPrecio = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 9999999.0, 0.01));
        ((JSpinner.DefaultEditor) spnPrecio.getEditor()).getTextField().setColumns(10);

        String[] spinnerLabels = {"Cantidad:", "Precio:", "Stock Minimo:"};
        JSpinner[] spinners = {spnCantidad, spnPrecio, spnStockMin};
        for (int i = 0; i < spinners.length; i++) {
            gbc.gridx = 0; gbc.gridy = 4 + i; gbc.weightx = 0.3;
            JLabel lbl = new JLabel(spinnerLabels[i]);
            lbl.setFont(labelFont);
            lbl.setForeground(labelColor);
            panel.add(lbl, gbc);
            gbc.gridx = 1; gbc.weightx = 0.7;
            panel.add(spinners[i], gbc);
        }

        if (producto != null) {
            txtCodigo.setText(producto.getCodigo());
            txtNombre.setText(producto.getNombre());
            txtDescripcion.setText(producto.getDescripcion());
            txtCategoria.setText(producto.getCategoria());
            spnCantidad.setValue(producto.getCantidad());
            spnPrecio.setValue(producto.getPrecio());
            spnStockMin.setValue(producto.getStockMinimo());

            gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
            gbc.insets = new Insets(10, 5, 2, 5);
            panel.add(crearPanelFechas(producto), gbc);
            gbc.insets = new Insets(6, 5, 6, 5);
            gbc.gridwidth = 1;
        }

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGuardar.setBackground(new Color(40, 140, 80));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setOpaque(true);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(e -> guardar());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancelar.setBackground(new Color(100, 110, 130));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setOpaque(true);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        int filaBoton = producto != null ? 8 : 7;
        gbc.gridx = 0; gbc.gridy = filaBoton; gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 5, 5, 5);
        panel.add(panelBotones, gbc);

        add(panel);
    }

    private JPanel crearPanelFechas(Producto p) {
        JPanel panel = new JPanel(new GridLayout(2, 2, 8, 4));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 190, 220)),
            "Historial de fechas",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 11),
            new Color(80, 90, 130)
        ));
        panel.setBackground(new Color(245, 247, 255));

        Font fLabel = new Font("Segoe UI", Font.BOLD, 11);
        Font fValor = new Font("Segoe UI", Font.PLAIN, 11);
        Color cLabel = new Color(60, 70, 100);
        Color cValor = new Color(20, 30, 60);

        JLabel lblCreaLbl = new JLabel("Fecha de creacion:");
        lblCreaLbl.setFont(fLabel);
        lblCreaLbl.setForeground(cLabel);

        JLabel lblCreaVal = new JLabel(formatearFecha(p.getFechaCreacion()));
        lblCreaVal.setFont(fValor);
        lblCreaVal.setForeground(cValor);

        JLabel lblActLbl = new JLabel("Ultima actualizacion:");
        lblActLbl.setFont(fLabel);
        lblActLbl.setForeground(cLabel);

        JLabel lblActVal = new JLabel(formatearFecha(p.getFechaActualizacion()));
        lblActVal.setFont(fValor);
        lblActVal.setForeground(new Color(40, 120, 40));

        panel.add(lblCreaLbl);
        panel.add(lblCreaVal);
        panel.add(lblActLbl);
        panel.add(lblActVal);

        return panel;
    }

    private String formatearFecha(String fecha) {
        if (fecha == null || fecha.isBlank()) return "No disponible";
        try {
            String[] partes = fecha.split(" ");
            if (partes.length == 2) {
                String[] d = partes[0].split("-");
                return d[2] + "/" + d[1] + "/" + d[0] + "  " + partes[1].substring(0, 5);
            }
        } catch (Exception ignored) {}
        return fecha;
    }

    private void guardar() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String categoria = txtCategoria.getText().trim();
        int cantidad = (int) spnCantidad.getValue();
        double precio = (double) spnPrecio.getValue();
        int stockMin = (int) spnStockMin.getValue();

        if (codigo.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Codigo y nombre son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int excludeId = (producto != null) ? producto.getId() : 0;
        if (productoDAO.existeCodigo(codigo, excludeId)) {
            JOptionPane.showMessageDialog(this, "Ya existe un producto con ese codigo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (producto == null) {
            Producto nuevo = new Producto(0, codigo, nombre, descripcion, categoria, cantidad, precio, stockMin);
            if (productoDAO.insertar(nuevo)) {
                productoDAO.listarTodos().stream()
                    .filter(pr -> pr.getCodigo().equals(codigo))
                    .findFirst()
                    .ifPresent(pr ->
                        productoDAO.registrarMovimiento(pr.getId(), usuarioActual.getId(), "CREACION", cantidad,
                            "Producto creado por " + usuarioActual.getNombre())
                    );
            }
        } else {
            producto.setCodigo(codigo);
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setCategoria(categoria);
            producto.setCantidad(cantidad);
            producto.setPrecio(precio);
            producto.setStockMinimo(stockMin);
            if (productoDAO.actualizar(producto)) {
                productoDAO.registrarMovimiento(producto.getId(), usuarioActual.getId(), "MODIFICACION", 0,
                    "Producto modificado por " + usuarioActual.getNombre());
            }
        }

        confirmado = true;
        dispose();
    }

    public boolean isConfirmado() {
        return confirmado;
    }
}
