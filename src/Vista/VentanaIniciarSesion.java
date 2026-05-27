package Vista;

import javax.swing.*;
import java.awt.*;
import Controlador.ControladorFinanzas;
import Controlador.ControladorUsuario;


public class VentanaIniciarSesion extends JFrame {

    private final JFrame frame = new JFrame();
    private JTextField EscribirMatricula;
    private JPasswordField EscribirContraseña;
    private ControladorFinanzas controladorFinanzas;
    private ControladorUsuario controladorUsuario;


    public VentanaIniciarSesion(ControladorUsuario controladorUsuario, ControladorFinanzas controladorFinanzas){
        this.controladorUsuario = controladorUsuario;
        this.controladorFinanzas = controladorFinanzas;
    }

}


