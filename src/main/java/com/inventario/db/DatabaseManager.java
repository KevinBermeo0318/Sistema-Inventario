package com.inventario.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:inventario.db";
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        conectar();
        inicializarTablas();
        insertarDatosIniciales();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void conectar() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.createStatement().execute("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos: " + e.getMessage(), e);
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                conectar();
            }
        } catch (SQLException e) {
            conectar();
        }
        return connection;
    }

    private void inicializarTablas() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS usuarios (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    rol TEXT NOT NULL CHECK(rol IN ('ADMIN','USUARIO')),
                    nombre TEXT NOT NULL
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS productos (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    codigo TEXT UNIQUE NOT NULL,
                    nombre TEXT NOT NULL,
                    descripcion TEXT,
                    categoria TEXT,
                    cantidad INTEGER NOT NULL DEFAULT 0,
                    precio REAL NOT NULL DEFAULT 0.0,
                    stock_minimo INTEGER NOT NULL DEFAULT 5,
                    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
                    fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS movimientos (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    producto_id INTEGER NOT NULL,
                    usuario_id INTEGER NOT NULL,
                    tipo TEXT NOT NULL CHECK(tipo IN ('ENTRADA','SALIDA','AJUSTE','CREACION','MODIFICACION','ELIMINACION')),
                    cantidad INTEGER NOT NULL DEFAULT 0,
                    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
                    observacion TEXT,
                    FOREIGN KEY(producto_id) REFERENCES productos(id),
                    FOREIGN KEY(usuario_id) REFERENCES usuarios(id)
                )
            """);

            migrarColumnasProductos(stmt);

        } catch (SQLException e) {
            throw new RuntimeException("Error al inicializar tablas: " + e.getMessage(), e);
        }
    }

    private void migrarColumnasProductos(Statement stmt) {
        try {
            stmt.execute("ALTER TABLE productos ADD COLUMN fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP");
        } catch (SQLException ignored) {}
        try {
            stmt.execute("ALTER TABLE productos ADD COLUMN fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP");
        } catch (SQLException ignored) {}
        try {
            stmt.execute("ALTER TABLE movimientos ADD COLUMN tipo TEXT NOT NULL DEFAULT 'AJUSTE'");
        } catch (SQLException ignored) {}
    }

    private void insertarDatosIniciales() {
        try (Statement stmt = connection.createStatement()) {
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM usuarios");
            if (rs.getInt(1) == 0) {
                connection.createStatement().execute("""
                    INSERT INTO usuarios (username, password, rol, nombre) VALUES
                    ('admin', 'admin123', 'ADMIN', 'Administrador'),
                    ('usuario', 'user123', 'USUARIO', 'Usuario General')
                """);

                connection.createStatement().execute("""
                    INSERT INTO productos (codigo, nombre, descripcion, categoria, cantidad, precio, stock_minimo) VALUES
                    ('P001', 'Laptop Dell', 'Laptop Dell Inspiron 15', 'Computadoras', 10, 850.00, 3),
                    ('P002', 'Mouse Logitech', 'Mouse inalambrico Logitech', 'Perifericos', 25, 35.00, 5),
                    ('P003', 'Teclado Mecanico', 'Teclado mecanico RGB', 'Perifericos', 15, 75.00, 5),
                    ('P004', 'Monitor LG 24"', 'Monitor LG Full HD 24 pulgadas', 'Monitores', 8, 220.00, 2),
                    ('P005', 'USB 32GB', 'Memoria USB 32GB', 'Almacenamiento', 50, 12.00, 10)
                """);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar datos iniciales: " + e.getMessage(), e);
        }
    }

    public void cerrar() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
