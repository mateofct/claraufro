package Vista;

import Modelo.Agrupacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Vista para gestionar agrupaciones.
 * <p>
 * MVC Puro: No conoce al controlador. Proporciona una tabla con las
 * agrupaciones registradas (columnas: ID y Nombre), un campo de texto
 * para editar el nombre de la agrupación seleccionada, y botones para
 * guardar cambios y eliminar la agrupación seleccionada.
 * </p>
 *
 * <h3>Elementos de la interfaz:</h3>
 * <table border="1" cellpadding="4" cellspacing="0">
 *   <caption>Componentes de la ventana de gestión de agrupaciones</caption>
 *   <tr><th>Elemento</th><th>Descripción</th></tr>
 *   <tr><td>Tabla</td><td>Columnas: ID, Nombre (no editable)</td></tr>
 *   <tr><td>Campo "EDITAR Nombre"</td><td>Permite editar el nombre de la agrupación seleccionada</td></tr>
 *   <tr><td>Botón "EDITAR Nombre"</td><td>Guarda el nuevo nombre de la agrupación</td></tr>
 *   <tr><td>Botón "Eliminar Agrupación"</td><td>Elimina la agrupación seleccionada (tras reasignar sus miembros)</td></tr>
 * </table>
 *
 * @author CLARA Team
 * @version 1.0
 * @see Controlador.ControladorPrincipal#mostrarGestionarAgrupaciones()
 */
public class VentanaGestionarAgrupacion extends JFrame {

    /**
     * Tabla que muestra la lista de agrupaciones registradas.
     */
    private JTable tablaAgrupaciones;

    /**
     * Modelo de datos de la tabla de agrupaciones.
     */
    private DefaultTableModel modeloTablaAgrupaciones;

    /**
     * Campo de texto donde el usuario puede editar el nombre de la agrupación
     * seleccionada.
     */
    private JTextField campoNombreAgrupacion;

    /**
     * Botón para guardar los cambios del nombre de la agrupación seleccionada.
     */
    private JButton btnGuardarCambios;

    /**
     * Botón para eliminar la agrupación seleccionada.
     */
    private JButton btnEliminar;

    /**
     * Constructor que construye y configura la ventana de gestión de agrupaciones.
     * <p>
     * Configura el título, tamaño, comportamiento de cierre y posición
     * centrada en pantalla. La tabla tiene las columnas ID y Nombre,
     * todas no editables. La selección es individual (una fila a la vez).
     * Utiliza {@link ComponentesUI} para garantizar un diseño visual
     * consistente con el resto de la aplicación.
     * </p>
     */
    public VentanaGestionarAgrupacion() {
        setTitle("CLARA - Gestionar Agrupaciones");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        ComponentesUI.configurarFondo(this);

        JPanel panelSuperior = ComponentesUI.crearPanel();
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panelSuperior.add(ComponentesUI.crearTitulo("Gestionar Agrupaciones"));
        add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCentral = ComponentesUI.crearPanel();
        panelCentral.setLayout(new BorderLayout(10,10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        String[] columnas = {"ID", "Nombre"};
        modeloTablaAgrupaciones = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaAgrupaciones = new JTable(modeloTablaAgrupaciones);
        tablaAgrupaciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tablaAgrupaciones);
        panelCentral.add(scrollPane, BorderLayout.CENTER);

        JPanel panelEdicion = ComponentesUI.crearPanel();
        panelEdicion.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        panelEdicion.add(ComponentesUI.crearEtiqueta("EDITAR Nombre de la Agrupación:"), gbc);
        campoNombreAgrupacion = ComponentesUI.crearCampoTexto();
        panelEdicion.add(campoNombreAgrupacion, gbc);

        JPanel panelBotonesEdicion = ComponentesUI.crearPanel();
        panelBotonesEdicion.setLayout(new GridLayout(1, 2, 10, 0));
        btnGuardarCambios = ComponentesUI.crearBoton("EDITAR Nombre", null);
        btnEliminar = ComponentesUI.crearBotonPeligro("Eliminar Agrupación", null);
        panelBotonesEdicion.add(btnGuardarCambios);
        panelBotonesEdicion.add(btnEliminar);
        gbc.insets = new Insets(15, 0, 5, 0);
        panelEdicion.add(panelBotonesEdicion, gbc);

        panelCentral.add(panelEdicion, BorderLayout.SOUTH);

        add(panelCentral, BorderLayout.CENTER);

        JPanel panelInferior = ComponentesUI.crearPanel();
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        panelInferior.setLayout(new BorderLayout());
        JButton botonCerrar = ComponentesUI.crearBotonPeligro("Cerrar", e -> dispose());
        panelInferior.add(botonCerrar, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    /**
     * Carga las agrupaciones en la tabla de resultados.
     *
     * @param agrupaciones lista de agrupaciones a mostrar en la tabla
     */
    public void cargarAgrupaciones(List<Agrupacion> agrupaciones) {
        modeloTablaAgrupaciones.setRowCount(0);
        for (Agrupacion a : agrupaciones) {
            modeloTablaAgrupaciones.addRow(new Object[]{a.getIdAgrupacion(), a.getNombreAgrupacion()});
        }
    }

    /**
     * Obtiene el ID de la agrupación seleccionada en la tabla.
     *
     * @return el ID de la agrupación seleccionada, o {@code null} si no hay
     *         selección
     */
    public String getIdSeleccionado() {
        int fila = tablaAgrupaciones.getSelectedRow();
        return (fila != -1) ? (String) modeloTablaAgrupaciones.getValueAt(fila, 0) : null;
    }

    /**
     * Obtiene el nombre de la agrupación ingresado en el campo de edición.
     *
     * @return el nombre sin espacios extremos
     */
    public String getNombreAgrupacion() {
        return campoNombreAgrupacion.getText().trim();
    }

    /**
     * Establece el nombre de la agrupación en el campo de edición.
     *
     * @param nombre el nombre a mostrar en el campo
     */
    public void setNombreAgrupacion(String nombre) {
        campoNombreAgrupacion.setText(nombre);
    }

    /**
     * Registra un listener de selección para la tabla de agrupaciones.
     *
     * @param listener el listener de selección de lista
     */
    public void setSeleccionListener(javax.swing.event.ListSelectionListener listener) {
        tablaAgrupaciones.getSelectionModel().addListSelectionListener(listener);
    }

    /**
     * Registra un {@link ActionListener} para el botón "EDITAR Nombre".
     *
     * @param listener el listener a ejecutar al presionar "EDITAR Nombre"
     */
    public void setGuardarListener(ActionListener listener) {
        btnGuardarCambios.addActionListener(listener);
    }

    /**
     * Registra un {@link ActionListener} para el botón "Eliminar Agrupación".
     *
     * @param listener el listener a ejecutar al presionar "Eliminar Agrupación"
     */
    public void setEliminarListener(ActionListener listener) {
        btnEliminar.addActionListener(listener);
    }
}
