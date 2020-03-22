/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blaster.controller;

import blaster.model.Articulo;
import blaster.model.ArticuloContado;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author José Domingo
 */
public class Operaciones {

    private ArrayList<Articulo> listaBase = new ArrayList<Articulo>();
    private ArrayList<Articulo> listaParaContar = new ArrayList<Articulo>();
    private JFileChooser fileChooser = new JFileChooser();
    private String ruta = "";
    private EntradaSalida es = new EntradaSalida();
    private int contador = 0;
    private String ean, codigo, desripcion, zona;
    private int totalPorArticulo = 0;
    private ArrayList<ArticuloContado> nuevaLista = new ArrayList<>();
    private int contadorZona = 0;
    private ArrayList<Integer> indicesRevision = new ArrayList<>();

    public void cargarBase() {
        System.out.println("Cargar la base desde operaciones");
        fileChooser.showOpenDialog(fileChooser);
        ruta = fileChooser.getSelectedFile().getAbsolutePath();
        listaBase = es.leerArchivoBase(ruta);
        listaParaContar = es.leerArchivoBase(ruta);
        for (Articulo articulo : listaParaContar) {
            articulo.setCantidad(0);
        }   
    }
    
    public void conteoDeArticulos(String contado) {
        boolean encontrado = false;
        int i = 0;
        for (i = 0; i < listaParaContar.size(); i++) {
            if (listaParaContar.get(i).getCodigo().equals(contado) || listaParaContar.get(i).getEan().equals(contado)) {
                encontrado = true;
                System.out.println("es igual" + encontrado);
                break;
            }
        }
        if (encontrado) {
            contador += 1;
            listaParaContar.get(i).setCantidad(listaParaContar.get(i).getCantidad() + 1);
            totalPorArticulo = listaParaContar.get(i).getCantidad();
            ean = listaParaContar.get(i).getEan();
            codigo = listaParaContar.get(i).getCodigo();
            desripcion = listaParaContar.get(i).getDescripcion();
            nuevaLista.add(new ArticuloContado(contador, ean, codigo, desripcion, zona));
            System.out.println("tamano de nueva lista" + nuevaLista.size());
            contadorZona += 1;
        } else {
            JOptionPane.showMessageDialog(null, "Codigo de barras o artículo incorrecto", "Verificar conteo", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void establecerZona(String zona) {
        contadorZona = 0;
        if (zona != null) {
            this.zona = zona;
        } else {
            this.zona = "";
        }
    }
    
    public void generarReporteDeConteo(){
        try {
            es.generaReporteConteo(nuevaLista, ruta);
        } catch (IOException ex) {
            Logger.getLogger(Operaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void generaReporteDeDiferencas(){
        try {
            indicesRevision = es.gererarReporteDiferencias(listaParaContar, ruta,listaBase);
        } catch (IOException ex) {
            Logger.getLogger(Operaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }

    public int getTotalPorArticulo() {
        return totalPorArticulo;
    }

    public void setTotalPorArticulo(int totalPorArticulo) {
        this.totalPorArticulo = totalPorArticulo;
    }

    public int getContadorZona() {
        return contadorZona;
    }

    public void setContadorZona(int contadorZona) {
        this.contadorZona = contadorZona;
    }

    public ArrayList<Articulo> getListaParaContar() {
        return listaParaContar;
    }

    public void setListaParaContar(ArrayList<Articulo> listaParaContar) {
        this.listaParaContar = listaParaContar;
    }

    public ArrayList<ArticuloContado> getNuevaLista() {
        return nuevaLista;
    }

    public void setNuevaLista(ArrayList<ArticuloContado> nuevaLista) {
        this.nuevaLista = nuevaLista;
    }
    
    

}
