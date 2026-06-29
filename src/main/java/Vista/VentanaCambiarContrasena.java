package Vista;

import Controlador.ControladorUsuario;
import Modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaCambiarContrasena extends JFrame {
    private ControladorUsuario controladorUsuario;
    private JPasswordField campoContrasenaActual;
    private JPasswordField campoNuevaContrasena;
    private JPasswordField campoConfirmarContrasena;

    public VentanaCambiarContrasena(ControladorUsuario controladorUsuario) {
        this.controladorUsuario = controladorUsuario;
    }
}