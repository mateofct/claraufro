package Vista;
import javax.swing.*;
import java.awt.*;
import Controlador.ControladorFinanzas;
import Controlador.ControladorUsuario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaMenuTesorero extends JFrame{

    private ControladorUsuario controladorUsuario;
    private ControladorFinanzas controladorFinanzas;

    public VentanaMenuTesorero(ControladorUsuario controladorUsuario, ControladorFinanzas controladorFinanzas) {
        this.controladorUsuario = controladorUsuario;
        this.controladorFinanzas = controladorFinanzas;
        String nombreUsuario = controladorUsuario.getUsuarioActivo().getNombre();

        setTitle("CLARA - Menu Tesorero");
        setSize(400, 500);
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

        JLabel etiquetaRol = new JLabel("TESORERO");
        etiquetaRol.setFont(new Font("Arial", Font.BOLD, 11));
        etiquetaRol.setForeground(new Color(200, 220, 255));
        etiquetaRol.setBounds(20, 65, 200, 20);
        add(etiquetaRol);

        JLabel seccionMovimientos = new JLabel("MOVIMIENTOS");
        seccionMovimientos.setFont(new Font("Arial", Font.BOLD, 11));
        seccionMovimientos.setForeground(Color.GRAY);
        seccionMovimientos.setBounds(20, 105, 200, 20);
        add(seccionMovimientos);

        JButton botonIngreso = new JButton("Registrar ingreso");
        botonIngreso.setFont(new Font("Arial", Font.PLAIN, 14));
        botonIngreso.setBackground(Color.WHITE);
        botonIngreso.setForeground(new Color(29, 130, 80));
        botonIngreso.setBounds(20, 128, 350, 45);
        botonIngreso.setHorizontalAlignment(JButton.LEFT);
        botonIngreso.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new VentanaRegistrarMovimiento(controladorFinanzas, controladorUsuario.getUsuarioActivo(), true);
            }
        });
        add(botonIngreso);

        JButton botonEgreso = new JButton("Registrar egreso");
        botonEgreso.setFont(new Font("Arial", Font.PLAIN, 14));
        botonEgreso.setBackground(Color.WHITE);
        botonEgreso.setForeground(new Color(200, 50, 50));
        botonEgreso.setBounds(20, 176, 350, 45);
        botonEgreso.setFocusPainted(false);
        botonEgreso.setHorizontalAlignment(JButton.LEFT);
        botonEgreso.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new VentanaRegistrarMovimiento(controladorFinanzas, controladorUsuario.getUsuarioActivo(), false);
            }
        });
        add(botonEgreso);

        JLabel seccionConsultas = new JLabel("CONSULTAS");
        seccionConsultas.setFont(new Font("Arial", Font.BOLD, 11));
        seccionConsultas.setForeground(Color.GRAY);
        seccionConsultas.setBounds(20, 235, 200, 20);
        add(seccionConsultas);

        JButton botonVerSaldo = new JButton("Ver saldo actual");
        botonVerSaldo.setFont(new Font("Arial", Font.PLAIN, 14));
        botonVerSaldo.setBackground(Color.WHITE);
        botonVerSaldo.setBounds(20, 258, 350, 45);
        botonVerSaldo.setFocusPainted(false);
        botonVerSaldo.setHorizontalAlignment(JButton.LEFT);
        botonVerSaldo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new VentanaSaldo(controladorFinanzas, controladorUsuario.getUsuarioActivo());
            }
        });
        add(botonVerSaldo);

        JButton botonHistorial = new JButton("Ver historial de movimientos");
        botonHistorial.setFont(new Font("Arial", Font.PLAIN, 14));
        botonHistorial.setBackground(Color.WHITE);
        botonHistorial.setBounds(20, 306, 350, 45);
        botonHistorial.setHorizontalAlignment(JButton.LEFT);
        botonHistorial.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new VentanaHistorial(controladorFinanzas, controladorUsuario.getUsuarioActivo());
            }
        });
        add(botonHistorial);

        JButton botonCerrarSesion = new JButton("Cerrar sesion");
        botonCerrarSesion.setFont(new Font("Arial", Font.BOLD, 14));
        botonCerrarSesion.setBackground(new Color(220, 50, 50));
        botonCerrarSesion.setForeground(Color.WHITE);
        botonCerrarSesion.setBounds(20, 410, 350, 45);
        botonCerrarSesion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controladorUsuario.cerrarSesion();
                dispose();
                new VentanaIniciarSesion(controladorUsuario, controladorFinanzas);
            }
        });
        add(botonCerrarSesion);
        setVisible(true);



    }
}
