package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import Modelo.RolUsuario;

/**
 * Vista para registrar un nuevo usuario.
 * <p>
 * MVC Puro: No conoce al controlador. Expone métodos para obtener datos
 * y asignar listeners. Los roles disponibles son Tesorero y Socio
 * (el administrador se crea automáticamente en la inicialización).
 * </p>
 *
 * <h3>Elementos de la interfaz:</h3>
 * <table border="1" cellpadding="4" cellspacing="0">
 *   <caption>Campos del formulario de registro</caption>
 *   <tr><th>Elemento</th><th>Descripción</th></tr>
 *   <tr><td>Campo "Matrícula"</td><td>11 caracteres (dígitos 0-9 y letra 'k')</td></tr>
 *   <tr><td>Campo "Contraseña"</td><td>Máximo 24 caracteres</td></tr>
 *   <tr><td>Combo "Rol del usuario"</td><td>Solo Tesorero y Socio</td></tr>
 * </table>
 *
 * @author CLARA Team
 * @version 1.0
 * @see Controlador.ControladorPrincipal#mostrarRegistrarUsuario()
 */
public class VentanaCrearUsuario extends JFrame {

    /**
     * Campo de texto donde se ingresa la matrícula del estudiante a registrar.
     */
    private JTextField EscribirMatricula;

    /**
     * Campo de contraseña donde se ingresa la contraseña del nuevo usuario.
     */
    private JPasswordField EscribirContraseña;

    /**
     * Combo box para seleccionar el rol del nuevo usuario.
     * Solo permite Tesorero y Socio.
     */
    private JComboBox<String> seleccionarRol;

    /**
     * Botón para registrar el nuevo usuario.
     */
    private JButton btnRegistrar;

    /**
     * Constructor que construye y configura la ventana de registro de usuario.
     * <p>
     * Configura el título, tamaño, comportamiento de cierre y posición
     * centrada en pantalla. Utiliza {@link ComponentesUI} para garantizar
     * un diseño visual consistente con el resto de la aplicación.
     * </p>
     */
    public VentanaCrearUsuario(){
        setTitle("CLARA - Registrar nuevo usuario");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        ComponentesUI.configurarFondo(this);

        JPanel panelSuperior = ComponentesUI.crearPanel();
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panelSuperior.add(ComponentesUI.crearTitulo("Registro de Usuario"));
        add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCentral = ComponentesUI.crearPanel();
        panelCentral.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 40, 8, 40);

        panelCentral.add(ComponentesUI.crearEtiqueta("Matrícula:"), gbc);
        EscribirMatricula = ComponentesUI.crearCampoTexto();
        panelCentral.add(EscribirMatricula, gbc);

        panelCentral.add(ComponentesUI.crearEtiqueta("Contraseña:"), gbc);
        EscribirContraseña = ComponentesUI.crearCampoContrasena();
        panelCentral.add(EscribirContraseña, gbc);

        panelCentral.add(ComponentesUI.crearEtiqueta("Rol del usuario:"), gbc);
        seleccionarRol = new JComboBox<>(new String[]{"Tesorero", "Socio"});
        seleccionarRol.setFont(new Font("Arial", Font.PLAIN, 14));
        panelCentral.add(seleccionarRol, gbc);

        add(panelCentral, BorderLayout.CENTER);

        JPanel panelInferior = ComponentesUI.crearPanel();
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 40, 30, 40));
        panelInferior.setLayout(new GridLayout(1, 2, 10, 0));

        btnRegistrar = ComponentesUI.crearBoton("Registrar", null);
        JButton btnCancelar = ComponentesUI.crearBotonPeligro("Cancelar", e -> dispose());

        panelInferior.add(btnRegistrar);
        panelInferior.add(btnCancelar);

        add(panelInferior, BorderLayout.SOUTH);
    }

    /**
     * Obtiene la matrícula ingresada en el campo de texto.
     *
     * @return la matrícula como cadena de texto sin espacios extremos
     */
    public String getMatricula() {
        return EscribirMatricula.getText().trim();
    }

    /**
     * Obtiene la contraseña ingresada en el campo de contraseña.
     *
     * @return la contraseña como cadena de texto
     */
    public String getContrasena() {
        return new String(EscribirContraseña.getPassword());
    }

    /**
     * Obtiene el rol seleccionado en el combo box y lo retorna como
     * una constante del enum {@link RolUsuario}.
     * <p>
     * Las opciones "Tesorero" y "Socio" del combo se mapean a
     * {@link RolUsuario#TESORERO} y {@link RolUsuario#SOCIO} respectivamente.
     * </p>
     *
     * @return el rol seleccionado ({@link RolUsuario#TESORERO} o
     *         {@link RolUsuario#SOCIO})
     */
    public RolUsuario getRolSeleccionado() {
        String rol = seleccionarRol.getSelectedItem().toString();
        return rol.equals("Tesorero") ? RolUsuario.TESORERO : RolUsuario.SOCIO;
    }

    /**
     * Registra un {@link ActionListener} para el botón "Registrar" que el
     * controlador principal asignará para procesar el nuevo usuario.
     *
     * @param listener el listener a ejecutar al presionar "Registrar"
     */
    public void setRegistrarListener(ActionListener listener) {
        btnRegistrar.addActionListener(listener);
    }
}
