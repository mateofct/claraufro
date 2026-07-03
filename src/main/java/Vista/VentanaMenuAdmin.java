package Vista;

import javax.swing.*;
import java.awt.*;
import Controlador.ControladorAgrupacion;
import Controlador.ControladorFinanzas;
import Controlador.ControladorUsuario;

public class VentanaMenuAdmin extends JFrame {
    private ControladorUsuario controladorUsuario;
    private ControladorFinanzas controladorFinanzas;
    private ControladorAgrupacion controladorAgrupacion;

    public VentanaMenuAdmin(ControladorUsuario controladorUsuario, ControladorFinanzas controladorFinanzas, ControladorAgrupacion controladorAgrupacion) {
        this.controladorUsuario = controladorUsuario;
        this.controladorFinanzas = controladorFinanzas;
        this.controladorAgrupacion = controladorAgrupacion;

        setTitle("CLARA - Panel de Administración");
        setSize(450, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Usamos BorderLayout para organizar la ventana en regiones
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

        JLabel saludo = ComponentesUI.crearEtiqueta("Hola, " + controladorUsuario.getUsuarioActivo().getNombre());
        saludo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSuperior.add(saludo);

        JLabel rol = new JLabel("ADMINISTRADOR");
        rol.setFont(new Font("Arial", Font.BOLD, 12));
        rol.setForeground(new Color(200, 220, 255));
        rol.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSuperior.add(rol);

        add(panelSuperior, BorderLayout.NORTH);

        JPanel PanelCentral = ComponentesUI.crearPanel();
        PanelCentral.setLayout(new GridBagLayout()); // GridBagLayout permite centrar un bloque de componentes
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 40, 5, 40);

        PanelCentral.add(crearSeparador("USUARIOS"), gbc);
        PanelCentral.add(ComponentesUI.crearBoton("Registrar nuevo usuario",
                e -> new VentanaCrearUsuario(controladorUsuario)), gbc);
        PanelCentral.add(ComponentesUI.crearBoton("Gestionar Usuarios",
                e -> new VentanaGestionarUsuario(controladorUsuario)), gbc);
        PanelCentral.add(Box.createVerticalStrut(15), gbc);

        PanelCentral.add(crearSeparador("AGRUPACIONES"), gbc);
        PanelCentral.add(ComponentesUI.crearBoton("Registrar agrupación",
                e -> new VentanaCrearAgrupacion(controladorAgrupacion)), gbc);
        PanelCentral.add(ComponentesUI.crearBoton("Gestionar miembros",
                e -> new VentanaGestionarMiembros(controladorAgrupacion, controladorUsuario)), gbc);
        PanelCentral.add(ComponentesUI.crearBoton("Gestionar agrupaciones",
                e -> new VentanaGestionarAgrupacion(controladorAgrupacion)), gbc);
        PanelCentral.add(Box.createVerticalStrut(15), gbc);

        PanelCentral.add(crearSeparador("PERSONAL"), gbc);
        PanelCentral.add(ComponentesUI.crearBoton("Cambiar contraseña",
                e -> new VentanaCambiarContrasena(controladorUsuario)), gbc);
        add(PanelCentral, BorderLayout.CENTER);

        JPanel panelInferior = ComponentesUI.crearPanel();
        panelInferior.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));
        panelInferior.setLayout(new BorderLayout());

        JButton botonCerrarSesion = ComponentesUI.crearBotonPeligro("Cerrar Sesión",
                e -> {
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