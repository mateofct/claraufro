package Vista;

import Controlador.ControladorAgrupacion;
import Modelo.Agrupacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaGestionarAgrupacion extends JFrame {
    private ControladorAgrupacion controladorAgrupacion;
    private JTable tablaAgrupaciones;
    private DefaultTableModel modeloTablaAgrupaciones;
    private JTextField campoNombreAgrupacion;

    public VentanaGestionarAgrupacion(ControladorAgrupacion controladorAgrupacion) {
        this.controladorAgrupacion = controladorAgrupacion;

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
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaAgrupaciones = new JTable(modeloTablaAgrupaciones);
        tablaAgrupaciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaAgrupaciones.getSelectionModel().addListSelectionListener(
                e -> {
                   if (!e.getValueIsAdjusting() && tablaAgrupaciones.getSelectedRow() != -1) {
                       cargarDatosAgrupacionSeleccionada();
                   }
                });
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
        JButton btnGuardarCambios = ComponentesUI.crearBoton("EDITAR Nombre",
                e -> guardarCambios());
        JButton btnEliminar = ComponentesUI.crearBotonPeligro("Eliminar Agrupación",
                e -> eliminarAgrupacion());
        panelBotonesEdicion.add(btnGuardarCambios);
        panelBotonesEdicion.add(btnEliminar);
        gbc.insets = new Insets(15, 0, 5, 0);
        panelEdicion.add(panelBotonesEdicion, gbc);

        panelCentral.add(panelEdicion, BorderLayout.SOUTH);

        add(panelCentral, BorderLayout.CENTER);

        JPanel panelInferior = ComponentesUI.crearPanel();
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        panelInferior.setLayout(new BorderLayout());
        JButton botonCerrar = ComponentesUI.crearBotonPeligro("Cerrar",
                e -> dispose());
        panelInferior.add(botonCerrar, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        cargarAgrupaciones();
        setVisible(true);
    }

    private void cargarAgrupaciones() {
        modeloTablaAgrupaciones.setRowCount(0); // Limpiar tabla
        List<Agrupacion> agrupaciones = controladorAgrupacion.listarAgrupaciones();
        for (Agrupacion agrupacion : agrupaciones) {
            modeloTablaAgrupaciones.addRow(new Object[]{
                    agrupacion.getIdAgrupacion(),
                    agrupacion.getNombreAgrupacion()
                    // Para agregar los saldos si queremos mejorar la tabla, debería ser aquí
            });
        }
    }

    private void cargarDatosAgrupacionSeleccionada() {
        int filaSeleccionada = tablaAgrupaciones.getSelectedRow();
        if (filaSeleccionada != -1) {
            String nombreActual = (String) modeloTablaAgrupaciones.getValueAt(filaSeleccionada, 1);
            campoNombreAgrupacion.setText(nombreActual);
        }
    }

    private void guardarCambios() {
        int filaSeleccionada = tablaAgrupaciones.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una agrupacion para editar.", "Aviso!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idAgrupacion = (String) modeloTablaAgrupaciones.getValueAt(filaSeleccionada, 0);
        String nuevoNombre = campoNombreAgrupacion.getText();

        if (nuevoNombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre de la agrupación no puede estar vacio.", "Aviso!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            controladorAgrupacion.editarAgrupacion(idAgrupacion, nuevoNombre);
            JOptionPane.showMessageDialog(this, "Agrupación actualizada exitosamente,", "Exito",  JOptionPane.INFORMATION_MESSAGE);
            cargarAgrupaciones();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarAgrupacion() {
        int filaSeleccionada = tablaAgrupaciones.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una agrupación de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idAgrupacion = (String) modeloTablaAgrupaciones.getValueAt(filaSeleccionada, 0);
        Agrupacion agrupacionAEliminar = controladorAgrupacion.buscarPorId(idAgrupacion);

        if (agrupacionAEliminar == null) {
            JOptionPane.showMessageDialog(this, "Agrupación no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar la agrupación " + agrupacionAEliminar.getNombreAgrupacion() + "? Esta acción es irreversible y eliminará todos sus movimientos y miembros asociados.", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                controladorAgrupacion.eliminarAgrupacion(agrupacionAEliminar.getIdAgrupacion());
                JOptionPane.showMessageDialog(this, "Agrupación eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarAgrupaciones(); // Recargar la tabla
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}