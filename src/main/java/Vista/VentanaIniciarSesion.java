package Vista;

import Vista.ComponentesUI;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana gráfica para el inicio de sesión en el sistema CLARA.
 * <p>
 * Según el patrón MVC puro, esta clase no conoce a los controladores.
 * Se encarga exclusivamente de la interfaz gráfica y de exponer los datos
 * ingresados por el usuario y los componentes necesarios para que el
 * controlador principal pueda interactuar con ella.
 * </p>
 *
 * <h3>Elementos de la interfaz:</h3>
 * <ul>
 *   <li>Panel superior: título "CLARA" y subtítulo "Gestión de Agrupaciones".</li>
 *   <li>Panel central: campos de texto para matrícula y contraseña.</li>
 *   <li>Panel inferior: botón "Ingresar" cuyo listener se asigna desde el controlador.</li>
 * </ul>
 *
 * @author CLARA Team
 * @version 1.0
 * @see Controlador.ControladorPrincipal#mostrarLogin()
 */
public class VentanaIniciarSesion extends JFrame {

    /**
     * Campo de texto donde el usuario ingresa su matrícula.
     */
    private JTextField campoMatricula;

    /**
     * Campo de contraseña donde el usuario ingresa su contraseña.
     */
    private JPasswordField campoContrasena;

    /**
     * Botón "Ingresar" que dispara el proceso de autenticación.
     */
    private JButton botonLogin;

    /**
     * Constructor que construye y configura la ventana de inicio de sesión.
     * <p>
     * Configura el título, tamaño, comportamiento de cierre y posición
     * centrada en pantalla. Utiliza {@link ComponentesUI} para garantizar
     * un diseño visual consistente con el resto de la aplicación.
     * </p>
     */
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

    /**
     * Obtiene el texto de la matrícula ingresado por el usuario,
     * eliminando espacios en blanco extremos.
     *
     * @return la matrícula ingresada
     */
    public String getMatricula() {
        return campoMatricula.getText().trim();
    }

    /**
     * Obtiene la contraseña ingresada por el usuario como cadena de texto.
     *
     * @return la contraseña ingresada
     */
    public String getContrasena() {
        return new String(campoContrasena.getPassword());
    }

    /**
     * Obtiene una referencia al botón "Ingresar" para que el controlador
     * pueda asignarle un {@link // ActionListener}.
     *
     * @return el botón de inicio de sesión
     */
    public JButton getBotonIngresar() {
        return botonLogin;
    }

    /**
     * Limpia el campo de contraseña tras un intento de login fallido.
     */
    public void limpiarContrasena() {
        campoContrasena.setText("");
    }
}