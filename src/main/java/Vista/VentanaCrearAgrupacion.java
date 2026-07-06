package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Vista para crear una agrupación.
 * <p>
 * MVC Puro: No conoce al controlador. Expone el nombre de la agrupación
 * ingresado en el campo de texto y un {@link ActionListener} para el
 * botón de crear que el controlador principal asigna mediante
 * {@link #setGuardarListener(ActionListener)}.
 * </p>
 *
 * @author CLARA Team
 * @version 1.0
 * @see Controlador.ControladorPrincipal#mostrarRegistrarAgrupacion()
 */
public class VentanaCrearAgrupacion extends JFrame {

    /**
     * Campo de texto donde el usuario ingresa el nombre de la nueva agrupación.
     */
    private JTextField nombreAgrupacion;

    /**
     * Botón para crear la nueva agrupación.
     */
    private JButton btnGuardar;

    /**
     * Constructor que construye y configura la ventana de creación de agrupación.
     * <p>
     * Configura el título, tamaño, comportamiento de cierre y posición
     * centrada en pantalla. Utiliza {@link ComponentesUI} para garantizar
     * un diseño visual consistente con el resto de la aplicación.
     * </p>
     */
    public VentanaCrearAgrupacion() {
        setTitle("CLARA - Crear Agrupación");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        ComponentesUI.configurarFondo(this);

        JPanel panelSuperior = ComponentesUI.crearPanel();
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panelSuperior.add(ComponentesUI.crearTitulo("Nueva Agrupación"));
        add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCentral = ComponentesUI.crearPanel();
        panelCentral.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 40, 10, 40);

        panelCentral.add(ComponentesUI.crearEtiqueta("Nombre de la Agrupación:"), gbc);
        nombreAgrupacion = ComponentesUI.crearCampoTexto();
        panelCentral.add(nombreAgrupacion, gbc);

        add(panelCentral, BorderLayout.CENTER);

        JPanel panelInferior = ComponentesUI.crearPanel();
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 40, 30, 40));
        panelInferior.setLayout(new GridLayout(1, 2, 10, 0));

        btnGuardar = ComponentesUI.crearBoton("Crear", null);
        JButton btnCancelar = ComponentesUI.crearBotonPeligro("Cancelar", e -> dispose());

        panelInferior.add(btnGuardar);
        panelInferior.add(btnCancelar);

        add(panelInferior, BorderLayout.SOUTH);
    }

    /**
     * Obtiene el nombre de la agrupación ingresado en el campo de texto.
     *
     * @return el nombre sin espacios extremos
     */
    public String getNombreAgrupacion() {
        return nombreAgrupacion.getText().trim();
    }

    /**
     * Registra un {@link ActionListener} para el botón "Crear" que el
     * controlador principal asignará para procesar la nueva agrupación.
     *
     * @param listener el listener a ejecutar al presionar "Crear"
     */
    public void setGuardarListener(ActionListener listener) {
        btnGuardar.addActionListener(listener);
    }
}
