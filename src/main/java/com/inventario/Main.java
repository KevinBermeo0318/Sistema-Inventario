package com.inventario;

import com.inventario.db.DatabaseManager;
import com.inventario.ui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        DatabaseManager.getInstance();

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> DatabaseManager.getInstance().cerrar()));
    }
}
