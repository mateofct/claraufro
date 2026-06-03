package Vista;
import javax.swing.*;
import java.awt.*;
import Modelo.Usuario;
import Controlador.ControladorFinanzas;
import Controlador.GestorArchivosCSV;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaHistorial extends JFrame{

    private final Usuario usuario;
    private final ControladorFinanzas controladorFinanzas;

    public VentanaHistorial(ControladorFinanzas controladorFinanzas, Usuario usuario) {
        this.controladorFinanzas = controladorFinanzas;
        this.usuario = usuario;

        setTitle("Historial de transacciones");
        setSize(500, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(102, 133, 183));

        JLabel titulo = new JLabel("Historial de transacciones");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(new Color(30, 100, 200));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(titulo, BorderLayout.NORTH);

        JTextArea areaHistorial = new JTextArea();
        areaHistorial.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaHistorial);
        add(scrollPane, BorderLayout.CENTER);

        List<String[]> historial = GestorArchivosCSV.leerLineasMovimientos(usuario.getIdAgrupacion());
        for (String[] linea : historial) {
            areaHistorial.append("[" + linea[4] + "] " + linea[2] + " por $" + linea[3] + " | " + linea[5] + " (Comprobante: " + linea[6] + ")\n");
        }

        JButton botonCerrar = new JButton("Cerrar");
        botonCerrar.setFont(new Font("Arial", Font.PLAIN, 13));
        botonCerrar.setBackground(Color.WHITE);
        botonCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        add(botonCerrar, BorderLayout.SOUTH);
        setVisible(true);
    }
}
