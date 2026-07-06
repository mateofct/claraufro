package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.List;

/**
 * Ventana gráfica para visualizar el historial de transacciones financieras
 * de una agrupación.
 * <p>
 * Según el patrón MVC puro, esta clase no conoce a los controladores.
 * Proporciona filtros por fecha y tipo de movimiento, una tabla de resultados
 * con las columnas ID, Agrupación, Tipo, Monto, Fecha, Descripción,
 * Comprobante y Usuario, y un botón para ver el comprobante del movimiento
 * seleccionado.
 * </p>
 *
 * <h3>Contratos de los métodos de filtro:</h3>
 * <table border="1" cellpadding="4" cellspacing="0">
 *   <caption>Métodos de filtrado</caption>
 *   <tr><th>Método</th><th>Comportamiento</th></tr>
 *   <tr>
 *     <td>{@link #getFechaFiltro()}</td>
 *     <td>Devuelve la fecha ingresada o cadena vacía si solo hay placeholders</td>
 *   </tr>
 *   <tr>
 *     <td>{@link #getTipoFiltro()}</td>
 *     <td>Devuelve el tipo seleccionado o cadena vacía si es "Todos"</td>
 *   </tr>
 * </table>
 *
 * @author CLARA Team
 * @version 1.0
 * @see Controlador.ControladorPrincipal#mostrarHistorial()
 */
public class VentanaHistorial extends JFrame {

    /**
     * Modelo de datos de la tabla de movimientos.
     */
    private DefaultTableModel modeloTabla;

    /**
     * Tabla que muestra los movimientos filtrados.
     */
    private JTable tablaMovimientos;

    /**
     * Campo de texto con máscara para ingresar una fecha en formato dd/MM/yyyy.
     */
    private JFormattedTextField txtFecha;

    /**
     * Combo box para seleccionar el tipo de movimiento a filtrar.
     */
    private JComboBox<String> cbTipo;

    /**
     * Botón que ejecuta el filtro aplicado.
     */
    private JButton btnFiltrar;

    /**
     * Botón para abrir el comprobante del movimiento seleccionado.
     */
    private JButton btnVerComprobante;

    /**
     * Constructor que construye y configura la ventana del historial.
     * <p>
     * Configura los filtros de fecha (con máscara dd/MM/yyyy) y tipo
     * (Todos, INGRESO, EGRESO), la tabla de resultados con 8 columnas
     * no editables, y los botones de filtrado y ver comprobante.
     * </p>
     */
    public VentanaHistorial() {
        setTitle("CLARA - Historial de Transacciones");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        ComponentesUI.configurarFondo(this);

        // --- PANEL SUPERIOR ---
        JPanel panelSuperior = ComponentesUI.crearPanel();
        panelSuperior.setLayout(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel titulo = ComponentesUI.crearSubtitulo("Historial de Transacciones");
        panelSuperior.add(titulo, BorderLayout.NORTH);

        JPanel panelFiltros = ComponentesUI.crearPanel();
        panelFiltros.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        panelFiltros.add(ComponentesUI.crearEtiqueta("Fecha (DD/MM/YYYY):"));
        try {
            MaskFormatter formato = new MaskFormatter("##/##/####");
            formato.setPlaceholderCharacter('_');
            txtFecha = new JFormattedTextField(formato);
            txtFecha.setPreferredSize(new Dimension(100, 30));
        } catch (ParseException e) {
            txtFecha = new JFormattedTextField();
        }
        panelFiltros.add(txtFecha);

        panelFiltros.add(ComponentesUI.crearEtiqueta("Tipo:"));
        cbTipo = new JComboBox<>(new String[]{"Todos", "INGRESO", "EGRESO"});
        panelFiltros.add(cbTipo);

        btnFiltrar = ComponentesUI.crearBoton("Filtrar", null);
        panelFiltros.add(btnFiltrar);

        panelSuperior.add(panelFiltros, BorderLayout.SOUTH);
        add(panelSuperior, BorderLayout.NORTH);

        // --- PANEL CENTRAL ---
        JPanel panelCentral = ComponentesUI.crearPanel();
        panelCentral.setLayout(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        String[] columnas = {"ID", "Agrupación", "Tipo", "Monto", "Fecha", "Descripción", "Comprobante", "Usuario"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaMovimientos = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaMovimientos);
        panelCentral.add(scroll, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        // --- PANEL INFERIOR ---
        JPanel panelInferior = ComponentesUI.crearPanel();
        panelInferior.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        btnVerComprobante = ComponentesUI.crearBoton("Ver Comprobante", null);
        panelInferior.add(btnVerComprobante);
        JButton btnCerrar = ComponentesUI.crearBotonPeligro("Cerrar", e -> dispose());
        panelInferior.add(btnCerrar);
        add(panelInferior, BorderLayout.SOUTH);
    }

    /**
     * Obtiene el valor del filtro de fecha.
     * <p>
     * Si el campo de fecha solo contiene placeholders (guiones bajos o barras),
     * retorna una cadena vacía indicando que no se desea filtrar por fecha.
     * </p>
     *
     * @return la fecha del filtro en formato dd/MM/yyyy, o cadena vacía
     *         si no se ha ingresado ninguna fecha
     */
    public String getFechaFiltro() {
        String fechaRaw = txtFecha.getText().replace("_", "").replace("/", "").trim();
        return fechaRaw.isEmpty() ? "" : txtFecha.getText();
    }

    /**
     * Obtiene el valor del filtro de tipo de movimiento.
     * <p>
     * Si el combo box muestra "Todos", retorna una cadena vacía indicando
     * que no se desea filtrar por tipo.
     * </p>
     *
     * @return el tipo seleccionado ("INGRESO" o "EGRESO"), o cadena vacía
     *         si está seleccionado "Todos"
     */
    public String getTipoFiltro() {
        String tipo = cbTipo.getSelectedItem().toString();
        return tipo.equals("Todos") ? "" : tipo;
    }

    /**
     * Carga los movimientos filtrados en la tabla de resultados.
     *
     * @param movimientos lista de arreglos de cadenas, donde cada arreglo
     *        representa una fila con los datos del movimiento
     */
    public void cargarMovimientos(List<String[]> movimientos) {
        modeloTabla.setRowCount(0);
        for (String[] fila : movimientos) {
            modeloTabla.addRow(fila);
        }
    }

    /**
     * Obtiene el nombre del comprobante del movimiento seleccionado en la tabla.
     * <p>
     * El nombre del comprobante se encuentra en la columna 6 (índice 6)
     * de la fila seleccionada.
     * </p>
     *
     * @return el nombre del comprobante, o {@code null} si no hay fila seleccionada
     */
    public String getNombreComprobanteSeleccionado() {
        int fila = tablaMovimientos.getSelectedRow();
        if (fila == -1) return null;
        return modeloTabla.getValueAt(fila, 6).toString();
    }

    /**
     * Registra un {@link ActionListener} para el botón de filtrado.
     *
     * @param listener el listener a ejecutar cuando se presione "Filtrar"
     */
    public void setFiltrarListener(ActionListener listener) {
        btnFiltrar.addActionListener(listener);
    }

    /**
     * Registra un {@link ActionListener} para el botón de ver comprobante.
     *
     * @param listener el listener a ejecutar cuando se presione "Ver Comprobante"
     */
    public void setVerComprobanteListener(ActionListener listener) {
        btnVerComprobante.addActionListener(listener);
    }
}
