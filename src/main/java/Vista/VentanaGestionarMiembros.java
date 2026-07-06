package Vista;

import Modelo.Agrupacion;
import Modelo.Usuario;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Vista para gestionar miembros de agrupaciones.
 * <p>
 * MVC Puro: No conoce al controlador. Proporciona un combo box para
 * seleccionar la agrupación a gestionar, un campo de búsqueda por matrícula
 * o nombre, y dos tablas: una con los candidatos disponibles (usuarios que
 * no pertenecen a la agrupación) y otra con los miembros actuales.
 * Incluye botones para trasladar candidatos a la agrupación y para quitar
 * miembros (los envía a "Sin Agrupación").
 * </p>
 *
 * <h3>Elementos de la interfaz:</h3>
 * <table border="1" cellpadding="4" cellspacing="0">
 *   <caption>Componentes de la ventana de gestión de miembros</caption>
 *   <tr><th>Elemento</th><th>Descripción</th></tr>
 *   <tr><td>Combo "Agrupación a Gestionar"</td><td>Selecciona la agrupación a gestionar</td></tr>
 *   <tr><td>Campo "Filtrar Usuarios"</td><td>Filtra usuarios por matrícula o nombre en tiempo real</td></tr>
 *   <tr><td>Tabla "Usuarios Disponibles"</td><td>Usuarios que no pertenecen a la agrupación (ID, Matrícula, Nombre, Agrupación Actual)</td></tr>
 *   <tr><td>Tabla "Miembros Actuales"</td><td>Usuarios que ya pertenecen a la agrupación (ID, Matrícula, Nombre)</td></tr>
 *   <tr><td>Botón "Trasladar"</td><td>Mueve el candidato seleccionado a la agrupación</td></tr>
 *   <tr><td>Botón "Dejar sin Agrupación"</td><td>Remueve el miembro seleccionado (lo envía a "Sin Agrupación")</td></tr>
 * </table>
 *
 * @author CLARA Team
 * @version 1.0
 * @see Controlador.ControladorPrincipal#mostrarGestionarMiembros()
 */
public class VentanaGestionarMiembros extends JFrame {

    /**
     * Combo box para seleccionar la agrupación a gestionar.
     */
    private JComboBox<Agrupacion> comboAgrupaciones;

    /**
     * Campo de texto para buscar usuarios por matrícula o nombre.
     */
    private JTextField campoBusquedaUsuario;

    /**
     * Tabla que muestra los usuarios candidatos disponibles.
     */
    private JTable tablaCandidatos;

    /**
     * Tabla que muestra los miembros actuales de la agrupación.
     */
    private JTable tablaMiembros;

    /**
     * Modelo de datos de la tabla de candidatos disponibles.
     */
    private DefaultTableModel modeloTablaCandidatos;

    /**
     * Modelo de datos de la tabla de miembros actuales.
     */
    private DefaultTableModel modeloTablaMiembros;

    /**
     * Botón para trasladar un candidato a la agrupación seleccionada.
     */
    private JButton botonMover;

    /**
     * Botón para quitar un miembro de la agrupación seleccionada.
     */
    private JButton botonQuitar;

    /**
     * Constructor que construye y configura la ventana de gestión de miembros.
     * <p>
     * Configura el título, tamaño, comportamiento de cierre y posición
     * centrada en pantalla. Utiliza {@link ComponentesUI} para garantizar
     * un diseño visual consistente con el resto de la aplicación.
     * </p>
     */
    public VentanaGestionarMiembros() {
        setTitle("CLARA - Traslado y Gestión de Miembros");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        ComponentesUI.configurarFondo(this);

        // --- PANEL SUPERIOR ---
        JPanel panelSuperior = ComponentesUI.crearPanel();
        panelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        panelSuperior.add(ComponentesUI.crearEtiqueta("Agrupación a Gestionar:"));
        comboAgrupaciones = new JComboBox<>();
        comboAgrupaciones.setFont(ComponentesUI.FUENTE_ETIQUETA);
        comboAgrupaciones.setPreferredSize(new Dimension(250, 35));
        panelSuperior.add(comboAgrupaciones);

        panelSuperior.add(Box.createHorizontalStrut(30));

        panelSuperior.add(ComponentesUI.crearEtiqueta("Filtrar Usuarios:"));
        campoBusquedaUsuario = ComponentesUI.crearCampoTexto();
        campoBusquedaUsuario.setPreferredSize(new Dimension(200, 35));
        panelSuperior.add(campoBusquedaUsuario);

        add(panelSuperior, BorderLayout.NORTH);

        // --- PANEL CENTRAL ---
        JPanel panelCentral = ComponentesUI.crearPanel();
        panelCentral.setLayout(new GridLayout(1, 2, 20, 0));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Tabla Candidatos
        String[] columnasCandidatos = {"ID", "Matrícula", "Nombre", "Agrupación Actual"};
        modeloTablaCandidatos = new DefaultTableModel(columnasCandidatos, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaCandidatos = new JTable(modeloTablaCandidatos);
        JScrollPane scrollCandidatos = new JScrollPane(tablaCandidatos);
        panelCentral.add(crearPanelTabla("Usuarios Disponibles / De otras Agrupaciones", scrollCandidatos));

        // Tabla Miembros
        String[] columnasMiembros = {"ID", "Matrícula", "Nombre"};
        modeloTablaMiembros = new DefaultTableModel(columnasMiembros, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaMiembros = new JTable(modeloTablaMiembros);
        JScrollPane scrollMiembros = new JScrollPane(tablaMiembros);
        panelCentral.add(crearPanelTabla("Miembros Actuales de la Agrupación", scrollMiembros));

        add(panelCentral, BorderLayout.CENTER);

        // --- PANEL INFERIOR ---
        JPanel panelInferior = ComponentesUI.crearPanel();
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 15));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        botonMover = ComponentesUI.crearBoton("Trasladar a esta Agrupación", null);
        botonQuitar = ComponentesUI.crearBotonPeligro("Dejar sin Agrupación", null);
        JButton botonCerrar = ComponentesUI.crearBotonPeligro("Cerrar", e -> dispose());

        panelInferior.add(botonMover);
        panelInferior.add(botonQuitar);
        panelInferior.add(botonCerrar);

        add(panelInferior, BorderLayout.SOUTH);
    }

