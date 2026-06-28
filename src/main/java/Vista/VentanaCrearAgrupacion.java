package Vista;

import javax.swing.*;
import java.awt.*;
import Controlador.ControladorAgrupacion;

public class VentanaCrearAgrupacion extends JFrame {
    private ControladorAgrupacion controladorAgrupacion;
    private JTextField escribirNombre;

    public VentanaCrearAgrupacion(ControladorAgrupacion controladorAgrupacion) {
        this.controladorAgrupacion = controladorAgrupacion;

        setTitle("Crear nueva agrupación");
        setSize(400, 220);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(102, 133, 183));

        JLabel titulo = new JLabel("Crear nueva agrupación");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setBounds(20, 15, 340, 30);
        add(titulo);

        JLabel etiquetaNombre = new JLabel("Nombre de la agrupación:");
        etiquetaNombre.setFont(new Font("Arial", Font.BOLD, 13));
        etiquetaNombre.setBounds(20, 60, 340, 20);
        add(etiquetaNombre);

        escribirNombre = new JTextField();
        escribirNombre.setBounds(20, 82, 340, 35);
        add(escribirNombre);

        JButton botonCrear = new JButton("Crear agrupación");
        botonCrear.setBackground(new Color(30, 100, 200));
        botonCrear.setForeground(Color.WHITE);
        botonCrear.setBounds(20, 140, 160, 38);
        botonCrear.addActionListener(e -> crear());
        add(botonCrear);

        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.setBackground(Color.WHITE);
        botonCancelar.setBounds(200, 140, 160, 38);
        botonCancelar.addActionListener(e -> dispose());
        add(botonCancelar);

        setVisible(true);
    }

    private void crear() {
        String nombre = escribirNombre.getText().trim();
        try {
            controladorAgrupacion.crearAgrupacion(nombre);
            JOptionPane.showMessageDialog(this, "Agrupación '" + nombre + "' creada.");
            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al crear la agrupación", JOptionPane.ERROR_MESSAGE);
        }
    }
}