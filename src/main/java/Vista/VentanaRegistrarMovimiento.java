package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Vista para registrar un movimiento financiero.
 * MVC Puro: No conoce al controlador.
 */
public class VentanaRegistrarMovimiento extends JFrame {
    private boolean esIngreso;
    private JTextField campoMonto;
    private JTextField campoDescripcion;
    private File archivoComprobante = null;
    private JButton botonSubirArchivo;
    private JButton botonGuardar;

    public VentanaRegistrarMovimiento(boolean esIngreso, int saldoActual) {
        this.esIngreso = esIngreso;

        setTitle(esIngreso ? "CLARA - Registrar Ingreso" : "CLARA - Registrar Egreso");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        ComponentesUI.configurarFondo(this);

        // --- PANEL SUPERIOR ---
        JPanel panelSuperior = ComponentesUI.crearPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel titulo = ComponentesUI.crearSubtitulo(esIngreso ? "Registrar Ingreso" : "Registrar Egreso");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSuperior.add(titulo);

        JLabel etiquetaSaldo = new JLabel("Saldo actual: $" + saldoActual);
        etiquetaSaldo.setFont(new Font("Arial", Font.ITALIC, 13));
        etiquetaSaldo.setForeground(Color.WHITE);
        etiquetaSaldo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSuperior.add(etiquetaSaldo);

        add(panelSuperior, BorderLayout.NORTH);

        // --- PANEL CENTRAL ---
        JPanel panelCentral = ComponentesUI.crearPanel();
        panelCentral.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 40, 5, 40);

        panelCentral.add(ComponentesUI.crearEtiqueta("Monto ($):"), gbc);
        campoMonto = ComponentesUI.crearCampoTexto();
        panelCentral.add(campoMonto, gbc);

        gbc.insets = new Insets(15, 40, 5, 40);
        panelCentral.add(ComponentesUI.crearEtiqueta("Descripción:"), gbc);
        campoDescripcion = ComponentesUI.crearCampoTexto();
        panelCentral.add(campoDescripcion, gbc);

        gbc.insets = new Insets(15, 40, 5, 40);
        panelCentral.add(ComponentesUI.crearEtiqueta("Comprobante (PDF):"), gbc);
        botonSubirArchivo = ComponentesUI.crearBoton("Seleccionar Archivo", e -> seleccionarArchivo());
        panelCentral.add(botonSubirArchivo, gbc);

        add(panelCentral, BorderLayout.CENTER);

        // --- PANEL INFERIOR ---
        JPanel panelInferior = ComponentesUI.crearPanel();
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 40, 40, 40));
        panelInferior.setLayout(new GridLayout(1, 2, 15, 0));

        botonGuardar = ComponentesUI.crearBoton(esIngreso ? "Guardar Ingreso" : "Guardar Egreso", null);
        if (esIngreso) {
            botonGuardar.setBackground(new Color(40, 167, 69));
            botonGuardar.setForeground(Color.WHITE);
        } else {
            botonGuardar.setBackground(new Color(220, 53, 69));
            botonGuardar.setForeground(Color.WHITE);
        }

        JButton botonCancelar = ComponentesUI.crearBoton("Cancelar", e -> dispose());

        panelInferior.add(botonGuardar);
        panelInferior.add(botonCancelar);

        add(panelInferior, BorderLayout.SOUTH);
    }

    private void seleccionarArchivo() {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Seleccione el comprobante");
        int resultado = selector.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            archivoComprobante = selector.getSelectedFile();
            botonSubirArchivo.setText(archivoComprobante.getName());
            botonSubirArchivo.setBackground(new Color(200, 255, 200));
        }
    }

    public String getMonto() {
        return campoMonto.getText().trim();
    }

    public String getDescripcion() {
        return campoDescripcion.getText().trim();
    }

    public String getRutaComprobante() {
        return (archivoComprobante != null) ? archivoComprobante.getAbsolutePath() : "";
    }

    public void setGuardarListener(ActionListener listener) {
        botonGuardar.addActionListener(listener);
    }
}