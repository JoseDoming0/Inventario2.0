/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blaster.controller;

import blaster.model.Articulo;
import blaster.view.VistaConteo;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author José Domingo
 */
public class Controller implements ActionListener {

    private VistaConteo vista = new VistaConteo();
    private Operaciones op = new Operaciones();
    private int i = 0;
    private int editarPosicion = 0;

    public Controller() {
        vista.getMenuCargaBase().addActionListener(this);
        vista.getBtnContar().addActionListener(this);
        vista.getBtnZon().addActionListener(this);
        vista.getMenuReporteConteo().addActionListener(this);
        vista.getMenuReporteDif().addActionListener(this);
        vista.getBtnRSiguiente().addActionListener(this);
        vista.getBtnREditar().addActionListener(this);
        vista.getBtnRGuardar().addActionListener(this);
        vista.getBtnRRepRevisado().addActionListener(this);
        vista.getMenuAbout().addActionListener(this);
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

                } else {
                    if (e.getSource() == vista.getMenuReporteConteo()) {
                        op.generarReporteDeConteo();
                    } else {
                        if (e.getSource() == vista.getMenuReporteDif()) {
                            op.generaReporteDeDiferencas();
                            vista.getBtnRSiguiente().setEnabled(true);
                            vista.getBtnREditar().setEnabled(true);
                            vista.getBtnRRepRevisado().setEnabled(true);
                        } else {
                            if (e.getSource() == vista.getBtnRSiguiente()) {
                                vista.getBtnRGuardar().setEnabled(true);
                                if (i < op.getIndicesRevision().size()) {
                                    editarPosicion = op.getIndicesRevision().get(i);
                                    vista.getLblRMArt().setText(op.getListaParaContar().get(op.getIndicesRevision().get(i)).getCodigo());
                                    vista.getLblRMDesc().setText(op.getListaParaContar().get(op.getIndicesRevision().get(i)).getDescripcion());
                                    vista.getTfRSistema().setText(String.valueOf(op.getListaBase().get(op.getIndicesRevision().get(i)).getCantidad()));
                                    vista.getTfRFisico().setText(String.valueOf(op.getListaParaContar().get(op.getIndicesRevision().get(i)).getCantidad()));
                                    i++;
                                } else {
                                    vista.getBtnRGuardar().setEnabled(false);
                                    JOptionPane.showMessageDialog(null, "Revisión terminada");
                                }
                            } else {
                                if (e.getSource() == vista.getBtnREditar()) {
                                    vista.getTfRFisico().setEnabled(true);
                                } else {
                                    if (e.getSource() == vista.getBtnRGuardar()) {
                                        op.getListaParaContar().get(editarPosicion).setCantidad(Integer.parseInt(vista.getTfRFisico().getText()));
                                        vista.getTfRFisico().setEnabled(false);
                                        vista.getBtnRGuardar().setEnabled(false);
                                        if (!(op.getListaParaContar().get(editarPosicion).getCantidad() == op.getListaBase().get(editarPosicion).getCantidad())) {
                                            op.getListaDiferencias().add(new Articulo(op.getListaParaContar().get(editarPosicion).getCodigo(),
                                                    op.getListaParaContar().get(editarPosicion).getDescripcion(),
                                                    op.getListaParaContar().get(editarPosicion).getCantidad(),
                                                    op.getListaParaContar().get(editarPosicion).getLote(),
                                                    op.getListaParaContar().get(editarPosicion).getEan()));
                                        }
                                    } else {
                                        if (e.getSource() == vista.getBtnRRepRevisado()) {
                                            op.generaReportesRevision();
                                        } else {
                                            if (e.getSource() == vista.getMenuAbout()) {
                                                JOptionPane.showMessageDialog(null, "Desarrollado por: Jose Domingo Juarez Camacho \n ::: Version: 1.0:::\n"
                                                        + "d-_-b");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void validarLicencia() {
        try {
            Date fechaHoy = new Date();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            String fechaHoyString = formatoFecha.format(fechaHoy);
            Date fechaFinal = formatoFecha.parse("2020-08-06");
            Date fechaH = formatoFecha.parse(fechaHoyString);

            if (!fechaH.before(fechaFinal)) {
                JOptionPane.showMessageDialog(null, "Se termino la vigencia " 
                  ,"Licencia caducada"  , JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        } catch (ParseException ex) {
        }
    }
}
