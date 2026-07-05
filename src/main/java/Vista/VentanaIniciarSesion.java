VentanaIniciarSesion.java
        1
        100 %
        package Vista;

import javax.swing.*;
import java.awt.*;

/**
 * Vista para el inicio de sesión.
 * Siguiendo el MVC puro, esta clase NO conoce a los controladores.
 * Solo se encarga de la interfaz y de exponer los datos y componentes necesarios.
 */
public class VentanaIniciarSesion extends JFrame {
    private JTextField campoMatricula;
    private JPasswordField campoContrasena;
    private JButton botonLogin;

    public VentanaIniciarSesion() {
        setTitle("CLARA - Iniciar Sesión");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        ComponentesUI.configurarFondo(this);

        // --- PANEL SUPERIOR ---
        JPanel panelSuperior = ComponentesUI.crearPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));

        JLabel titulo = ComponentesUI.crearTitulo("CLARA");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSuperior.add(titulo);

        JLabel subtitulo = ComponentesUI.crearSubtitulo("Gestión de Agrupaciones");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSuperior.add(subtitulo);

        add(panelSuperior, BorderLayout.NORTH);

        // --- PANEL CENTRAL ---
        JPanel panelCentral = ComponentesUI.crearPanel();
        panelCentral.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 5, 20);

        panelCentral.add(ComponentesUI.crearEtiqueta("Matrícula:"), gbc);
        campoMatricula = ComponentesUI.crearCampoTexto();
        panelCentral.add(campoMatricula, gbc);

        gbc.insets = new Insets(15, 20, 5, 20);
        panelCentral.add(ComponentesUI.crearEtiqueta("Contraseña:"), gbc);
        campoContrasena = ComponentesUI.crearCampoContrasena();
        panelCentral.add(campoContrasena, gbc);

        add(panelCentral, BorderLayout.CENTER);

        // --- PANEL INFERIOR ---
        JPanel panelInferior = ComponentesUI.crearPanel();
        panelInferior.setBorder(BorderFactory.createEmptyBorder(20, 50, 60, 50));
        panelInferior.setLayout(new BorderLayout());

        botonLogin = ComponentesUI.crearBoton("Ingresar", null); // El listener se asigna desde el controlador
        panelInferior.add(botonLogin, BorderLayout.CENTER);

        add(panelInferior, BorderLayout.SOUTH);
    }

    // Métodos para que el controlador obtenga los datos
    public String getMatricula() {
        return campoMatricula.getText().trim();
    }

    public String getContrasena() {
        return new String(campoContrasena.getPassword());
    }

    public JButton getBotonIngresar() {
        return botonLogin;
    }

    public void limpiarContrasena() {
        campoContrasena.setText("");
    }
}