package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Vista para cambiar la contraseña.
 * <p>
 * MVC Puro: No conoce al controlador. Expone tres campos de contraseña
 * (contraseña actual, nueva contraseña y confirmación) y un
 * {@link ActionListener} para el botón de guardar que el controlador
 * principal asigna mediante {@link #setGuardarListener(ActionListener)}.
 * </p>
 *
 * <h3>Elementos de la interfaz:</h3>
 * <table border="1" cellpadding="4" cellspacing="0">
 *   <caption>Campos del formulario de cambio de contraseña</caption>
 *   <tr><th>Elemento</th><th>Descripción</th></tr>
 *   <tr><td>Campo "Contraseña actual"</td><td>Para verificar la identidad del usuario</td></tr>
 *   <tr><td>Campo "Nueva contraseña"</td><td>La nueva contraseña deseada</td></tr>
 *   <tr><td>Campo "Confirmar nueva contraseña"</td><td>Debe coincidir con la nueva contraseña</td></tr>
 * </table>
 *
 * @author CLARA Team
 * @version 1.0
 * @see Controlador.ControladorPrincipal#mostrarCambiarContrasena()
 */
public class VentanaCambiarContrasena extends JFrame {

    /**
     * Campo de contraseña donde el usuario ingresa su contraseña actual.
     */
    private JPasswordField campoContrasenaActual;

    /**
     * Campo de contraseña donde el usuario ingresa la nueva contraseña deseada.
     */
    private JPasswordField campoNuevaContrasena;

    /**
     * Campo de contraseña donde el usuario confirma la nueva contraseña.
     */
    private JPasswordField campoConfirmarContrasena;

    /**
     * Botón para guardar los cambios de contraseña.
     */
    private JButton btnGuardar;

    /**
     * Constructor que construye y configura la ventana de cambio de contraseña.
     * <p>
     * Configura el título, tamaño, comportamiento de cierre y posición
     * centrada en pantalla. Utiliza {@link ComponentesUI} para garantizar
     * un diseño visual consistente con el resto de la aplicación.
     * </p>
     */
    public VentanaCambiarContrasena() {
        setTitle("CLARA - Cambiar Contraseña");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        ComponentesUI.configurarFondo(this);

        JPanel panelSuperior = ComponentesUI.crearPanel();
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panelSuperior.add(ComponentesUI.crearTitulo("Cambiar Contraseña"));
        add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCentro = ComponentesUI.crearPanel();
        panelCentro.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 40, 8, 40);

        panelCentro.add(ComponentesUI.crearEtiqueta("Contraseña Actual:"), gbc);
        campoContrasenaActual = ComponentesUI.crearCampoContrasena();
        panelCentro.add(campoContrasenaActual, gbc);

        panelCentro.add(ComponentesUI.crearEtiqueta("Nueva Contraseña:"), gbc);
        campoNuevaContrasena = ComponentesUI.crearCampoContrasena();
        panelCentro.add(campoNuevaContrasena, gbc);

        panelCentro.add(ComponentesUI.crearEtiqueta("Confirmar Nueva Contraseña:"), gbc);
        campoConfirmarContrasena = ComponentesUI.crearCampoContrasena();
        panelCentro.add(campoConfirmarContrasena, gbc);

        add(panelCentro, BorderLayout.CENTER);

        JPanel panelInferior = ComponentesUI.crearPanel();
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 40, 30, 40));
        panelInferior.setLayout(new GridLayout(1, 2, 10, 0));

        btnGuardar = ComponentesUI.crearBoton("Guardar Cambios", null);
        JButton btnCancelar = ComponentesUI.crearBotonPeligro("Cancelar", e -> dispose());

        panelInferior.add(btnGuardar);
        panelInferior.add(btnCancelar);

        add(panelInferior, BorderLayout.SOUTH);
    }

    /**
     * Obtiene la contraseña actual ingresada por el usuario como cadena de texto.
     *
     * @return la contraseña actual
     */
    public String getContrasenaActual() {
        return new String(campoContrasenaActual.getPassword());
    }

    /**
     * Obtiene la nueva contraseña ingresada por el usuario como cadena de texto.
     *
     * @return la nueva contraseña
     */
    public String getNuevaContrasena() {
        return new String(campoNuevaContrasena.getPassword());
    }

    /**
     * Obtiene la contraseña de confirmación ingresada por el usuario.
     *
     * @return la contraseña de confirmación
     */
    public String getConfirmacionContrasena() {
        return new String(campoConfirmarContrasena.getPassword());
    }

    /**
     * Registra un {@link ActionListener} para el botón "Guardar Cambios" que el
     * controlador principal asignará para procesar el cambio de contraseña.
     *
     * @param listener el listener a ejecutar al presionar "Guardar Cambios"
     */
    public void setGuardarListener(ActionListener listener) {
        btnGuardar.addActionListener(listener);
    }
}
