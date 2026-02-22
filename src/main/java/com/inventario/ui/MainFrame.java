package com.inventario.ui;

import com.inventario.dao.ProductoDAO;
import com.inventario.model.Producto;
import com.inventario.model.Usuario;
import com.inventario.report.ReporteInventarioPDF;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;
import java.util.List;

public class MainFrame extends JFrame {

    private final Usuario usuarioActual;
    private final ProductoDAO productoDAO = new ProductoDAO();

    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    private JLabel lblUsuario;
    private JLabel lblStock;

    private static final String[] COLUMNAS = {"ID", "Codigo", "Nombre", "Categoria", "Cantidad", "Precio", "Stock Min."};

    public MainFrame(Usuario usuario) {
        this.usuarioActual = usuario;
        setTitle("Sistema de Inventario");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 600));
        inicializarUI();
        cargarProductos(null);
        pack();
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void inicializarUI() {
        setLayout(new BorderLayout());

        add(crearPanelHeader(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(20, 30, 50));
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel lblTitulo = new JLabel("SISTEMA DE INVENTARIO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);

        JPanel panelDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelDerecha.setOpaque(false);
        lblUsuario = new JLabel(usuarioActual.getNombre() + " [" + usuarioActual.getRol() + "]");
        lblUsuario.setForeground(new Color(220, 230, 255));
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JButton btnCerrar = new JButton("Cerrar Sesion");
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBackground(new Color(180, 50, 50));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setOpaque(true);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        panelDerecha.add(lblUsuario);
        panelDerecha.add(btnCerrar);

        panel.add(lblTitulo, BorderLayout.WEST);
        panel.add(panelDerecha, BorderLayout.EAST);
        return panel;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBorder(new EmptyBorder(10, 15, 5, 15));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblBuscar.setForeground(new Color(20, 30, 60));
        panelBusqueda.add(lblBuscar);
        txtBuscar = new JTextField(25);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtBuscar.setForeground(Color.BLACK);
        panelBusqueda.add(txtBuscar);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> cargarProductos(txtBuscar.getText().trim()));
        panelBusqueda.add(btnBuscar);

        JButton btnMostrarTodo = new JButton("Mostrar Todo");
        btnMostrarTodo.addActionListener(e -> {
            txtBuscar.setText("");
            cargarProductos(null);
        });
        panelBusqueda.add(btnMostrarTodo);

        lblStock = new JLabel();
        lblStock.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblStock.setForeground(new Color(190, 30, 30));
        panelBusqueda.add(Box.createHorizontalStrut(20));
        panelBusqueda.add(lblStock);

        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
            @Override
            public Class<?> getColumnClass(int col) {
                if (col == 0 || col == 4 || col == 6) return Integer.class;
                if (col == 5) return Double.class;
                return String.class;
            }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaProductos.setForeground(new Color(15, 20, 40));
        tablaProductos.setRowHeight(28);
        tablaProductos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaProductos.getTableHeader().setForeground(new Color(15, 20, 40));
        tablaProductos.getTableHeader().setBackground(new Color(210, 220, 240));
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProductos.setSelectionBackground(new Color(60, 100, 200));
        tablaProductos.setSelectionForeground(Color.WHITE);
        tablaProductos.setGridColor(new Color(200, 210, 230));
        tablaProductos.setDefaultRenderer(Object.class, new StockRenderer());
        tablaProductos.setDefaultRenderer(Integer.class, new StockRenderer());
        tablaProductos.setDefaultRenderer(Double.class, new StockRenderer());

        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(80);
        tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(200);
        tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(120);
        tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(80);
        tablaProductos.getColumnModel().getColumn(5).setPreferredWidth(90);
        tablaProductos.getColumnModel().getColumn(6).setPreferredWidth(80);

        tablaProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && usuarioActual.esAdmin()) {
                    editarProductoSeleccionado();
                }
            }
        });

        panel.add(panelBusqueda, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(5, 15, 10, 15));
        panel.setBackground(new Color(230, 235, 250));

        JPanel panelIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelIzq.setOpaque(false);

        if (usuarioActual.esAdmin()) {
            JButton btnAgregar = crearBoton("+ Agregar Producto", new Color(40, 140, 80));
            btnAgregar.addActionListener(e -> agregarProducto());

            JButton btnEditar = crearBoton("Editar Producto", new Color(50, 100, 200));
            btnEditar.addActionListener(e -> editarProductoSeleccionado());

            JButton btnEliminar = crearBoton("Eliminar Producto", new Color(180, 50, 50));
            btnEliminar.addActionListener(e -> eliminarProducto());

            JButton btnUsuarios = crearBoton("Gestionar Usuarios", new Color(100, 50, 150));
            btnUsuarios.addActionListener(e -> new UsuariosFrame(usuarioActual).setVisible(true));

            panelIzq.add(btnAgregar);
            panelIzq.add(btnEditar);
            panelIzq.add(btnEliminar);
            panelIzq.add(btnUsuarios);
        }

        JButton btnMovimientos = crearBoton("Ver Movimientos", new Color(80, 130, 130));
        btnMovimientos.addActionListener(e -> new MovimientosFrame().setVisible(true));
        panelIzq.add(btnMovimientos);

        JButton btnActualizar = crearBoton("Actualizar", new Color(70, 70, 70));
        btnActualizar.addActionListener(e -> cargarProductos(txtBuscar.getText().trim()));
        panelIzq.add(btnActualizar);

        JButton btnReporte = crearBoton("Generar Reporte PDF", new Color(100, 60, 140));
        btnReporte.addActionListener(e -> generarReportePDF());
        panelIzq.add(btnReporte);

        panel.add(panelIzq, BorderLayout.WEST);

        if (usuarioActual.esAdmin()) {
            JPanel panelDer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            panelDer.setOpaque(false);
            JButton btnMovEntrada = crearBoton("Registrar Entrada", new Color(40, 140, 80));
            btnMovEntrada.addActionListener(e -> registrarMovimiento("ENTRADA"));
            JButton btnMovSalida = crearBoton("Registrar Salida", new Color(180, 100, 30));
            btnMovSalida.addActionListener(e -> registrarMovimiento("SALIDA"));
            panelDer.add(btnMovEntrada);
            panelDer.add(btnMovSalida);
            panel.add(panelDer, BorderLayout.EAST);
        }

        return panel;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void cargarProductos(String filtro) {
        modeloTabla.setRowCount(0);
        List<Producto> lista = (filtro == null || filtro.isEmpty())
            ? productoDAO.listarTodos()
            : productoDAO.buscar(filtro);

        int stockBajo = 0;
        for (Producto p : lista) {
            modeloTabla.addRow(new Object[]{
                p.getId(), p.getCodigo(), p.getNombre(), p.getCategoria(),
                p.getCantidad(), p.getPrecio(), p.getStockMinimo()
            });
            if (p.stockBajo()) stockBajo++;
        }

        if (stockBajo > 0) {
            lblStock.setText("ALERTA: " + stockBajo + " producto(s) con stock bajo");
        } else {
            lblStock.setText("");
        }
    }

    private Producto getProductoSeleccionado() {
        int fila = tablaProductos.getSelectedRow();
        if (fila < 0) return null;
        int id = (int) modeloTabla.getValueAt(fila, 0);
        return productoDAO.listarTodos().stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    private void agregarProducto() {
        ProductoDialog dlg = new ProductoDialog(this, null, usuarioActual);
        dlg.setVisible(true);
        if (dlg.isConfirmado()) cargarProductos(null);
    }

    private void editarProductoSeleccionado() {
        Producto p = getProductoSeleccionado();
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        ProductoDialog dlg = new ProductoDialog(this, p, usuarioActual);
        dlg.setVisible(true);
        if (dlg.isConfirmado()) cargarProductos(null);
    }

    private void eliminarProducto() {
        Producto p = getProductoSeleccionado();
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int res = JOptionPane.showConfirmDialog(this,
            "Esta seguro de eliminar el producto: " + p.getNombre() + "?",
            "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            productoDAO.registrarMovimiento(p.getId(), usuarioActual.getId(), "ELIMINACION", p.getCantidad(),
                "Producto eliminado por " + usuarioActual.getNombre() + " | Codigo: " + p.getCodigo());
            productoDAO.eliminar(p.getId());
            cargarProductos(null);
        }
    }

    private void registrarMovimiento(String tipo) {
        Producto p = getProductoSeleccionado();
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String cantStr = JOptionPane.showInputDialog(this, "Cantidad a registrar (" + tipo + "):", "Movimiento", JOptionPane.QUESTION_MESSAGE);
        if (cantStr == null || cantStr.trim().isEmpty()) return;
        try {
            int cantidad = Integer.parseInt(cantStr.trim());
            if (cantidad <= 0) throw new NumberFormatException();

            int nueva = tipo.equals("ENTRADA") ? p.getCantidad() + cantidad : p.getCantidad() - cantidad;
            if (nueva < 0) {
                JOptionPane.showMessageDialog(this, "Stock insuficiente para la salida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            p.setCantidad(nueva);
            productoDAO.actualizar(p);
            productoDAO.registrarMovimiento(p.getId(), usuarioActual.getId(), tipo, cantidad, tipo + " registrada por " + usuarioActual.getNombre());
            cargarProductos(null);
            JOptionPane.showMessageDialog(this, tipo + " registrada exitosamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad valida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class StockRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                        boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                int cantidad = (int) table.getValueAt(row, 4);
                int stockMin = (int) table.getValueAt(row, 6);
                if (cantidad <= stockMin) {
                    c.setBackground(new Color(255, 210, 210));
                    c.setForeground(new Color(140, 0, 0));
                } else if (row % 2 == 0) {
                    c.setBackground(Color.WHITE);
                    c.setForeground(new Color(15, 20, 40));
                } else {
                    c.setBackground(new Color(240, 244, 255));
                    c.setForeground(new Color(15, 20, 40));
                }
            } else {
                c.setForeground(Color.WHITE);
            }
            if (column == 5 && value instanceof Double) {
                setText(String.format("$%.2f", (Double) value));
            }
            return c;
        }
    }

    private void generarReportePDF() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar carpeta para guardar el reporte");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setSelectedFile(new File(System.getProperty("user.home") + "\\Desktop"));

        int res = chooser.showSaveDialog(this);
        if (res != JFileChooser.APPROVE_OPTION) return;

        String carpeta = chooser.getSelectedFile().getAbsolutePath();

        try {
            String archivo = new ReporteInventarioPDF().generar(carpeta);
            int abrir = JOptionPane.showConfirmDialog(this,
                "Reporte generado exitosamente:\n" + archivo + "\n\n¿Desea abrir el archivo?",
                "Reporte PDF", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (abrir == JOptionPane.YES_OPTION) {
                Desktop.getDesktop().open(new File(archivo));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al generar el reporte:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
