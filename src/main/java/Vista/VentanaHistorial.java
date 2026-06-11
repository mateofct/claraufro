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

        setTitle("Historial de transacciones");
        setSize(800, 500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(102, 133, 183));

        inicializarPanelFiltros();
        inicializarTabla();
        inicializarPanelBotones();

        // carga de datos mediante el controlador, no se consulta directamente el CSV
        ejecutarBusqueda();

        setVisible(true);
    }

    // render barra de busqeuda
    private void inicializarPanelFiltros() {
        JPanel panelFiltros = new JPanel(new FlowLayout());
        panelFiltros.setBackground(new Color(102, 133, 183));

        JLabel lblFecha = new JLabel("Fecha (DD/MM/YYYY):");
        lblFecha.setForeground(Color.WHITE);
        panelFiltros.add(lblFecha);

        try {
            MaskFormatter formatoFecha = new MaskFormatter("##/##/####");
            formatoFecha.setPlaceholderCharacter('_');
            txtFecha = new JFormattedTextField(formatoFecha);
            txtFecha.setPreferredSize(new Dimension(100, 25));
        } catch (ParseException e) {
            txtFecha = new JFormattedTextField();
        }
        panelFiltros.add(txtFecha);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setForeground(Color.WHITE);
        panelFiltros.add(lblTipo);

        cbTipo = new JComboBox<>(new String[]{"Todos", "INGRESO", "EGRESO"});
        panelFiltros.add(cbTipo);

        JButton btnBuscar = new JButton("Filtrar");
        btnBuscar.addActionListener(e -> ejecutarBusqueda());
        panelFiltros.add(btnBuscar);

        JLabel titulo = new JLabel("Historial de transacciones");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(new Color(30, 100, 200));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setOpaque(false);
        panelNorte.add(titulo, BorderLayout.NORTH);
        panelNorte.add(panelFiltros, BorderLayout.SOUTH);
        add(panelNorte, BorderLayout.NORTH);
    }

    private void inicializarTabla() {
        String[] columnas = {"ID", "Agrupación", "Tipo", "Monto", "Fecha", "Descripción", "Comprobante", "Usuario"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaMovimientos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaMovimientos);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void inicializarPanelBotones() {
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelAcciones.setBackground(new Color(102, 133, 183));

        JButton btnVerComprobante = new JButton("Ver Comprobante");
        btnVerComprobante.setFont(new Font("Arial", Font.PLAIN, 13));
        btnVerComprobante.setBackground(Color.WHITE);
        btnVerComprobante.addActionListener(e -> abrirComprobanteSeleccionado());
        panelAcciones.add(btnVerComprobante);

        JButton botonCerrar = new JButton("Cerrar");
        botonCerrar.setFont(new Font("Arial", Font.PLAIN, 13));
        botonCerrar.setBackground(Color.WHITE);
        botonCerrar.addActionListener(e -> dispose());
        panelAcciones.add(botonCerrar);

        add(panelAcciones, BorderLayout.SOUTH);
    }

    // logica para pedirle al controlador los datos
    private void ejecutarBusqueda() {
        String fechaRaw = txtFecha.getText().replace("_", "").replace("/", "").trim();
        String fechaBuscada = fechaRaw.isEmpty() ? "" : txtFecha.getText();

        String tipoSeleccionado = cbTipo.getSelectedItem().toString();
        if (tipoSeleccionado.equals("Todos")) {
            tipoSeleccionado = "";
        }

        List<String[]> resultados = controladorFinanzas.filtrarMovimientos(usuario.getIdAgrupacion(), fechaBuscada, tipoSeleccionado);
        modeloTabla.setRowCount(0);

        for (String[] fila : resultados) {
            modeloTabla.addRow(fila);
        }

        if (resultados.isEmpty() && !fechaBuscada.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron movimientos.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // logica para que controlador abra comprobante
    private void abrirComprobanteSeleccionado() {
        int filaSeleccionada = tablaMovimientos.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un movimiento en la tabla primero.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombreComprobante = modeloTabla.getValueAt(filaSeleccionada, 6).toString();
        controladorFinanzas.pedirAbrirComprobante(nombreComprobante);
    }
}