package Vista;

import javax.swing.*;
import java.awt.*;
import Controlador.ControladorPrincipal;
import Controlador.ControladorUsuario;
import Controlador.ControladorFinanzas;

/**
 * Menú principal de la aplicación para usuarios con rol de socio.
 * <p>
 * Según el patrón MVC puro, esta clase no contiene lógica de negocio.
 * Recibe una referencia al {@link ControladorPrincipal} para gestionar
 * la navegación entre ventanas, y al {@link ControladorUsuario} para
 * obtener el nombre del usuario activo.
 * </p>
 * <p>
 * A diferencia del administrador y el tesorero, el socio tiene un conjunto
 * limitado de opciones: solo puede consultar información financiera
 * (saldo e historial de su agrupación) y cambiar su contraseña.
 * </p>
 *
 * <h3>Secciones del menú:</h3>
 * <table border="1" cellpadding="4" cellspacing="0">
 *   <caption>Opciones del socio</caption>
 *   <tr><th>Sección</th><th>Opciones</th></tr>
 *   <tr>
 *     <td><strong>Consultas</strong></td>
 *     <td>Ver saldo de mi agrupación, Ver historial de movimientos</td>
 *   </tr>
 *   <tr>
 *     <td><strong>Personal</strong></td>
 *     <td>Cambiar contraseña</td>
 *   </tr>
 * </table>
 *
 * @author CLARA Team
 * @version 1.0
 * @see Controlador.ControladorPrincipal
 */
public class VentanaMenuSocio extends JFrame {

    /**
     * Referencia al controlador principal para gestionar la navegación
     * entre ventanas.
     */
    private ControladorPrincipal controladorPrincipal;

    /**
     * Constructor que construye y configura la ventana del menú del socio.
     * <p>
     * Muestra un saludo con el nombre del usuario y su rol, seguido de las
     * opciones organizadas en secciones de consultas y personal.
     * Cada botón invoca directamente un método del controlador principal.
     * </p>
     *
     * @param cp el controlador principal que orquesta la navegación
     * @param cu el controlador de usuarios para obtener el nombre del activo
     * @param cf el controlador de finanzas (recibido pero no usado directamente
     *        en esta vista)
     */
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

    /**
     * Crea un panel separador con una etiqueta de texto en negrita
     * utilizada como título de sección en el menú.
     *
     * @param texto el texto del separador
     * @return el panel con la etiqueta de sección
     */
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
