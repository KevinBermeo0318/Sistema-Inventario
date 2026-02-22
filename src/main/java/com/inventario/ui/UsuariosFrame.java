package com.inventario.ui;

import com.inventario.dao.UsuarioDAO;
import com.inventario.model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class UsuariosFrame extends JFrame {

    private final Usuario usuarioActual;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private JList<Usuario> listaUsuarios;
    private DefaultListModel<Usuario> modeloLista;

    public UsuariosFrame(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        setTitle("Gestion de Usuarios");
        setSize(500, 450);
        setLocationRelativeTo(null);
        inicializarUI();
        cargarUsuarios();
    }

    private void inicializarUI() {
        setLayout(new BorderLayout(10, 10));
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Usuarios del Sistema");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(titulo, BorderLayout.NORTH);

        modeloLista = new DefaultListModel<>();
        listaUsuarios = new JList<>(modeloLista);
        listaUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        listaUsuarios.setCellRenderer(new UsuarioCellRenderer());
        panel.add(new JScrollPane(listaUsuarios), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));

        JButton btnAgregar = crearBoton("Agregar Usuario", new Color(40, 140, 80));
        btnAgregar.addActionListener(e -> agregarUsuario());

        JButton btnEditar = crearBoton("Editar", new Color(50, 100, 200));
        btnEditar.addActionListener(e -> editarUsuario());

        JButton btnEliminar = crearBoton("Eliminar", new Color(180, 50, 50));
        btnEliminar.addActionListener(e -> eliminarUsuario());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panel.add(panelBotones, BorderLayout.SOUTH);

        add(panel);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    private void cargarUsuarios() {
        modeloLista.clear();
        for (Usuario u : usuarioDAO.listarTodos()) {
            modeloLista.addElement(u);
        }
    }

    private void agregarUsuario() {
        UsuarioDialog dlg = new UsuarioDialog(this, null);
        dlg.setVisible(true);
        if (dlg.isConfirmado()) cargarUsuarios();
    }

    private void editarUsuario() {
        Usuario u = listaUsuarios.getSelectedValue();
        if (u == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        UsuarioDialog dlg = new UsuarioDialog(this, u);
        dlg.setVisible(true);
        if (dlg.isConfirmado()) cargarUsuarios();
    }

    private void eliminarUsuario() {
        Usuario u = listaUsuarios.getSelectedValue();
        if (u == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (u.getId() == usuarioActual.getId()) {
            JOptionPane.showMessageDialog(this, "No puede eliminar su propio usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int res = JOptionPane.showConfirmDialog(this,
            "Eliminar al usuario: " + u.getNombre() + "?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            usuarioDAO.eliminar(u.getId());
            cargarUsuarios();
        }
    }

    private static class UsuarioCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                       boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Usuario) {
                Usuario u = (Usuario) value;
                setText(u.getNombre() + " (" + u.getUsername() + ") - " + u.getRol());
                setIcon(UIManager.getIcon("OptionPane.informationIcon"));
            }
            return this;
        }
    }
}
