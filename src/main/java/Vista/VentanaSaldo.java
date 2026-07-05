package Vista;

import javax.swing.*;
import java.awt.*;

/**
 * Vista para mostrar el saldo.
 * MVC Puro: No conoce al controlador. Recibe los datos ya calculados.
 */
public class VentanaSaldo extends JFrame {

    public VentanaSaldo(int saldo) {
        setTitle("CLARA - Saldo");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        ComponentesUI.configurarFondo(this);

        // --- PANEL SUPERIOR ---
        JPanel panelSuperior = ComponentesUI.crearPanel();
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(30, 20, 10, 20));
        panelSuperior.add(ComponentesUI.crearSubtitulo("Saldo de la agrupación"));
        add(panelSuperior, BorderLayout.NORTH);

        // --- PANEL CENTRAL ---
        JPanel panelCentral = ComponentesUI.crearPanel();
        panelCentral.setLayout(new GridBagLayout());
        JLabel valorSaldo = new JLabel("$" + saldo);
        valorSaldo.setFont(new Font("Arial", Font.BOLD, 48));
        valorSaldo.setForeground(Color.WHITE);
        panelCentral.add(valorSaldo);
        add(panelCentral, BorderLayout.CENTER);

        // --- PANEL INFERIOR ---
        JPanel panelInferior = ComponentesUI.crearPanel();
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 50, 40, 50));
        panelInferior.setLayout(new BorderLayout());
        JButton btnCerrar = ComponentesUI.crearBotonPeligro("Cerrar", e -> dispose());
        panelInferior.add(btnCerrar, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }
}