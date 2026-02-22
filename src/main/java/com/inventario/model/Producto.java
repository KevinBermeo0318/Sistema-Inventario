package com.inventario.model;

public class Producto {
    private int id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String categoria;
    private int cantidad;
    private double precio;
    private int stockMinimo;
    private String fechaCreacion;
    private String fechaActualizacion;

    public Producto() {}

    public Producto(int id, String codigo, String nombre, String descripcion,
                    String categoria, int cantidad, double precio, int stockMinimo) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.precio = precio;
        this.stockMinimo = stockMinimo;
    }

    public Producto(int id, String codigo, String nombre, String descripcion,
                    String categoria, int cantidad, double precio, int stockMinimo,
                    String fechaCreacion, String fechaActualizacion) {
        this(id, codigo, nombre, descripcion, categoria, cantidad, precio, stockMinimo);
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(String fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public boolean stockBajo() {
        return cantidad <= stockMinimo;
    }
}
