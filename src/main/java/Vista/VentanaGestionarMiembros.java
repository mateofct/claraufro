package Vista;

import Controlador.ControladorAgrupacion;
import Controlador.ControladorUsuario;
import Modelo.Agrupacion;
import Modelo.Usuario;

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
    private JTextField campoBusquedaUsuario;
    private JTable tablaCandidatos;
    private JTable tablaMiembros;
    private DefaultTableModel modeloTablaCandidatos;
    private DefaultTableModel modeloTablaMiembros;

    public VentanaGestionarMiembros(ControladorAgrupacion controladorAgrupacion, ControladorUsuario controladorUsuario) {
        this.controladorAgrupacion = controladorAgrupacion;
        this.controladorUsuario = controladorUsuario;

        setTitle("CLARA - Traslado y Gestión de Miembros");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        ComponentesUI.configurarFondo(this);

        // --- PANEL SUPERIOR (Selector de Agrupación y Búsqueda) ---
        JPanel panelSuperior = ComponentesUI.crearPanel();
        panelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        panelSuperior.add(ComponentesUI.crearEtiqueta("Agrupación a Gestionar:"));
        comboAgrupaciones = new JComboBox<>();
        comboAgrupaciones.setFont(ComponentesUI.FUENTE_ETIQUETA);
        comboAgrupaciones.setPreferredSize(new Dimension(250, 35));
        cargarAgrupacionesEnCombo();
        comboAgrupaciones.addActionListener(e -> cargarUsuariosEnTablas());
        panelSuperior.add(comboAgrupaciones);

        panelSuperior.add(Box.createHorizontalStrut(30));

        panelSuperior.add(ComponentesUI.crearEtiqueta("Filtrar Usuarios:"));
        campoBusquedaUsuario = ComponentesUI.crearCampoTexto();
        campoBusquedaUsuario.setPreferredSize(new Dimension(200, 35));
        campoBusquedaUsuario.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { cargarUsuariosEnTablas(); }
            public void removeUpdate(DocumentEvent e) { cargarUsuariosEnTablas(); }
            public void changedUpdate(DocumentEvent e) { cargarUsuariosEnTablas(); }
        });
        panelSuperior.add(campoBusquedaUsuario);

        add(panelSuperior, BorderLayout.NORTH);

        // --- PANEL CENTRAL (Tablas) ---
        JPanel panelCentral = ComponentesUI.crearPanel();
        panelCentral.setLayout(new GridLayout(1, 2, 20, 0));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Tabla de Candidatos (Todos los demás usuarios)
        String[] columnasCandidatos = {"ID", "Matrícula", "Nombre", "Agrupación Actual"};
        modeloTablaCandidatos = new DefaultTableModel(columnasCandidatos, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaCandidatos = new JTable(modeloTablaCandidatos);
        JScrollPane scrollCandidatos = new JScrollPane(tablaCandidatos);
        panelCentral.add(crearPanelTabla("Usuarios Disponibles / De otras Agrupaciones", scrollCandidatos));

        // Tabla de Miembros (Usuarios de la agrupación seleccionada)
        String[] columnasMiembros = {"ID", "Matrícula", "Nombre"};
        modeloTablaMiembros = new DefaultTableModel(columnasMiembros, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaMiembros = new JTable(modeloTablaMiembros);
        JScrollPane scrollMiembros = new JScrollPane(tablaMiembros);
        panelCentral.add(crearPanelTabla("Miembros Actuales de la Agrupación", scrollMiembros));

        add(panelCentral, BorderLayout.CENTER);

        // --- PANEL INFERIOR (Botones de Acción) ---
        JPanel panelInferior = ComponentesUI.crearPanel();
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 15));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));


        JButton botonMover = ComponentesUI.crearBoton("Trasladar a esta Agrupación", e -> trasladarMiembro());
        JButton botonQuitar = ComponentesUI.crearBotonPeligro("Dejar sin Agrupación", e -> quitarMiembro());
        JButton botonCerrar = ComponentesUI.crearBotonPeligro("Cerrar", e -> dispose());

        panelInferior.add(botonMover);
        panelInferior.add(botonQuitar);
        panelInferior.add(botonCerrar);

        add(panelInferior, BorderLayout.SOUTH);

        cargarUsuariosEnTablas();
        setVisible(true);
    }

    private void cargarAgrupacionesEnCombo() {
        comboAgrupaciones.removeAllItems();
        for (Agrupacion ag : controladorAgrupacion.listarAgrupaciones()) {
            comboAgrupaciones.addItem(ag);
        }
    }

    private void cargarUsuariosEnTablas() {
        modeloTablaCandidatos.setRowCount(0);
        modeloTablaMiembros.setRowCount(0);

        Agrupacion agrupacionSeleccionada = (Agrupacion) comboAgrupaciones.getSelectedItem();
        if (agrupacionSeleccionada == null) return;

        String filtro = campoBusquedaUsuario.getText().toLowerCase();
        List<Usuario> todosLosUsuarios = controladorUsuario.listarUsuarios();

        for (Usuario user : todosLosUsuarios) {
            // No mostramos al Admin en la gestión de miembros
            if (user.getRol() == Modelo.RolUsuario.ADMIN) continue;

            boolean esMiembro = user.getIdAgrupacion().equals(agrupacionSeleccionada.getIdAgrupacion());
            boolean coincideFiltro = filtro.isEmpty() ||
                    user.getMatricula().toLowerCase().contains(filtro) ||
                    user.getNombre().toLowerCase().contains(filtro);

            if (coincideFiltro) {
                if (esMiembro) {
                    modeloTablaMiembros.addRow(new Object[]{user.getIdUsuario(), user.getMatricula(), user.getNombre()});
                } else {
                    Agrupacion actual = controladorAgrupacion.buscarPorId(user.getIdAgrupacion());
                    String nombreAgrupacionActual = (actual != null) ? actual.getNombreAgrupacion() : "Ninguna";
                    modeloTablaCandidatos.addRow(new Object[]{user.getIdUsuario(), user.getMatricula(), user.getNombre(), nombreAgrupacionActual});
                }
            }
        }
    }

    private void trasladarMiembro() {
        int filaSeleccionada = tablaCandidatos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para trasladar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idUsuario = (String) modeloTablaCandidatos.getValueAt(filaSeleccionada, 0);
        Usuario usuario = controladorUsuario.buscarUsuarioPorId(idUsuario);
        Agrupacion destino = (Agrupacion) comboAgrupaciones.getSelectedItem();

        if (usuario != null && destino != null) {
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Desea trasladar a " + usuario.getNombre() + " a la agrupación " + destino.getNombreAgrupacion() + "?",
                    "Confirmar Traslado", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                try {
                    controladorAgrupacion.agregarMiembro(destino.getIdAgrupacion(), usuario);
                    JOptionPane.showMessageDialog(this, "Usuario trasladado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarUsuariosEnTablas();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al trasladar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void quitarMiembro() {
        int filaSeleccionada = tablaMiembros.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un miembro para dejar sin agrupación.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idUsuario = (String) modeloTablaMiembros.getValueAt(filaSeleccionada, 0);
        Usuario usuario = controladorUsuario.buscarUsuarioPorId(idUsuario);
        Agrupacion actual = (Agrupacion) comboAgrupaciones.getSelectedItem();

        if (usuario != null && actual != null) {
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Desea quitar a " + usuario.getNombre() + " de la agrupación " + actual.getNombreAgrupacion() + "?",
                    "Confirmar Acción", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                try {
                    controladorAgrupacion.quitarMiembro(actual.getIdAgrupacion(), usuario);
                    JOptionPane.showMessageDialog(this, "Usuario ahora se encuentra sin agrupación.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarUsuariosEnTablas();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
