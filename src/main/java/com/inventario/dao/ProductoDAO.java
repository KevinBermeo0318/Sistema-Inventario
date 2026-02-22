package com.inventario.dao;

import com.inventario.db.DatabaseManager;
import com.inventario.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    private Connection getConn() {
        return DatabaseManager.getInstance().getConnection();
    }

    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY nombre";
        try (Statement stmt = getConn().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Producto> buscar(String filtro) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE LOWER(nombre) LIKE ? OR LOWER(codigo) LIKE ? OR LOWER(categoria) LIKE ? ORDER BY nombre";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            String f = "%" + filtro.toLowerCase() + "%";
            ps.setString(1, f);
            ps.setString(2, f);
            ps.setString(3, f);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean insertar(Producto p) {
        String sql = "INSERT INTO productos (codigo, nombre, descripcion, categoria, cantidad, precio, stock_minimo) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setString(4, p.getCategoria());
            ps.setInt(5, p.getCantidad());
            ps.setDouble(6, p.getPrecio());
            ps.setInt(7, p.getStockMinimo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizar(Producto p) {
        String sql = "UPDATE productos SET codigo=?, nombre=?, descripcion=?, categoria=?, cantidad=?, precio=?, stock_minimo=? WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setString(4, p.getCategoria());
            ps.setInt(5, p.getCantidad());
            ps.setDouble(6, p.getPrecio());
            ps.setInt(7, p.getStockMinimo());
            ps.setInt(8, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existeCodigo(String codigo, int excludeId) {
        String sql = "SELECT COUNT(*) FROM productos WHERE codigo=? AND id!=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, codigo);
            ps.setInt(2, excludeId);
            ResultSet rs = ps.executeQuery();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void registrarMovimiento(int productoId, int usuarioId, String tipo, int cantidad, String observacion) {
        String sql = "INSERT INTO movimientos (producto_id, usuario_id, tipo, cantidad, observacion) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, productoId);
            ps.setInt(2, usuarioId);
            ps.setString(3, tipo);
            ps.setInt(4, cantidad);
            ps.setString(5, observacion);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> listarMovimientos() {
        List<String[]> lista = new ArrayList<>();
        String sql = """
            SELECT m.fecha, p.nombre, u.nombre, m.tipo, m.cantidad, m.observacion
            FROM movimientos m
            JOIN productos p ON m.producto_id = p.id
            JOIN usuarios u ON m.usuario_id = u.id
            ORDER BY m.fecha DESC
            LIMIT 200
        """;
        try (Statement stmt = getConn().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new String[]{
                    rs.getString(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), String.valueOf(rs.getInt(5)), rs.getString(6)
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private Producto mapear(ResultSet rs) throws SQLException {
        return new Producto(
            rs.getInt("id"),
            rs.getString("codigo"),
            rs.getString("nombre"),
            rs.getString("descripcion"),
            rs.getString("categoria"),
            rs.getInt("cantidad"),
            rs.getDouble("precio"),
            rs.getInt("stock_minimo")
        );
    }
}
