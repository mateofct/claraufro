package Vista;
import javax.swing.*;
import java.awt.*;
import Modelo.Usuario;
import Controlador.ControladorFinanzas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaSaldo extends JFrame {
    private ControladorFinanzas controladorFinanzas;
    private Usuario usuario;

    public VentanaSaldo(ControladorFinanzas controladorFinanzas, Usuario usuario) {
        this.controladorFinanzas = controladorFinanzas;
        this.usuario = usuario;
        int saldo    = controladorFinanzas.calcularSaldo(usuario.getIdAgrupacion());

        setTitle("Saldo de la agrupación");
        setSize(380, 320);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(102, 133, 183));

        JLabel titulo = new JLabel("Saldo de la agrupación");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(new Color(30, 100, 200));
        titulo.setBounds(20, 15, 330, 30);
        add(titulo);

        JLabel valorSaldo = new JLabel("$" + saldo);
        valorSaldo.setFont(new Font("Arial", Font.BOLD, 36));
        valorSaldo.setForeground(Color.BLACK);
        valorSaldo.setBounds(20, 50, 280, 50);
        add(valorSaldo);

        JButton botonCerrar = new JButton("Cerrar");
        botonCerrar.setFont(new Font("Arial", Font.PLAIN, 13));
        botonCerrar.setBackground(Color.WHITE);
        botonCerrar.setBounds(20, 120, 280, 35);

        botonCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        add(botonCerrar);
        setVisible(true);
    }


}
