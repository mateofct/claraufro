package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Vista para crear una agrupación.
 * MVC Puro: No conoce al controlador.
 */
public class VentanaCrearAgrupacion extends JFrame {
    private JTextField nombreAgrupacion;
    private JButton btnGuardar;

    public VentanaCrearAgrupacion() {
        setTitle("CLARA - Crear Agrupación");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        ComponentesUI.configurarFondo(this);

        JPanel panelSuperior = ComponentesUI.crearPanel();
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panelSuperior.add(ComponentesUI.crearTitulo("Nueva Agrupación"));
        add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCentral = ComponentesUI.crearPanel();
        panelCentral.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 40, 10, 40);

        panelCentral.add(ComponentesUI.crearEtiqueta("Nombre de la Agrupación:"), gbc);
        nombreAgrupacion = ComponentesUI.crearCampoTexto();
        panelCentral.add(nombreAgrupacion, gbc);

        add(panelCentral, BorderLayout.CENTER);

        JPanel panelInferior = ComponentesUI.crearPanel();
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 40, 30, 40));
        panelInferior.setLayout(new GridLayout(1, 2, 10, 0));

        btnGuardar = ComponentesUI.crearBoton("Crear", null);
        JButton btnCancelar = ComponentesUI.crearBotonPeligro("Cancelar", e -> dispose());

        panelInferior.add(btnGuardar);
        panelInferior.add(btnCancelar);

        add(panelInferior, BorderLayout.SOUTH);
    }

    public String getNombreAgrupacion() {
        return nombreAgrupacion.getText().trim();
    }

    public void setGuardarListener(ActionListener listener) {
        btnGuardar.addActionListener(listener);
    }
}
