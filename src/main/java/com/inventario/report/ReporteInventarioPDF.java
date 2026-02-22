package com.inventario.report;

import com.inventario.dao.ProductoDAO;
import com.inventario.model.Producto;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReporteInventarioPDF {

    private static final Color COLOR_HEADER     = new Color(20, 30, 50);
    private static final Color COLOR_SUBTITULO  = new Color(50, 80, 140);
    private static final Color COLOR_FILA_PAR   = new Color(240, 244, 255);
    private static final Color COLOR_FILA_IMPAR = Color.WHITE;
    private static final Color COLOR_STOCK_BAJO = new Color(255, 210, 210);
    private static final Color COLOR_TEXTO_ROJO = new Color(160, 0, 0);
    private static final Color COLOR_TEXTO_OSC  = new Color(15, 20, 40);

    private final ProductoDAO productoDAO = new ProductoDAO();

    public String generar(String rutaDestino) throws DocumentException, IOException {
        List<Producto> productos = productoDAO.listarTodos();

        String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        String nombreArchivo = rutaDestino + "\\Reporte_Inventario_" +
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")) + ".pdf";

        Document doc = new Document(PageSize.A4.rotate(), 30, 30, 40, 30);
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(nombreArchivo));

        writer.setPageEvent(new PieDePagina(fechaHora));

        doc.open();

        agregarEncabezado(doc, fechaHora);
        agregarResumen(doc, productos);
        agregarTablaProductos(doc, productos);
        agregarSeccionStockBajo(doc, productos);

        doc.close();
        return nombreArchivo;
    }

    private void agregarEncabezado(Document doc, String fechaHora) throws DocumentException {
        Font fuenteTitulo = new Font(Font.HELVETICA, 22, Font.BOLD, Color.WHITE);
        Font fuenteFecha  = new Font(Font.HELVETICA, 10, Font.NORMAL, new Color(200, 210, 235));

        PdfPTable tablaHeader = new PdfPTable(1);
        tablaHeader.setWidthPercentage(100);

        PdfPCell celdaTitulo = new PdfPCell();
        celdaTitulo.setBackgroundColor(COLOR_HEADER);
        celdaTitulo.setBorder(Rectangle.NO_BORDER);
        celdaTitulo.setPadding(14);

        Paragraph titulo = new Paragraph("REPORTE DE INVENTARIO", fuenteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        celdaTitulo.addElement(titulo);

        Paragraph fecha = new Paragraph("Generado el: " + fechaHora, fuenteFecha);
        fecha.setAlignment(Element.ALIGN_CENTER);
        celdaTitulo.addElement(fecha);

        tablaHeader.addCell(celdaTitulo);
        doc.add(tablaHeader);
        doc.add(Chunk.NEWLINE);
    }

    private void agregarResumen(Document doc, List<Producto> productos) throws DocumentException {
        Font fuenteSeccion = new Font(Font.HELVETICA, 13, Font.BOLD, COLOR_SUBTITULO);
        Font fuenteDato    = new Font(Font.HELVETICA, 11, Font.NORMAL, COLOR_TEXTO_OSC);
        Font fuenteValor   = new Font(Font.HELVETICA, 11, Font.BOLD, COLOR_TEXTO_OSC);

        int totalProductos = productos.size();
        int totalUnidades = productos.stream().mapToInt(Producto::getCantidad).sum();
        double valorTotal = productos.stream().mapToDouble(p -> p.getCantidad() * p.getPrecio()).sum();
        long stockBajo = productos.stream().filter(Producto::stockBajo).count();

        Paragraph seccion = new Paragraph("RESUMEN GENERAL", fuenteSeccion);
        seccion.setSpacingBefore(4);
        seccion.setSpacingAfter(6);
        doc.add(seccion);

        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{1, 1, 1, 1});

        agregarCeldaResumen(tabla, "Total de Productos", String.valueOf(totalProductos), fuenteDato, fuenteValor, new Color(230, 240, 255));
        agregarCeldaResumen(tabla, "Total de Unidades", String.valueOf(totalUnidades), fuenteDato, fuenteValor, new Color(230, 245, 235));
        agregarCeldaResumen(tabla, "Valor Total del Stock", String.format("$%.2f", valorTotal), fuenteDato, fuenteValor, new Color(235, 245, 225));
        agregarCeldaResumen(tabla, "Productos Stock Bajo", String.valueOf(stockBajo),
            fuenteDato,
            new Font(Font.HELVETICA, 11, Font.BOLD, stockBajo > 0 ? COLOR_TEXTO_ROJO : new Color(0, 120, 0)),
            stockBajo > 0 ? new Color(255, 230, 230) : new Color(225, 255, 225));

        doc.add(tabla);
        doc.add(Chunk.NEWLINE);
    }

    private void agregarCeldaResumen(PdfPTable tabla, String etiqueta, String valor,
                                      Font fuenteEtiqueta, Font fuenteValor, Color fondo) {
        PdfPCell celda = new PdfPCell();
        celda.setBackgroundColor(fondo);
        celda.setPadding(10);
        celda.setBorderColor(new Color(200, 210, 230));

        Paragraph p = new Paragraph();
        p.add(new Chunk(etiqueta + "\n", fuenteEtiqueta));
        p.add(new Chunk(valor, fuenteValor));
        celda.addElement(p);
        tabla.addCell(celda);
    }

    private void agregarTablaProductos(Document doc, List<Producto> productos) throws DocumentException {
        Font fuenteSeccion  = new Font(Font.HELVETICA, 13, Font.BOLD, COLOR_SUBTITULO);
        Font fuenteColHeader = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
        Font fuenteCelda    = new Font(Font.HELVETICA, 9, Font.NORMAL, COLOR_TEXTO_OSC);
        Font fuenteCeldaRojo = new Font(Font.HELVETICA, 9, Font.BOLD, COLOR_TEXTO_ROJO);

        Paragraph seccion = new Paragraph("DETALLE DE PRODUCTOS", fuenteSeccion);
        seccion.setSpacingAfter(6);
        doc.add(seccion);

        PdfPTable tabla = new PdfPTable(7);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{0.5f, 1.2f, 2.8f, 1.5f, 0.8f, 1f, 0.8f});

        String[] headers = {"#", "Codigo", "Nombre", "Categoria", "Cantidad", "Precio", "Stock Min."};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, fuenteColHeader));
            cell.setBackgroundColor(COLOR_HEADER);
            cell.setPadding(7);
            cell.setBorderColor(new Color(50, 60, 90));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(cell);
        }

        int i = 0;
        for (Producto p : productos) {
            i++;
            Color fondo = p.stockBajo() ? COLOR_STOCK_BAJO : (i % 2 == 0 ? COLOR_FILA_PAR : COLOR_FILA_IMPAR);
            Font fuente = p.stockBajo() ? fuenteCeldaRojo : fuenteCelda;

            agregarCeldaTabla(tabla, String.valueOf(i), fuente, fondo, Element.ALIGN_CENTER);
            agregarCeldaTabla(tabla, p.getCodigo(), fuente, fondo, Element.ALIGN_LEFT);
            agregarCeldaTabla(tabla, p.getNombre(), fuente, fondo, Element.ALIGN_LEFT);
            agregarCeldaTabla(tabla, p.getCategoria() != null ? p.getCategoria() : "-", fuente, fondo, Element.ALIGN_LEFT);
            agregarCeldaTabla(tabla, String.valueOf(p.getCantidad()), fuente, fondo, Element.ALIGN_CENTER);
            agregarCeldaTabla(tabla, String.format("$%.2f", p.getPrecio()), fuente, fondo, Element.ALIGN_RIGHT);
            agregarCeldaTabla(tabla, String.valueOf(p.getStockMinimo()), fuente, fondo, Element.ALIGN_CENTER);
        }

        doc.add(tabla);
    }

    private void agregarCeldaTabla(PdfPTable tabla, String texto, Font fuente, Color fondo, int alineacion) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, fuente));
        cell.setBackgroundColor(fondo);
        cell.setPadding(5);
        cell.setBorderColor(new Color(210, 215, 230));
        cell.setHorizontalAlignment(alineacion);
        tabla.addCell(cell);
    }

    private void agregarSeccionStockBajo(Document doc, List<Producto> productos) throws DocumentException {
        List<Producto> bajos = productos.stream().filter(Producto::stockBajo).toList();
        if (bajos.isEmpty()) return;

        doc.add(Chunk.NEWLINE);
        Font fuenteSeccion  = new Font(Font.HELVETICA, 13, Font.BOLD, COLOR_TEXTO_ROJO);
        Font fuenteColHeader = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
        Font fuenteCelda    = new Font(Font.HELVETICA, 9, Font.BOLD, COLOR_TEXTO_ROJO);

        Paragraph seccion = new Paragraph("ALERTAS DE STOCK BAJO (" + bajos.size() + " productos)", fuenteSeccion);
        seccion.setSpacingAfter(6);
        doc.add(seccion);

        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidthPercentage(80);
        tabla.setWidths(new float[]{1.5f, 2.5f, 1f, 1f, 1.5f});

        String[] headers = {"Codigo", "Nombre", "Cantidad", "Stock Min.", "Diferencia"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, fuenteColHeader));
            cell.setBackgroundColor(new Color(160, 0, 0));
            cell.setPadding(7);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(cell);
        }

        for (Producto p : bajos) {
            Color fondo = new Color(255, 225, 225);
            agregarCeldaTabla(tabla, p.getCodigo(), fuenteCelda, fondo, Element.ALIGN_LEFT);
            agregarCeldaTabla(tabla, p.getNombre(), fuenteCelda, fondo, Element.ALIGN_LEFT);
            agregarCeldaTabla(tabla, String.valueOf(p.getCantidad()), fuenteCelda, fondo, Element.ALIGN_CENTER);
            agregarCeldaTabla(tabla, String.valueOf(p.getStockMinimo()), fuenteCelda, fondo, Element.ALIGN_CENTER);
            agregarCeldaTabla(tabla, String.valueOf(p.getCantidad() - p.getStockMinimo()), fuenteCelda, fondo, Element.ALIGN_CENTER);
        }

        doc.add(tabla);
    }

    private static class PieDePagina extends PdfPageEventHelper {
        private final String fechaHora;

        PieDePagina(String fechaHora) {
            this.fechaHora = fechaHora;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            Font fuente = new Font(Font.HELVETICA, 8, Font.NORMAL, new Color(120, 130, 150));

            Phrase pagina = new Phrase("Pagina " + writer.getPageNumber() + "  |  Sistema de Inventario  |  " + fechaHora, fuente);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, pagina,
                (document.left() + document.right()) / 2,
                document.bottom() - 10, 0);
        }
    }
}
