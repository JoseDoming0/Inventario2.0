
import blaster.controller.Controller;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author José Domingo
 */
public class TestInventario {

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
            
            Controller c = new Controller();
            c.getVista().setVisible(true);
            c.validarLicencia();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestInventario.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(TestInventario.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TestInventario.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(TestInventario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
