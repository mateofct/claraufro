package Vista;

import Modelo.Agrupacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Vista para gestionar agrupaciones.
 * MVC Puro: No conoce al controlador.
 */
public class VentanaGestionarAgrupacion extends JFrame {
    private JTable tablaAgrupaciones;
    private DefaultTableModel modeloTablaAgrupaciones;
    private JTextField campoNombreAgrupacion;
    private JButton btnGuardarCambios;
    private JButton btnEliminar;

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

    public void cargarAgrupaciones(List<Agrupacion> agrupaciones) {
        modeloTablaAgrupaciones.setRowCount(0);
        for (Agrupacion a : agrupaciones) {
            modeloTablaAgrupaciones.addRow(new Object[]{a.getIdAgrupacion(), a.getNombreAgrupacion()});
        }
    }

    public String getIdSeleccionado() {
        int fila = tablaAgrupaciones.getSelectedRow();
        return (fila != -1) ? (String) modeloTablaAgrupaciones.getValueAt(fila, 0) : null;
    }

    public String getNombreAgrupacion() {
        return campoNombreAgrupacion.getText().trim();
    }

    public void setNombreAgrupacion(String nombre) {
        campoNombreAgrupacion.setText(nombre);
    }

    public void setSeleccionListener(javax.swing.event.ListSelectionListener listener) {
        tablaAgrupaciones.getSelectionModel().addListSelectionListener(listener);
    }

    public void setGuardarListener(ActionListener listener) {
        btnGuardarCambios.addActionListener(listener);
    }

    public void setEliminarListener(ActionListener listener) {
        btnEliminar.addActionListener(listener);
    }
}
