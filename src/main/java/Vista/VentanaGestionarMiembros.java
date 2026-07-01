package Vista;

import Modelo.Agrupacion;
import Modelo.Usuario;
import Controlador.ControladorAgrupacion;
import Controlador.ControladorUsuario;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaGestionarMiembros extends JFrame {
    private ControladorAgrupacion controladorAgrupacion;
    private ControladorUsuario controladorUsuario;
    private JComboBox<Agrupacion> comboAgrupaciones;
    private JTextField buscarUsuario;
    private DefaultTableModel modeloTablaMiembros;
    private DefaultTableModel modeloTablaCandidatos;
    private JTable tablaMiembros;
    private JTable tablaCandidatos;

    public VentanaGestionarMiembros(ControladorAgrupacion controladorAgrupacion, ControladorUsuario controladorUsuario) {
        this.controladorAgrupacion = controladorAgrupacion;
        this.controladorUsuario = controladorUsuario;

        setTitle("CLARA - Gestionar miembros de agrupación");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        ComponentesUI.configurarFondo(this);

        JPanel panelSuperior = ComponentesUI.crearPanel();
        panelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT, 10,10));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        panelSuperior.add(ComponentesUI.crearEtiqueta("Seleccionar Agrupación:"));
        comboAgrupaciones = new JComboBox<>();
        comboAgrupaciones.setFont(ComponentesUI.FUENTE_ETIQUETA);
        comboAgrupaciones.setPreferredSize(new Dimension(400, 25));
        cargarAgrupaciones();
        comboAgrupaciones.addActionListener(e -> cargarUsuariosEnTablas());
        panelSuperior.add(comboAgrupaciones);

        panelSuperior.add(Box.createHorizontalStrut(10));

        panelSuperior.add(ComponentesUI.crearEtiqueta("Seleccionar Usuario:"));
        buscarUsuario = ComponentesUI.crearCampoTexto();
        buscarUsuario.setPreferredSize(new Dimension(180, 30));
        buscarUsuario.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { cargarUsuariosEnTablas(); }
            public void removeUpdate(DocumentEvent e) { cargarUsuariosEnTablas(); }
            public void changedUpdate(DocumentEvent e) { cargarUsuariosEnTablas(); }
        });
        panelSuperior.add(buscarUsuario);

        JPanel panelCentral = ComponentesUI.crearPanel();
        panelCentral.setLayout(new GridLayout(1, 2, 15, 0)); // Dos columnas para las tablas
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        String[] columnas = {"ID", "Matrícula", "Nombre"};
        modeloTablaCandidatos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaCandidatos = new JTable(modeloTablaCandidatos);
        JScrollPane scrollCandidatos = new JScrollPane(tablaCandidatos);
        panelCentral.add(crearPanelTabla("Usuarios Candidatos", scrollCandidatos));

        modeloTablaMiembros = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaMiembros = new JTable(modeloTablaMiembros);
        JScrollPane scrollMiembros = new JScrollPane(tablaMiembros);
        panelCentral.add(crearPanelTabla("Miembros de la Agrupación", scrollMiembros));

        add(panelCentral, BorderLayout.CENTER);

        JPanel panelSur = ComponentesUI.crearPanel();
        panelSur.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelSur.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JButton botonAgregar = ComponentesUI.crearBoton("Agregar Miembro", e -> agregarSeleccionado());
        JButton botonQuitar = ComponentesUI.crearBotonPeligro("Quitar Miembro", e -> quitarSeleccionado());

        panelSur.add(botonAgregar);
        panelSur.add(botonQuitar);

        add(panelSur, BorderLayout.SOUTH);

        cargarUsuariosEnTablas();
        setVisible(true);
    }

    private void cargarAgrupaciones() {
        comboAgrupaciones.removeAllItems();
        for (Agrupacion agrupacion : controladorAgrupacion.listarAgrupaciones()) {
            comboAgrupaciones.addItem(agrupacion);
        }
    }

    private void agregarSeleccionado() {
        int filaSeleccionada = tablaCandidatos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la lista de candidatos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idUsuario = (String) modeloTablaCandidatos.getValueAt(filaSeleccionada, 0);
        Usuario usuario = controladorUsuario.buscarUsuarioPorId(idUsuario);
        Agrupacion agrupacion = (Agrupacion) comboAgrupaciones.getSelectedItem();

        if (usuario != null && agrupacion != null) {
            try {
                controladorAgrupacion.agregarMiembro(agrupacion.getIdAgrupacion(), usuario);
                JOptionPane.showMessageDialog(this, "Miembro agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarUsuariosEnTablas();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void quitarSeleccionado() {
        int filaSeleccionada = tablaMiembros.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un miembro de la lista de la agrupación.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idUsuario = (String) modeloTablaMiembros.getValueAt(filaSeleccionada, 0);
        Usuario usuario = controladorUsuario.buscarUsuarioPorId(idUsuario);
        Agrupacion agrupacion = (Agrupacion) comboAgrupaciones.getSelectedItem();

        if (usuario != null && agrupacion != null) {
            try {
                controladorAgrupacion.quitarMiembro(agrupacion.getIdAgrupacion(), usuario);
                JOptionPane.showMessageDialog(this, "Miembro quitado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarUsuariosEnTablas();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarUsuariosEnTablas() {
        modeloTablaCandidatos.setRowCount(0);
        modeloTablaMiembros.setRowCount(0);

        Agrupacion agrupacionSeleccionada = (Agrupacion) comboAgrupaciones.getSelectedItem();
        if (agrupacionSeleccionada == null) return;

        String filtro = buscarUsuario.getText().toLowerCase();

        List<Usuario> todosLosUsuarios = controladorUsuario.listarUsuarios();
        List<String> idMiembrosAgrupacion = agrupacionSeleccionada.getIdMiembros();

        for (Usuario user : todosLosUsuarios) {
            boolean esMiembro = idMiembrosAgrupacion.contains(user.getIdUsuario());
            boolean coincideFiltro = filtro.isEmpty() ||
                    user.getMatricula().toLowerCase().contains(filtro) ||
                    user.getNombre().toLowerCase().contains(filtro);

            if (coincideFiltro) {
                Object[] fila = {user.getIdUsuario(), user.getMatricula(), user.getNombre()};
                if (esMiembro) {
                    modeloTablaMiembros.addRow(fila);
                } else {
                    modeloTablaCandidatos.addRow(fila);
                }
            }
        }
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