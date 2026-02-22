package com.inventario.ui;

import com.inventario.dao.UsuarioDAO;
import com.inventario.model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UsuarioDialog extends JDialog {

    private boolean confirmado = false;
    private final Usuario usuario;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private JTextField txtNombre, txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRol;

    public UsuarioDialog(Frame parent, Usuario usuario) {
        super(parent, usuario == null ? "Agregar Usuario" : "Editar Usuario", true);
        this.usuario = usuario;
        inicializarUI();
        pack();
        setLocationRelativeTo(parent);
    }

    private void inicializarUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 25, 15, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre = new JTextField(20);
        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);
        cmbRol = new JComboBox<>(new String[]{"ADMIN", "USUARIO"});

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.35;
        panel.add(new JLabel("Nombre completo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.65;
        panel.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nombre de usuario:"), gbc);
        gbc.gridx = 1;
        panel.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Contrasena:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Rol:"), gbc);
        gbc.gridx = 1;
        panel.add(cmbRol, gbc);

        if (usuario != null) {
            txtNombre.setText(usuario.getNombre());
            txtUsername.setText(usuario.getUsername());
            txtPassword.setText(usuario.getPassword());
            cmbRol.setSelectedItem(usuario.getRol());
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

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(panelBotones, gbc);

        add(panel);
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String rol = (String) cmbRol.getSelectedItem();

        if (nombre.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int excludeId = (usuario != null) ? usuario.getId() : 0;
        if (usuarioDAO.existeUsername(username, excludeId)) {
            JOptionPane.showMessageDialog(this, "Ese nombre de usuario ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (usuario == null) {
            Usuario nuevo = new Usuario(0, username, password, rol, nombre);
            usuarioDAO.insertar(nuevo);
        } else {
            usuario.setNombre(nombre);
            usuario.setUsername(username);
            usuario.setPassword(password);
            usuario.setRol(rol);
            usuarioDAO.actualizar(usuario);
        }

        confirmado = true;
        dispose();
    }

    public boolean isConfirmado() {
        return confirmado;
    }
}
