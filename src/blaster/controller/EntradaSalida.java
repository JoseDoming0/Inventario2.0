/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blaster.controller;

import blaster.model.Articulo;
import blaster.model.ArticuloContado;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author José Domingo
 */
public class EntradaSalida {
    
    
    public ArrayList<Articulo> leerArchivoBase(String ruta) {
        
        ArrayList<Articulo> listaBase = new ArrayList<>();
        
        FileInputStream excelStream = null;
        try {
            excelStream = new FileInputStream(ruta);

            HSSFWorkbook libro = new HSSFWorkbook(excelStream);
            HSSFSheet hoja = libro.getSheetAt(0);
            HSSFRow fila;
            int filas = hoja.getLastRowNum() + 1;
            int columnas = 0;
            String codigo = "";
            String desc = "";
            int cantidad = 0;
            String lote = "";
            String ean = "";

            for (int r = 0 + 1; r < filas; r++) {
                fila = hoja.getRow(r);
                if (fila == null) {
                    break;
                } else {
                    for (int c = 0; c < (columnas = fila.getLastCellNum()); c++) {
                        codigo = fila.getCell(columnas - 5).getStringCellValue();
                        desc = fila.getCell(columnas - 4).getStringCellValue();
                        cantidad = (int) fila.getCell(columnas - 3).getNumericCellValue();
                        lote = fila.getCell(columnas - 2).getStringCellValue();
                        ean = fila.getCell(columnas - 1).getStringCellValue();
                    }
                    listaBase.add(new Articulo(codigo, desc, cantidad, lote, ean));
                   // listaContados.add(new Articulo(codigo, desc, 0, lote, ean));
                }
            }
            System.out.println("***********************BASE CARGADA*******************************************");
        } catch (FileNotFoundException fnfe) {
            System.out.println("archivo no encontrado");
        } catch (IOException e) {
            System.out.println("Error al procesar");
            JOptionPane.showMessageDialog(null, "Archivo incorrecto", "Error en el archivo", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                excelStream.close();
            } catch (IOException e) {
                System.out.println("Error al procesar");
            }
        }
        return listaBase;
    }
    
    public void generaReporteConteo(ArrayList<ArticuloContado> nuevaLista, String ruta) throws FileNotFoundException, IOException {

        Workbook wb = new HSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        org.apache.poi.ss.usermodel.Sheet hoja1 = wb.createSheet("Reporte Conteo");

        Row fila1 = hoja1.createRow(0);
        fila1.createCell(0).setCellValue("Número");
        fila1.createCell(1).setCellValue("Codigo de barras");
        fila1.createCell(2).setCellValue("Codigo");
        fila1.createCell(3).setCellValue("Descripcion");
        fila1.createCell(4).setCellValue("Zona");
        int rowNum = 1;
        int numero = 1;
        for (int i = 0; i < nuevaLista.size(); i++) {

            Row row = hoja1.createRow(rowNum++);
            row.createCell(0).setCellValue(numero++);
            row.createCell(1).setCellValue(nuevaLista.get(i).getEan());
            row.createCell(2).setCellValue(nuevaLista.get(i).getCodigo());
            row.createCell(3).setCellValue(nuevaLista.get(i).getDescripcion());
            row.createCell(4).setCellValue(nuevaLista.get(i).getZona());
        }
        String archivo = "Reporte de Conteo " + JOptionPane.showInputDialog("Marca: ");
        FileOutputStream fileOut = new FileOutputStream(ruta+archivo + ".xls");
        wb.write(fileOut);
        fileOut.close();
        JOptionPane.showMessageDialog(null, "Reporte creado");
    }
    
     public ArrayList<Integer> gererarReporteDiferencias(ArrayList<Articulo> listaContados, String ruta, ArrayList<Articulo> listaBase) throws IOException {
        ArrayList<Integer> indicesRevision = new ArrayList<>();
        String marca = "";
        int diferencia = 0;
        Workbook wb = new HSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        org.apache.poi.ss.usermodel.Sheet hoja1 = wb.createSheet("Reporte");

        Row fila1 = hoja1.createRow(0);
        fila1.createCell(0).setCellValue("Número");
        fila1.createCell(1).setCellValue("Clave");
        fila1.createCell(2).setCellValue("Descripción");
        fila1.createCell(3).setCellValue("Físico");
        fila1.createCell(4).setCellValue("Sistema");
        fila1.createCell(5).setCellValue("Diferencia");
        fila1.createCell(6).setCellValue("Observación");

        int rowNum = 1;
        int numero = 1;
        int n1 = 0, n2 = 0;
        for (int i = 0; i < listaContados.size(); i++) {

            Row row = hoja1.createRow(rowNum++);
            row.createCell(0).setCellValue(numero++);
            row.createCell(1).setCellValue(listaContados.get(i).getCodigo());
            row.createCell(2).setCellValue(listaContados.get(i).getDescripcion());
            n1 = listaContados.get(i).getCantidad();
            row.createCell(3).setCellValue(n1);
            n2 = listaBase.get(i).getCantidad();
            row.createCell(4).setCellValue(n2);
            diferencia = listaContados.get(i).getCantidad() - listaBase.get(i).getCantidad();
            row.createCell(5).setCellValue(diferencia);
            if (diferencia < 0) {
                row.createCell(6).setCellValue(" Faltante");
                indicesRevision.add(i);
            } else {
                if (diferencia > 0) {
                    row.createCell(6).setCellValue(" Sobrante");
                    indicesRevision.add(i);
                }
            }
        }
        marca = JOptionPane.showInputDialog("Marca:");
        String archivo = "Reporte de Inventario " + marca;
        System.out.println("Reporte creado");
        FileOutputStream fileOut = new FileOutputStream(ruta+archivo + ".xls");
        wb.write(fileOut);
        fileOut.close();
        JOptionPane.showMessageDialog(null, "Se ha creado el reporte", "Reporte Final", JOptionPane.INFORMATION_MESSAGE);
        return indicesRevision;
    }
    
}
