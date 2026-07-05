package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.List;

/**
 * Vista para el historial de transacciones.
 * MVC Puro: No conoce al controlador.
 */
public class VentanaHistorial extends JFrame {
    private DefaultTableModel modeloTabla;
    private JTable tablaMovimientos;
    private JFormattedTextField txtFecha;
    private JComboBox<String> cbTipo;
    private JButton btnFiltrar;
    private JButton btnVerComprobante;

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

    public String getFechaFiltro() {
        String fechaRaw = txtFecha.getText().replace("_", "").replace("/", "").trim();
        return fechaRaw.isEmpty() ? "" : txtFecha.getText();
    }

    public String getTipoFiltro() {
        String tipo = cbTipo.getSelectedItem().toString();
        return tipo.equals("Todos") ? "" : tipo;
    }

    public void cargarMovimientos(List<String[]> movimientos) {
        modeloTabla.setRowCount(0);
        for (String[] fila : movimientos) {
            modeloTabla.addRow(fila);
        }
    }

    public String getNombreComprobanteSeleccionado() {
        int fila = tablaMovimientos.getSelectedRow();
        if (fila == -1) return null;
        return modeloTabla.getValueAt(fila, 6).toString();
    }

    public void setFiltrarListener(ActionListener listener) {
        btnFiltrar.addActionListener(listener);
    }

    public void setVerComprobanteListener(ActionListener listener) {
        btnVerComprobante.addActionListener(listener);
    }
}
