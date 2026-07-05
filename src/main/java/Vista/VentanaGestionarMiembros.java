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
 * MVC Puro: No conoce al controlador.
 */
public class VentanaGestionarMiembros extends JFrame {
    private JComboBox<Agrupacion> comboAgrupaciones;
    private JTextField campoBusquedaUsuario;
    private JTable tablaCandidatos;
    private JTable tablaMiembros;
    private DefaultTableModel modeloTablaCandidatos;
    private DefaultTableModel modeloTablaMiembros;
    private JButton botonMover;
    private JButton botonQuitar;

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

    public void setAgrupaciones(List<Agrupacion> agrupaciones) {
        comboAgrupaciones.removeAllItems();
        for (Agrupacion a : agrupaciones) comboAgrupaciones.addItem(a);
    }

    public Agrupacion getAgrupacionSeleccionada() {
        return (Agrupacion) comboAgrupaciones.getSelectedItem();
    }

    public String getFiltro() {
        return campoBusquedaUsuario.getText().toLowerCase();
    }

    public void cargarTablas(List<Object[]> candidatos, List<Object[]> miembros) {
        modeloTablaCandidatos.setRowCount(0);
        for (Object[] fila : candidatos) modeloTablaCandidatos.addRow(fila);

        modeloTablaMiembros.setRowCount(0);
        for (Object[] fila : miembros) modeloTablaMiembros.addRow(fila);
    }

    public String getIdCandidatoSeleccionado() {
        int fila = tablaCandidatos.getSelectedRow();
        return (fila != -1) ? (String) modeloTablaCandidatos.getValueAt(fila, 0) : null;
    }

    public String getIdMiembroSeleccionado() {
        int fila = tablaMiembros.getSelectedRow();
        return (fila != -1) ? (String) modeloTablaMiembros.getValueAt(fila, 0) : null;
    }

    public void setAgrupacionListener(ActionListener listener) {
        comboAgrupaciones.addActionListener(listener);
    }

    public void setBusquedaListener(DocumentListener listener) {
        campoBusquedaUsuario.getDocument().addDocumentListener(listener);
    }

    public void setMoverListener(ActionListener listener) {
        botonMover.addActionListener(listener);
    }

    public void setQuitarListener(ActionListener listener) {
        botonQuitar.addActionListener(listener);
    }

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
