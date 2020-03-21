/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blaster.controller;

import blaster.model.Articulo;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

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
    
}
