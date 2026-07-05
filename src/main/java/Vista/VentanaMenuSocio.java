package Vista;

import javax.swing.*;
import java.awt.*;
import Controlador.ControladorPrincipal;
import Controlador.ControladorUsuario;
import Controlador.ControladorFinanzas;

/**
 * Menú principal para el Socio.
 */
public class VentanaMenuSocio extends JFrame {
    private ControladorPrincipal controladorPrincipal;

    public VentanaMenuSocio(ControladorPrincipal cp, ControladorUsuario cu, ControladorFinanzas cf) {
        this.controladorPrincipal = cp;
        String nombreUsuario = cu.getUsuarioActivo().getNombre();

        setTitle("CLARA - Menú Socio");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        ComponentesUI.configurarFondo(this);

        // --- PANEL SUPERIOR ---
        JPanel panelSuperior = ComponentesUI.crearPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = ComponentesUI.crearTitulo("CLARA");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSuperior.add(titulo);
        panelSuperior.add(Box.createVerticalStrut(10));

        JLabel saludo = ComponentesUI.crearEtiqueta("Hola, " + nombreUsuario);
        saludo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSuperior.add(saludo);

        JLabel etiquetaRol = new JLabel("SOCIO");
        etiquetaRol.setFont(new Font("Arial", Font.BOLD, 12));
        etiquetaRol.setForeground(new Color(200, 220, 255));
        etiquetaRol.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSuperior.add(etiquetaRol);

        add(panelSuperior, BorderLayout.NORTH);

        // --- PANEL CENTRAL ---
        JPanel PanelCentral = ComponentesUI.crearPanel();
        PanelCentral.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 40, 5, 40);

        // Sección Consultas
        PanelCentral.add(crearSeparador("CONSULTAS"), gbc);
        PanelCentral.add(ComponentesUI.crearBoton("Ver saldo de mi agrupación", e -> controladorPrincipal.mostrarVerSaldo()), gbc);
        PanelCentral.add(ComponentesUI.crearBoton("Ver historial de movimientos", e -> controladorPrincipal.mostrarHistorial()), gbc);
        PanelCentral.add(Box.createVerticalStrut(15), gbc);

        // Sección Personal
        PanelCentral.add(crearSeparador("PERSONAL"), gbc);
        PanelCentral.add(ComponentesUI.crearBoton("Cambiar contraseña", e -> controladorPrincipal.mostrarCambiarContrasena()), gbc);
        add(PanelCentral, BorderLayout.CENTER);

        // --- PANEL INFERIOR ---
        JPanel panelInferior = ComponentesUI.crearPanel();
        panelInferior.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));
        panelInferior.setLayout(new BorderLayout());

        JButton botonCerrarSesion = ComponentesUI.crearBotonPeligro("Cerrar Sesión", e -> {
            dispose();
            controladorPrincipal.cerrarSesion();
        });

        panelInferior.add(botonCerrarSesion, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel crearSeparador(String texto) {
        JPanel p = ComponentesUI.crearPanel();
        p.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Arial", Font.BOLD, 11));
        l.setForeground(Color.BLACK);
        p.add(l);
        return p;
    }
}