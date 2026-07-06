package Vista;

import javax.swing.*;
import java.awt.*;
import Controlador.ControladorPrincipal;
import Controlador.ControladorUsuario;
import Controlador.ControladorFinanzas;

/**
 * Menú principal de la aplicación para usuarios con rol de tesorero.
 * <p>
 * Según el patrón MVC puro, esta clase no contiene lógica de negocio.
 * Recibe una referencia al {@link ControladorPrincipal} para gestionar
 * la navegación entre ventanas, al {@link ControladorUsuario} para obtener
 * el nombre del usuario activo, y al {@link ControladorFinanzas} (que
 * aunque se recibe no se utiliza directamente en esta vista).
 * </p>
 *
 * <h3>Secciones del menú:</h3>
 * <table border="1" cellpadding="4" cellspacing="0">
 *   <caption>Opciones del tesorero</caption>
 *   <tr><th>Sección</th><th>Opciones</th></tr>
 *   <tr>
 *     <td><strong>Consultas</strong></td>
 *     <td>Ver saldo de la agrupación, Ver historial de movimientos</td>
 *   </tr>
 *   <tr>
 *     <td><strong>Movimientos</strong></td>
 *     <td>Registrar ingreso (botón verde), Registrar egreso (botón rojo)</td>
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
public class VentanaMenuTesorero extends JFrame {

    /**
     * Referencia al controlador principal para gestionar la navegación
     * entre ventanas.
     */
    private ControladorPrincipal controladorPrincipal;

    /**
     * Constructor que construye y configura la ventana del menú del tesorero.
     * <p>
     * Muestra un saludo con el nombre del usuario y su rol, seguido de las
     * opciones organizadas en secciones de consultas, movimientos y personal.
     * Cada botón invoca directamente un método del controlador principal.
     * </p>
     *
     * @param cp el controlador principal que orquesta la navegación
     * @param cu el controlador de usuarios para obtener el nombre del activo
     * @param cf el controlador de finanzas (recibido pero no usado directamente
     *        en esta vista)
     */
    public VentanaMenuTesorero(ControladorPrincipal cp, ControladorUsuario cu, ControladorFinanzas cf) {
        this.controladorPrincipal = cp;

        String nombreUsuario = cu.getUsuarioActivo().getNombre();

        setTitle("CLARA - Menú Tesorero");
        setSize(450, 600);
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

        JLabel etiquetaRol = new JLabel("TESORERO");
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

        // Sección Consultas
        panelCentral.add(crearSeparador("CONSULTAS"), gbc);
        panelCentral.add(ComponentesUI.crearBoton("Ver saldo de la agrupación", e -> controladorPrincipal.mostrarVerSaldo()), gbc);
        panelCentral.add(ComponentesUI.crearBoton("Ver historial de movimientos", e -> controladorPrincipal.mostrarHistorial()), gbc);

        panelCentral.add(Box.createVerticalStrut(15), gbc);

        // Sección Movimientos
        panelCentral.add(crearSeparador("MOVIMIENTOS"), gbc);
        panelCentral.add(ComponentesUI.crearBoton("Registrar ingreso (Verde)", e -> controladorPrincipal.mostrarRegistrarMovimiento(true)), gbc);
        panelCentral.add(ComponentesUI.crearBoton("Registrar egreso (Rojo)", e -> controladorPrincipal.mostrarRegistrarMovimiento(false)), gbc);

        panelCentral.add(Box.createVerticalStrut(15), gbc);

        // Sección Personal
        panelCentral.add(crearSeparador("PERSONAL"), gbc);
        panelCentral.add(ComponentesUI.crearBoton("Cambiar contraseña", e -> controladorPrincipal.mostrarCambiarContrasena()), gbc);

        add(panelCentral, BorderLayout.CENTER);

        // --- PANEL INFERIOR ---
        JPanel panelInferior = ComponentesUI.crearPanel();
        panelInferior.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));
        panelInferior.setLayout(new BorderLayout());

        JButton btnCerrarSesion = ComponentesUI.crearBotonPeligro("Cerrar Sesión", e -> {
            dispose();
            controladorPrincipal.cerrarSesion();
        });
        panelInferior.add(btnCerrarSesion, BorderLayout.CENTER);

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
