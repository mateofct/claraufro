package Vista;

import javax.swing.*;
import java.awt.*;
import Controlador.ControladorUsuario;
import Controlador.ControladorFinanzas;
import Controlador.ControladorAgrupacion;

public class VentanaMenuAdmin extends JFrame {
    private ControladorUsuario controladorUsuario;
    private ControladorFinanzas controladorFinanzas;
    private ControladorAgrupacion controladorAgrupacion;

    public VentanaMenuAdmin(ControladorUsuario controladorUsuario, ControladorFinanzas controladorFinanzas, ControladorAgrupacion controladorAgrupacion) {
        this.controladorUsuario = controladorUsuario;
        this.controladorFinanzas = controladorFinanzas;
        this.controladorAgrupacion = controladorAgrupacion;

        String nombreUsuario = controladorUsuario.getUsuarioActivo().getNombre();

        setTitle("CLARA - Menú Administrador");
        setSize(500, 650);
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

        JLabel etiquetaRol = new JLabel("ADMINISTRADOR");
        etiquetaRol.setFont(new Font("Arial", Font.BOLD, 12));
        etiquetaRol.setForeground(new Color(200, 220, 255));
        etiquetaRol.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSuperior.add(etiquetaRol);

        add(panelSuperior, BorderLayout.NORTH);

        // --- PANEL CENTRAL ---
        JPanel panelCentral = ComponentesUI.crearPanel();
        panelCentral.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 40, 5, 40);

        // Sección Usuarios
        panelCentral.add(crearSeparador("USUARIOS"), gbc);
        panelCentral.add(ComponentesUI.crearBoton("Registrar usuario", e -> new VentanaCrearUsuario(controladorUsuario)), gbc);
        panelCentral.add(ComponentesUI.crearBoton("Gestionar usuario", e -> new VentanaGestionarUsuario(controladorUsuario)), gbc);

        panelCentral.add(Box.createVerticalStrut(15), gbc);

        // Sección Agrupaciones
        panelCentral.add(crearSeparador("AGRUPACIONES"), gbc);
        panelCentral.add(ComponentesUI.crearBoton("Registrar agrupación", e -> new VentanaCrearAgrupacion(controladorAgrupacion)), gbc);
        panelCentral.add(ComponentesUI.crearBoton("Gestionar agrupación", e -> new VentanaGestionarAgrupacion(controladorAgrupacion)), gbc);
        panelCentral.add(ComponentesUI.crearBoton("Gestionar miembros de agrupación", e -> new VentanaGestionarMiembros(controladorAgrupacion, controladorUsuario)), gbc);

        panelCentral.add(Box.createVerticalStrut(15), gbc);

        // Sección Personal
        panelCentral.add(crearSeparador("PERSONAL"), gbc);
        panelCentral.add(ComponentesUI.crearBoton("Cambiar contraseña", e -> new VentanaCambiarContrasena(controladorUsuario)), gbc);

        add(panelCentral, BorderLayout.CENTER);

        // --- PANEL INFERIOR ---
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
        l.setForeground(Color.BLACK);
        p.add(l);
        return p;
    }
}
