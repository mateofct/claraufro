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
    private DefaultTableModel modeloTabla;

    public VentanaGestionarAgrupacion(ControladorAgrupacion controladorAgrupacion) {
        this.controladorAgrupacion = controladorAgrupacion;

        setTitle("CLARA - Gestionar Agrupaciones");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(102, 133, 183));

        // Tabla de Agrupaciones
        String[] columnas = {"ID Agrupación", "Nombre", "Saldo Total"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Las celdas no son editables directamente en la tabla
            }
        };
        tablaAgrupaciones = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaAgrupaciones);
        add(scrollPane, BorderLayout.CENTER);

        // Panel Inferior (Botones de Acción)
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelInferior.setBackground(new Color(102, 133, 183));

        JButton botonEditar = ComponentesUI.crearBoton("Editar Agrupación",
                e -> editarAgrupacion());
        panelInferior.add(botonEditar);

        JButton botonEliminar = ComponentesUI.crearBoton("Eliminar Agrupación",
                e -> eliminarAgrupacion());
        panelInferior.add(botonEliminar);

        add(panelInferior, BorderLayout.SOUTH);

        cargarAgrupaciones();
        setVisible(true);
    }

    private void cargarAgrupaciones() {
        modeloTabla.setRowCount(0); // Limpiar tabla
        List<Agrupacion> agrupaciones = controladorAgrupacion.listarAgrupaciones();

        for (Agrupacion agrupacion : agrupaciones) {
            modeloTabla.addRow(new Object[]{
                    agrupacion.getIdAgrupacion(),
                    agrupacion.getNombreAgrupacion(),
                    agrupacion.getSaldoTotal()
            });
        }
    }

    private void editarAgrupacion() {
        int filaSeleccionada = tablaAgrupaciones.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una agrupación de la tabla para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idAgrupacion = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        Agrupacion editando = controladorAgrupacion.buscarPorId(idAgrupacion);

        if (editando == null) {
            JOptionPane.showMessageDialog(this, "Agrupación no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nuevoNombre = JOptionPane.showInputDialog(this, "Ingrese el nuevo nombre para la agrupación:", editando.getNombreAgrupacion());

        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
            try {
                controladorAgrupacion.editarAgrupacion(editando.getIdAgrupacion(), nuevoNombre.trim());
                JOptionPane.showMessageDialog(this, "Agrupación editada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarAgrupaciones(); // Recargar la tabla
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarAgrupacion() {
        int filaSeleccionada = tablaAgrupaciones.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una agrupación de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idAgrupacion = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
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