package Vista;

import Controlador.ControladorUsuario;

import javax.swing.*;
import java.awt.*;

public class VentanaCambiarContrasena extends JFrame {
    private ControladorUsuario controladorUsuario;
    private JPasswordField campoContrasenaActual;
    private JPasswordField campoNuevaContrasena;
    private JPasswordField campoConfirmarContrasena;

    public VentanaCambiarContrasena(ControladorUsuario controladorUsuario) {
        this.controladorUsuario = controladorUsuario;

        setTitle("CLARA - Cambiar Contraseña");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        ComponentesUI.configurarFondo(this);

        JPanel panelSuperior = ComponentesUI.crearPanel();
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panelSuperior.add(ComponentesUI.crearTitulo("Cambiar Contraseña"));
        add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCentro = ComponentesUI.crearPanel();
        panelCentro.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 40, 8, 40);

        panelCentro.add(ComponentesUI.crearEtiqueta("Contraseña Actual:"), gbc);
        campoContrasenaActual = ComponentesUI.crearCampoContrasena();
        panelCentro.add(campoContrasenaActual, gbc);

        panelCentro.add(ComponentesUI.crearEtiqueta("Nueva Contraseña:"), gbc);
        campoNuevaContrasena = ComponentesUI.crearCampoContrasena();
        panelCentro.add(campoNuevaContrasena, gbc);

        panelCentro.add(ComponentesUI.crearEtiqueta("Confirmar Nueva Contraseña:"), gbc);
        campoConfirmarContrasena = ComponentesUI.crearCampoContrasena();
        panelCentro.add(campoConfirmarContrasena, gbc);

        add(panelCentro, BorderLayout.CENTER);

        JPanel panelInferior = ComponentesUI.crearPanel();
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 40, 30, 40));
        panelInferior.setLayout(new GridLayout(1, 2, 10, 0));

        JButton btnGuardar = ComponentesUI.crearBoton("Guardar Cambios",
                e -> cambiarContrasena());
        JButton btnCancelar = ComponentesUI.crearBotonPeligro("Cancelar",
                e -> dispose());

        panelInferior.add(btnGuardar);
        panelInferior.add(btnCancelar);

        add(panelInferior, BorderLayout.SOUTH);

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