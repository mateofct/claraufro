package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Vista para registrar un movimiento financiero.
 * <p>
 * MVC Puro: No conoce al controlador. Expone los datos del formulario
 * (monto, descripción, ruta del comprobante) y un {@link ActionListener}
 * para el botón de guardar que el controlador principal asigna mediante
 * {@link #setGuardarListener(ActionListener)}.
 * </p>
 * <p>
 * Cuando se registra un egreso, se muestra el saldo actual de la agrupación
 * en el panel superior para que el tesorero pueda validar que el monto del
 * egreso no supere el saldo disponible.
 * </p>
 *
 * <h3>Elementos de la interfaz:</h3>
 * <table border="1" cellpadding="4" cellspacing="0">
 *   <caption>Campos del formulario de movimiento</caption>
 *   <tr><th>Elemento</th><th>Descripción</th></tr>
 *   <tr><td>Campo "Monto"</td><td>Texto libre (el controlador valida que sea numérico y positivo)</td></tr>
 *   <tr><td>Campo "Descripción"</td><td>Texto libre para describir el movimiento</td></tr>
 *   <tr><td>Botón "Seleccionar Archivo"</td><td>Abre un diálogo para elegir el comprobante PDF</td></tr>
 * </table>
 *
 * @author CLARA Team
 * @version 1.0
 * @see Controlador.ControladorPrincipal#mostrarRegistrarMovimiento(boolean)
 */
public class VentanaRegistrarMovimiento extends JFrame {

    /**
     * Indica si el movimiento a registrar es un ingreso ({@code true})
     * o un egreso ({@code false}).
     */
    private boolean esIngreso;

    /**
     * Campo de texto donde el usuario ingresa el monto del movimiento.
     */
    private JTextField campoMonto;

    /**
     * Campo de texto donde el usuario ingresa la descripción del movimiento.
     */
    private JTextField campoDescripcion;

    /**
     * Archivo de comprobante seleccionado por el usuario, o {@code null}
     * si no se ha seleccionado ningún archivo.
     */
    private File archivoComprobante = null;

    /**
     * Botón para seleccionar el archivo de comprobante.
     */
    private JButton botonSubirArchivo;

    /**
     * Botón para guardar el movimiento registrado.
     */
    private JButton botonGuardar;

    /**
     * Constructor que construye y configura la ventana de registro de movimiento.
     * <p>
     * Configura el título y los componentes según el tipo de movimiento
     * (ingreso o egreso). El botón de guardar se colorea en verde para
     * ingresos y en rojo para egresos. Se muestra el saldo actual de la
     * agrupación en el panel superior como referencia visual.
     * </p>
     *
     * @param esIngreso {@code true} para un ingreso, {@code false} para un egreso
     * @param saldoActual el saldo actual de la agrupación (se muestra en pantalla)
     */
    public VentanaRegistrarMovimiento(boolean esIngreso, int saldoActual) {
        this.esIngreso = esIngreso;

        setTitle(esIngreso ? "CLARA - Registrar Ingreso" : "CLARA - Registrar Egreso");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        ComponentesUI.configurarFondo(this);

        // --- PANEL SUPERIOR ---
        JPanel panelSuperior = ComponentesUI.crearPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel titulo = ComponentesUI.crearSubtitulo(esIngreso ? "Registrar Ingreso" : "Registrar Egreso");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSuperior.add(titulo);

        JLabel etiquetaSaldo = new JLabel("Saldo actual: $" + saldoActual);
        etiquetaSaldo.setFont(new Font("Arial", Font.ITALIC, 13));
        etiquetaSaldo.setForeground(Color.WHITE);
        etiquetaSaldo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSuperior.add(etiquetaSaldo);

        add(panelSuperior, BorderLayout.NORTH);

        // --- PANEL CENTRAL ---
        JPanel panelCentral = ComponentesUI.crearPanel();
        panelCentral.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 40, 5, 40);

        panelCentral.add(ComponentesUI.crearEtiqueta("Monto ($):"), gbc);
        campoMonto = ComponentesUI.crearCampoTexto();
        panelCentral.add(campoMonto, gbc);

        gbc.insets = new Insets(15, 40, 5, 40);
        panelCentral.add(ComponentesUI.crearEtiqueta("Descripción:"), gbc);
        campoDescripcion = ComponentesUI.crearCampoTexto();
        panelCentral.add(campoDescripcion, gbc);

        gbc.insets = new Insets(15, 40, 5, 40);
        panelCentral.add(ComponentesUI.crearEtiqueta("Comprobante (PDF):"), gbc);
        botonSubirArchivo = ComponentesUI.crearBoton("Seleccionar Archivo", e -> seleccionarArchivo());
        panelCentral.add(botonSubirArchivo, gbc);

        add(panelCentral, BorderLayout.CENTER);

        // --- PANEL INFERIOR ---
        JPanel panelInferior = ComponentesUI.crearPanel();
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 40, 40, 40));
        panelInferior.setLayout(new GridLayout(1, 2, 15, 0));

        botonGuardar = ComponentesUI.crearBoton(esIngreso ? "Guardar Ingreso" : "Guardar Egreso", null);
        if (esIngreso) {
            botonGuardar.setBackground(new Color(40, 167, 69));
            botonGuardar.setForeground(Color.WHITE);
        } else {
            botonGuardar.setBackground(new Color(220, 53, 69));
            botonGuardar.setForeground(Color.WHITE);
        }

        JButton botonCancelar = ComponentesUI.crearBoton("Cancelar", e -> dispose());

        panelInferior.add(botonGuardar);
        panelInferior.add(botonCancelar);

        add(panelInferior, BorderLayout.SOUTH);
    }

    /**
     * Abre un diálogo de selección de archivos para que el usuario elija
     * el comprobante PDF del movimiento.
     * <p>
     * Si se selecciona un archivo, actualiza el texto del botón con el nombre
     * del archivo y cambia su color de fondo a verde claro para indicar
     * que se cargó correctamente.
     * </p>
     */
    private void seleccionarArchivo() {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Seleccione el comprobante");
        int resultado = selector.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            archivoComprobante = selector.getSelectedFile();
            botonSubirArchivo.setText(archivoComprobante.getName());
            botonSubirArchivo.setBackground(new Color(200, 255, 200));
        }
    }

    /**
     * Obtiene el monto ingresado en el campo de texto.
     *
     * @return el monto como cadena de texto
     */
    public String getMonto() {
        return campoMonto.getText().trim();
    }

    /**
     * Obtiene la descripción ingresada en el campo de texto.
     *
     * @return la descripción del movimiento
     */
    public String getDescripcion() {
        return campoDescripcion.getText().trim();
    }

    /**
     * Obtiene la ruta absoluta del archivo de comprobante seleccionado.
     *
     * @return la ruta del comprobante, o cadena vacía si no se ha
     *         seleccionado ningún archivo
     */
    public String getRutaComprobante() {
        return (archivoComprobante != null) ? archivoComprobante.getAbsolutePath() : "";
    }

    /**
     * Registra un {@link ActionListener} para el botón de guardar que el
     * controlador principal asignará para procesar el movimiento.
     *
     * @param listener el listener a ejecutar al presionar el botón de guardar
     */
    public void setGuardarListener(ActionListener listener) {
        botonGuardar.addActionListener(listener);
    }
}
