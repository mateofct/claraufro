package Vista;

import javax.swing.*;
import java.awt.*;
import Controlador.ControladorUsuario;
import Controlador.ControladorFinanzas;
import Controlador.ControladorAgrupacion;
import Modelo.Usuario;

public class VentanaIniciarSesion extends JFrame {
    private ControladorUsuario controladorUsuario;
    private ControladorFinanzas controladorFinanzas;
    private ControladorAgrupacion controladorAgrupacion;
    private JTextField campoMatricula;
    private JPasswordField campoContrasena;

    public VentanaIniciarSesion(ControladorUsuario controladorUsuario, ControladorFinanzas controladorFinanzas, ControladorAgrupacion controladorAgrupacion) {
        this.controladorUsuario = controladorUsuario;
        this.controladorFinanzas = controladorFinanzas;
        this.controladorAgrupacion = controladorAgrupacion;

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

        JButton botonLogin = ComponentesUI.crearBoton("Ingresar", e -> intentarLogin());
        panelInferior.add(botonLogin, BorderLayout.CENTER);

        add(panelInferior, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void intentarLogin() {
        String matricula = campoMatricula.getText().trim();
        String contrasena = new String(campoContrasena.getPassword());

        if (matricula.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Los campos no deben de estar vacíos", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (controladorUsuario.iniciarSesion(matricula, contrasena)) {
            Usuario activo = controladorUsuario.getUsuarioActivo();
            dispose();
            switch (activo.getRol()) {
                case ADMIN:
                    new VentanaMenuAdmin(controladorUsuario, controladorFinanzas, controladorAgrupacion);
                    break;
                case TESORERO:
                    new VentanaMenuTesorero(controladorUsuario, controladorFinanzas, controladorAgrupacion);
                    break;
                case SOCIO:
                    new VentanaMenuSocio(controladorUsuario, controladorFinanzas, controladorAgrupacion);
                    break;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Matrícula o contraseña incorrectos.", "Error de acceso", JOptionPane.ERROR_MESSAGE);
            campoContrasena.setText("");
        }
    }
}
