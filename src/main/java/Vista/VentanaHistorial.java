package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.util.List;
import Modelo.Usuario;
import Controlador.ControladorFinanzas;

public class VentanaHistorial extends JFrame {
    private final Usuario usuario;
    private final ControladorFinanzas controladorFinanzas;
    private DefaultTableModel modeloTabla;
    private JTable tablaMovimientos;
    private JFormattedTextField txtFecha;
    private JComboBox<String> cbTipo;

    public VentanaHistorial(ControladorFinanzas controladorFinanzas, Usuario usuario) {
        this.controladorFinanzas = controladorFinanzas;
        this.usuario = usuario;

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

        panelFiltros.add(ComponentesUI.crearBoton("Filtrar", e -> ejecutarBusqueda()));

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

        panelInferior.add(ComponentesUI.crearBoton("Ver Comprobante", e -> abrirComprobante()));
        panelInferior.add(ComponentesUI.crearBoton("Cerrar", e -> dispose()));
        add(panelInferior, BorderLayout.SOUTH);

        ejecutarBusqueda();
        setVisible(true);
    }

    private void ejecutarBusqueda() {
        String fechaRaw = txtFecha.getText().replace("_", "").replace("/", "").trim();
        String fecha = fechaRaw.isEmpty() ? "" : txtFecha.getText();
        String tipo = cbTipo.getSelectedItem().toString();
        if (tipo.equals("Todos")) tipo = "";

        List<String[]> resultados = controladorFinanzas.filtrarMovimientos(usuario.getIdAgrupacion(), fecha, tipo);
        modeloTabla.setRowCount(0);
        for (String[] fila : resultados) {
            modeloTabla.addRow(fila);
        }
    }

    private void abrirComprobante() {
        int fila = tablaMovimientos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un movimiento.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nombre = modeloTabla.getValueAt(fila, 6).toString();
        controladorFinanzas.pedirAbrirComprobante(nombre);
    }
}
