package com.inventario.ui;

import com.inventario.dao.UsuarioDAO;
import com.inventario.model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private JLabel lblError;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public LoginFrame() {
        setTitle("Sistema de Inventario - Iniciar Sesion");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        inicializarUI();
        pack();
        setLocationRelativeTo(null);
    }

    private void inicializarUI() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(20, 30, 50));

        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(new Color(20, 30, 50));
        panelHeader.setBorder(new EmptyBorder(30, 40, 10, 40));
        JLabel lblTitulo = new JLabel("SISTEMA DE INVENTARIO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(255, 255, 255));
        JLabel lblSubtitulo = new JLabel("Gestion de productos y stock");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(200, 215, 240));
        panelHeader.setLayout(new BoxLayout(panelHeader, BoxLayout.Y_AXIS));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelHeader.add(lblTitulo);
        panelHeader.add(Box.createVerticalStrut(5));
        panelHeader.add(lblSubtitulo);

        JPanel panelForm = new JPanel();
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(new EmptyBorder(30, 40, 30, 40));
        panelForm.setLayout(new GridBagLayout());
        panelForm.setPreferredSize(new Dimension(380, 270));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblIniciar = new JLabel("Iniciar Sesion");
        lblIniciar.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblIniciar.setForeground(new Color(0, 0, 0));
        panelForm.add(lblIniciar, gbc);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Color labelColor = new Color(30, 40, 70);

        gbc.gridy = 1; gbc.gridwidth = 1; gbc.gridx = 0; gbc.weightx = 0.3;
        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setFont(labelFont);
        lblUser.setForeground(labelColor);
        panelForm.add(lblUser, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtUsuario = new JTextField(15);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setForeground(Color.BLACK);
        panelForm.add(txtUsuario, gbc);

        gbc.gridy = 2; gbc.gridx = 0; gbc.weightx = 0.3;
        JLabel lblPass = new JLabel("Contrasena:");
        lblPass.setFont(labelFont);
        lblPass.setForeground(labelColor);
        panelForm.add(lblPass, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtPassword = new JPasswordField(15);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setForeground(Color.BLACK);
        panelForm.add(txtPassword, gbc);

        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        lblError = new JLabel(" ");
        lblError.setForeground(new Color(200, 30, 30));
        lblError.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelForm.add(lblError, gbc);

        gbc.gridy = 4;
        btnIngresar = new JButton("INGRESAR");
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIngresar.setBackground(new Color(20, 30, 50));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setOpaque(true);
        btnIngresar.setBorderPainted(false);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnIngresar.setPreferredSize(new Dimension(200, 38));
        panelForm.add(btnIngresar, gbc);

        JPanel panelHint = new JPanel();
        panelHint.setBackground(new Color(230, 235, 250));
        panelHint.setBorder(new EmptyBorder(6, 10, 10, 10));
        JLabel hint = new JLabel("<html><b style='color:#1a2a4a'>Admin:</b> <span style='color:#000'>admin / admin123</span> &nbsp;&nbsp; <b style='color:#1a2a4a'>Usuario:</b> <span style='color:#000'>usuario / user123</span></html>");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelHint.add(hint);

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(panelForm, BorderLayout.CENTER);
        panelPrincipal.add(panelHint, BorderLayout.SOUTH);

        add(panelPrincipal);

        btnIngresar.addActionListener(e -> login());
        txtPassword.addActionListener(e -> login());
        txtUsuario.addActionListener(e -> txtPassword.requestFocus());
    }

    private void login() {
        String user = txtUsuario.getText().trim();
        String pass = new String(txtPassword.getPassword());
        if (user.isEmpty() || pass.isEmpty()) {
            lblError.setText("Ingrese usuario y contrasena");
            return;
        }
        Usuario u = usuarioDAO.autenticar(user, pass);
        if (u != null) {
            dispose();
            new MainFrame(u).setVisible(true);
        } else {
            lblError.setText("Usuario o contrasena incorrectos");
            txtPassword.setText("");
        }
    }
}
