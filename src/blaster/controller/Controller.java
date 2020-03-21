/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blaster.controller;

import blaster.model.Articulo;
import blaster.model.ArticuloContado;
import blaster.view.VistaConteo;
import java.awt.Color;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Action;
import javax.swing.JOptionPane;

/**
 *
 * @author José Domingo
 */
public class Controller implements ActionListener {

    private VistaConteo vista = new VistaConteo();
    private Operaciones op = new Operaciones();

    public Controller() {
        vista.getMenuCargaBase().addActionListener(this);
        vista.getBtnContar().addActionListener(this);
        vista.getBtnZon().addActionListener(this);
        llenarTable();
    }

    public VistaConteo getVista() {
        return vista;
    }

    public void setVista(VistaConteo vista) {
        this.vista = vista;
    }
    
     public void llenarTable() {
        String[][] datosTabla = new String[op.getNuevaLista().size()][5];
        for (int i = 0; i < op.getNuevaLista().size(); i++) {
            datosTabla[i][0] = String.valueOf(op.getNuevaLista().get(i).getNumero());
            datosTabla[i][1] = op.getNuevaLista().get(i).getEan();
            datosTabla[i][2] = op.getNuevaLista().get(i).getCodigo();
            datosTabla[i][3] = op.getNuevaLista().get(i).getDescripcion();
            datosTabla[i][4] = op.getNuevaLista().get(i).getZona();
        }
        vista.getjTable1().setModel(new javax.swing.table.DefaultTableModel(
                datosTabla,
                new String[]{
                    "Numero", "Codigo de barras", "Codigo", "Descripcion", "Zona"
                }
        ));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.getMenuCargaBase()) {
            op.cargarBase();
            vista.getTfIntroduceArt().setEnabled(true);
            vista.getBtnContar().setEnabled(true);
            vista.getTfZona().setEnabled(true);
            vista.getLblBaseCargada().setForeground(new Color(0, 240, 10));
            vista.getBtnZon().setEnabled(true);
        } else {
            if (e.getSource() == vista.getBtnContar()) {
                if (!vista.getTfIntroduceArt().getText().equals("")) {
                    op.conteoDeArticulos(vista.getTfIntroduceArt().getText());
                    vista.getLblFArtActual().setText(vista.getTfIntroduceArt().getText());
                    vista.getTfIntroduceArt().setText("");
                    vista.getLblMArtContados().setText(String.valueOf(op.getTotalPorArticulo()));
                    vista.getLblMArtTotal().setText(String.valueOf(op.getNuevaLista().size()));
                    vista.getLblMArtPoZona().setText(String.valueOf(op.getContadorZona()));
                    llenarTable();
                } else {
                    JOptionPane.showMessageDialog(null, "No has introducido ningún código", "Codigo vacío", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (e.getSource() == vista.getBtnZon()) {
                    op.establecerZona(vista.getTfZona().getText());
                    JOptionPane.showMessageDialog(null, "La zona establecida es " + vista.getTfZona().getText(), "ZONA", JOptionPane.INFORMATION_MESSAGE);

                }
            }
        }

    }

}
