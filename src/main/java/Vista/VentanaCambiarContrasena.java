package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Vista para cambiar la contraseña.
 * MVC Puro: No conoce al controlador.
 */
public class VentanaCambiarContrasena extends JFrame {
    private JPasswordField campoContrasenaActual;
    private JPasswordField campoNuevaContrasena;
    private JPasswordField campoConfirmarContrasena;
    private JButton btnGuardar;

    public VentanaCambiarContrasena() {
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

        btnGuardar = ComponentesUI.crearBoton("Guardar Cambios", null);
        JButton btnCancelar = ComponentesUI.crearBotonPeligro("Cancelar", e -> dispose());

        panelInferior.add(btnGuardar);
        panelInferior.add(btnCancelar);

        add(panelInferior, BorderLayout.SOUTH);
    }

    public String getContrasenaActual() {
        return new String(campoContrasenaActual.getPassword());
    }

    public String getNuevaContrasena() {
        return new String(campoNuevaContrasena.getPassword());
    }

    public String getConfirmacionContrasena() {
        return new String(campoConfirmarContrasena.getPassword());
    }

    public void setGuardarListener(ActionListener listener) {
        btnGuardar.addActionListener(listener);
    }
}
