package Vista;
import javax.swing.*;
import java.awt.*;

import Controlador.ControladorAgrupacion;
import Controlador.ControladorFinanzas;
import Controlador.ControladorUsuario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaMenuAdmin extends JFrame{
    private ControladorUsuario controladorUsuario;
    private ControladorFinanzas controladorFinanzas;
    private ControladorAgrupacion controladorAgrupacion;

    public VentanaMenuAdmin(ControladorUsuario controladorUsuario, ControladorFinanzas controladorFinanzas, ControladorAgrupacion controladorAgrupacion) {
        this.controladorUsuario = controladorUsuario;
        this.controladorFinanzas = controladorFinanzas;
        this.controladorAgrupacion = controladorAgrupacion;
        String nombreUsuario = controladorUsuario.getUsuarioActivo().getNombre();

        setTitle("CLARA - Menú Administrador");
        setSize(400, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(102, 133, 183));

        JLabel titulo = new JLabel("CLARA");
        titulo.setFont(new Font("ROG Fonts Normal", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(20, 10, 150, 35);
        add(titulo);

        JLabel saludo = new JLabel("Hola, " + nombreUsuario);
        saludo.setFont(new Font("Arial", Font.PLAIN, 14));
        saludo.setForeground(Color.WHITE);
        saludo.setBounds(20, 45, 300, 25);
        add(saludo);

        JLabel etiquetaRol = new JLabel("ADMINISTRADOR");
        etiquetaRol.setFont(new Font("Arial", Font.BOLD, 11));
        etiquetaRol.setForeground(new Color(200, 220, 255));
        etiquetaRol.setBounds(20, 65, 200, 20);
        add(etiquetaRol);

        JLabel seccionUsuarios = new JLabel("USUARIOS");
        seccionUsuarios.setFont(new Font("Arial", Font.BOLD, 11));
        seccionUsuarios.setForeground(Color.BLACK);
        seccionUsuarios.setBounds(20, 105, 200, 20);
        add(seccionUsuarios);

        JButton botonRegistrarUsuario = new JButton("Registrar nuevo usuario");
        botonRegistrarUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        botonRegistrarUsuario.setBackground(Color.WHITE);
        botonRegistrarUsuario.setBounds(20, 125, 350, 45);
        botonRegistrarUsuario.setHorizontalAlignment(JButton.CENTER);
        botonRegistrarUsuario.addActionListener(e -> new VentanaRegistrarUsuario(controladorUsuario));
        add(botonRegistrarUsuario);

        JButton botonGestionarUsuario = new JButton("Gestionar Usuarios");
        botonGestionarUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        botonGestionarUsuario.setBackground(Color.WHITE);
        botonGestionarUsuario.setBounds(20, 175, 350, 45);
        botonGestionarUsuario.setHorizontalAlignment(JButton.CENTER);
        botonRegistrarUsuario.addActionListener(e -> new VentanaGestionarUsuario(controladorUsuario));
        add(botonGestionarUsuario);

        JLabel seccionAgrupaciones = new JLabel("AGRUPACIONES");
        seccionAgrupaciones.setFont(new Font("Arial", Font.BOLD, 11));
        seccionAgrupaciones.setForeground(Color.BLACK);
        seccionAgrupaciones.setBounds(20, 250, 200, 20);
        add(seccionAgrupaciones);

        JButton botonCrearAgrupacion = new JButton("Crear nueva agrupación");
        botonCrearAgrupacion.setFont(new Font("Arial", Font.PLAIN, 14));
        botonCrearAgrupacion.setBackground(Color.WHITE);
        botonCrearAgrupacion.setBounds(20, 270, 350, 45);
        botonCrearAgrupacion.setHorizontalAlignment(JButton.CENTER);
        botonCrearAgrupacion.addActionListener(e -> new VentanaCrearAgrupacion(controladorAgrupacion));
        add(botonCrearAgrupacion);

        JButton botonGestionarMiembros = new JButton("Gestionar miembros de agrupación");
        botonGestionarMiembros.setFont(new Font("Arial", Font.PLAIN, 14));
        botonGestionarMiembros.setBackground(Color.WHITE);
        botonGestionarMiembros.setBounds(20, 320, 350, 45);
        botonGestionarMiembros.setHorizontalAlignment(JButton.CENTER);
        botonGestionarMiembros.addActionListener(e -> new VentanaGestionarMiembros(controladorAgrupacion, controladorUsuario));
        add(botonGestionarMiembros);

        JButton botonGestionarAgrupacion = new JButton("Gestionar agrupación");
        botonGestionarAgrupacion.setFont(new Font("Arial", Font.PLAIN, 14));
        botonGestionarAgrupacion.setBackground(Color.WHITE);
        botonGestionarAgrupacion.setBounds(20, 370, 350, 45);
        botonGestionarAgrupacion.setHorizontalAlignment(JButton.CENTER);
        botonGestionarAgrupacion.addActionListener(e -> new VentanaGestionarAgrupacion(controladorAgrupacion));
        add(botonGestionarAgrupacion);

        JLabel seccionPersonal = new JLabel("PERSONAL");
        seccionPersonal.setFont(new Font("Arial", Font.BOLD, 11));
        seccionPersonal.setForeground(Color.BLACK);
        seccionPersonal.setBounds(20, 440, 200, 20);
        add(seccionPersonal);

        JButton botonCambiarContrasena = new JButton("Cambiar contraseña");
        botonCambiarContrasena.setFont(new Font("Arial", Font.PLAIN, 14));
        botonCambiarContrasena.setBackground(Color.WHITE);
        botonCambiarContrasena.setBounds(20, 460, 350, 45);
        botonCambiarContrasena.setHorizontalAlignment(JButton.CENTER);
        botonCambiarContrasena.addActionListener(e -> new VentanaCambiarContrasena(controladorUsuario));
        add(botonCambiarContrasena);

        JButton botonCerrarSesion = new JButton("Cerrar sesión");
        botonCerrarSesion.setFont(new Font("Arial", Font.BOLD, 14));
        botonCerrarSesion.setBackground(new Color(220, 50, 50));
        botonCerrarSesion.setForeground(Color.WHITE);
        botonCerrarSesion.setBounds(20, 560, 350, 45);
        botonCerrarSesion.setFocusPainted(false);
        botonCerrarSesion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controladorUsuario.cerrarSesion();
                dispose();
                new VentanaIniciarSesion(controladorUsuario, controladorFinanzas, controladorAgrupacion);
            }
        });
        add(botonCerrarSesion);
        setVisible(true);
    }
}
