package Vista;

import Controlador.ControladorUsuario;

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

        setTitle("CLARA - Cambiar Contraseña");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(102, 133, 183));

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridLayout(0, 1, 10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(new Color(102, 133, 183));

        JLabel titulo = new JLabel("Cambiar Contraseña", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        panelPrincipal.add(titulo);

        panelPrincipal.add(ComponentesUI.crearEtiqueta("Contraseña Actual:"));
        campoContrasenaActual = ComponentesUI.crearCampoContrasena();
        panelPrincipal.add(campoContrasenaActual);

        panelPrincipal.add(ComponentesUI.crearEtiqueta("Nueva Contraseña:"));
        campoNuevaContrasena = ComponentesUI.crearCampoContrasena();
        panelPrincipal.add(campoNuevaContrasena);

        panelPrincipal.add(ComponentesUI.crearEtiqueta("Confirmar Nueva Contraseña:"));
        campoConfirmarContrasena = ComponentesUI.crearCampoContrasena();
        panelPrincipal.add(campoConfirmarContrasena);

        JButton botonCambiar = ComponentesUI.crearBoton("Cambiar Contraseña",
                e -> cambiarContrasena());
        panelPrincipal.add(botonCambiar);
        add(panelPrincipal, BorderLayout.CENTER);
        setVisible(true);
    }

    private void cambiarContrasena() {
        String contrasenaActual = new String(campoContrasenaActual.getPassword());
        String nuevaContrasena = new String(campoNuevaContrasena.getPassword());
        String confirmarContrasena = new String(campoConfirmarContrasena.getPassword());

        if (contrasenaActual.isEmpty() || nuevaContrasena.isEmpty() || confirmarContrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!nuevaContrasena.equals(confirmarContrasena)) {
            JOptionPane.showMessageDialog(this, "La nueva contraseña y su confirmación no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            controladorUsuario.cambiarPropiaContrasena(contrasenaActual, nuevaContrasena);
            JOptionPane.showMessageDialog(this, "Contraseña cambiada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}