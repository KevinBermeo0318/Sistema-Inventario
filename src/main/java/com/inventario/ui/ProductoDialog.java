package com.inventario.ui;

import com.inventario.dao.ProductoDAO;
import com.inventario.model.Producto;
import com.inventario.model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

        String[][] campos = {
            {"Codigo:", ""}, {"Nombre:", ""}, {"Descripcion:", ""}, {"Categoria:", ""}
        };

        txtCodigo = new JTextField(20);
        txtNombre = new JTextField(20);
        txtDescripcion = new JTextField(20);
        txtCategoria = new JTextField(20);

        JTextField[] textos = {txtCodigo, txtNombre, txtDescripcion, txtCategoria};

        for (int i = 0; i < campos.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.3;
            panel.add(new JLabel(campos[i][0]), gbc);
            gbc.gridx = 1; gbc.weightx = 0.7;
            panel.add(textos[i], gbc);
        }

        spnCantidad = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 1));
        spnStockMin = new JSpinner(new SpinnerNumberModel(5, 0, 9999, 1));
        spnPrecio = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 9999999.0, 0.01));
        ((JSpinner.DefaultEditor) spnPrecio.getEditor()).getTextField().setColumns(10);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3;
        panel.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        panel.add(spnCantidad, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.3;
        panel.add(new JLabel("Precio:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        panel.add(spnPrecio, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0.3;
        panel.add(new JLabel("Stock Minimo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        panel.add(spnStockMin, gbc);

        if (producto != null) {
            txtCodigo.setText(producto.getCodigo());
            txtNombre.setText(producto.getNombre());
            txtDescripcion.setText(producto.getDescripcion());
            txtCategoria.setText(producto.getCategoria());
            spnCantidad.setValue(producto.getCantidad());
            spnPrecio.setValue(producto.getPrecio());
            spnStockMin.setValue(producto.getStockMinimo());
        }

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(40, 140, 80));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(e -> guardar());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(panelBotones, gbc);

        add(panel);
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
            productoDAO.insertar(nuevo);
        } else {
            producto.setCodigo(codigo);
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setCategoria(categoria);
            producto.setCantidad(cantidad);
            producto.setPrecio(precio);
            producto.setStockMinimo(stockMin);
            productoDAO.actualizar(producto);
        }

        confirmado = true;
        dispose();
    }

    public boolean isConfirmado() {
        return confirmado;
    }
}
