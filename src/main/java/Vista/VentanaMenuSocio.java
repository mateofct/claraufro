package Vista;

import javax.swing.*;
import java.awt.*;

import Controlador.ControladorAgrupacion;
import Controlador.ControladorFinanzas;
import Controlador.ControladorUsuario;

public class VentanaMenuSocio extends JFrame {
    private ControladorUsuario controladorUsuario;
    private ControladorFinanzas controladorFinanzas;
    private ControladorAgrupacion controladorAgrupacion;

    public VentanaMenuSocio(ControladorUsuario controladorUsuario, ControladorFinanzas controladorFinanzas, ControladorAgrupacion controladorAgrupacion) {
        String nombreUsuario = controladorUsuario.getUsuarioActivo().getNombre();
        this.controladorUsuario = controladorUsuario;
        this.controladorFinanzas = controladorFinanzas;
        this.controladorAgrupacion = controladorAgrupacion;

        setTitle("CLARA - Menu Socio");
        setSize(400, 500); // Ajustado un poco para dar espacio al nuevo layout dinámico
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Usamos BorderLayout para organizar la ventana en regiones, igual que en el Admin
        setLayout(new BorderLayout());
        ComponentesUI.configurarFondo(this);

        // --- PANEL SUPERIOR (Encabezado) ---
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

        // --- PANEL CENTRAL (Opciones) ---
        JPanel PanelCentral = ComponentesUI.crearPanel();
        PanelCentral.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 40, 5, 40);

        // Sección Consultas
        PanelCentral.add(crearSeparador("CONSULTAS"), gbc);
        PanelCentral.add(ComponentesUI.crearBoton("Ver saldo de mi agrupación", e -> new VentanaSaldo(controladorFinanzas, controladorUsuario.getUsuarioActivo())), gbc);
        PanelCentral.add(ComponentesUI.crearBoton("Ver historial de movimientos", e -> new VentanaHistorial(controladorFinanzas, controladorUsuario.getUsuarioActivo())), gbc);
        PanelCentral.add(Box.createVerticalStrut(15), gbc);

        // Sección Personal
        PanelCentral.add(crearSeparador("PERSONAL"), gbc);
        PanelCentral.add(ComponentesUI.crearBoton("Cambiar contraseña", e -> new VentanaCambiarContrasena(controladorUsuario)), gbc);
        add(PanelCentral, BorderLayout.CENTER);

        // --- PANEL INFERIOR (Cerrar Sesión) ---
        JPanel panelInferior = ComponentesUI.crearPanel();
        panelInferior.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));
        panelInferior.setLayout(new BorderLayout());

        JButton botonCerrarSesion = ComponentesUI.crearBotonPeligro("Cerrar Sesión", e -> {
            controladorUsuario.cerrarSesion();
            dispose();
            new VentanaIniciarSesion(controladorUsuario, controladorFinanzas, controladorAgrupacion);
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
        l.setForeground(Color.BLACK); // Puedes cambiar a Color.GRAY si prefieres el estilo original
        p.add(l);
        return p;
    }
}