    /**
     * Establece la lista de agrupaciones en el combo box de selección.
     *
     * @param agrupaciones lista de agrupaciones a cargar en el combo
     */
    public void setAgrupaciones(List<Agrupacion> agrupaciones) {
        comboAgrupaciones.removeAllItems();
        for (Agrupacion a : agrupaciones) comboAgrupaciones.addItem(a);
    }

    /**
     * Obtiene la agrupación seleccionada actualmente en el combo box.
     *
     * @return la agrupación seleccionada, o {@code null} si no hay selección
     */
    public Agrupacion getAgrupacionSeleccionada() {
        return (Agrupacion) comboAgrupaciones.getSelectedItem();
    }

    /**
     * Obtiene el texto de búsqueda ingresado en el campo de filtro,
     * convertido a minúsculas.
     *
     * @return el texto de búsqueda en minúsculas
     */
    public String getFiltro() {
        return campoBusquedaUsuario.getText().toLowerCase();
    }

    /**
     * Carga las tablas de candidatos y miembros con los datos proporcionados.
     *
     * @param candidatos lista de arreglos de objetos con los candidatos disponibles
     * @param miembros lista de arreglos de objetos con los miembros actuales
     */
    public void cargarTablas(List<Object[]> candidatos, List<Object[]> miembros) {
        modeloTablaCandidatos.setRowCount(0);
        for (Object[] fila : candidatos) modeloTablaCandidatos.addRow(fila);

        modeloTablaMiembros.setRowCount(0);
        for (Object[] fila : miembros) modeloTablaMiembros.addRow(fila);
    }

    /**
     * Obtiene el ID del candidato seleccionado en la tabla de candidatos.
     *
     * @return el ID del candidato, o {@code null} si no hay selección
     */
    public String getIdCandidatoSeleccionado() {
        int fila = tablaCandidatos.getSelectedRow();
        return (fila != -1) ? (String) modeloTablaCandidatos.getValueAt(fila, 0) : null;
    }

    /**
     * Obtiene el ID del miembro seleccionado en la tabla de miembros.
     *
     * @return el ID del miembro, o {@code null} si no hay selección
     */
    public String getIdMiembroSeleccionado() {
        int fila = tablaMiembros.getSelectedRow();
        return (fila != -1) ? (String) modeloTablaMiembros.getValueAt(fila, 0) : null;
    }

    /**
     * Registra un {@link ActionListener} para el combo box de agrupaciones.
     *
     * @param listener el listener a ejecutar al cambiar la agrupación seleccionada
     */
    public void setAgrupacionListener(ActionListener listener) {
        comboAgrupaciones.addActionListener(listener);
    }

    /**
     * Registra un {@link DocumentListener} para el campo de búsqueda que
     * se ejecuta en tiempo real al escribir.
     *
     * @param listener el listener de documento
     */
    public void setBusquedaListener(DocumentListener listener) {
        campoBusquedaUsuario.getDocument().addDocumentListener(listener);
    }

    /**
     * Registra un {@link ActionListener} para el botón "Trasladar".
     *
     * @param listener el listener a ejecutar al presionar "Trasladar a esta Agrupación"
     */
    public void setMoverListener(ActionListener listener) {
        botonMover.addActionListener(listener);
    }

    /**
     * Registra un {@link ActionListener} para el botón "Dejar sin Agrupación".
     *
     * @param listener el listener a ejecutar al presionar "Dejar sin Agrupación"
     */
    public void setQuitarListener(ActionListener listener) {
        botonQuitar.addActionListener(listener);
    }

    /**
     * Crea un panel contenedor para una tabla con su título centrado.
     *
     * @param titulo el texto del título de la tabla
     * @param scrollPane el {@code JScrollPane} que contiene la tabla
     * @return el panel configurado con el título en la parte superior y
     *         la tabla en el centro
     */
    private JPanel crearPanelTabla(String titulo, JScrollPane scrollPane) {
        JPanel panel = ComponentesUI.crearPanel();
        panel.setLayout(new BorderLayout(5, 5));
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(ComponentesUI.FUENTE_ETIQUETA);
        lblTitulo.setForeground(ComponentesUI.COLOR_TEXTO_ETIQUETA);
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
}
