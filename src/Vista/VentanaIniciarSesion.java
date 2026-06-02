package Vista;

import javax.swing.*;
import java.awt.*;
import Controlador.ControladorFinanzas;
import Controlador.ControladorUsuario;
import Modelo.RolUsuario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class VentanaIniciarSesion extends JFrame {

    private JTextField EscribirMatricula;
    private JPasswordField EscribirContraseña;
    private ControladorFinanzas controladorFinanzas;
    private ControladorUsuario controladorUsuario;

    public VentanaIniciarSesion(ControladorFinanzas controladorFinanzas, ControladorUsuario controladorUsuario) {
        this.controladorFinanzas = controladorFinanzas;
        this.controladorUsuario = controladorUsuario;
        setTitle("Iniciar Sesión - CLARA");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(new Color(102, 133, 183));

        JLabel titulo = new JLabel("CLARA");
        titulo.setFont(new Font("ROG Fonts Normal", Font.BOLD, 24));
        titulo.setBounds(160, 20, 200, 40);
        add(titulo);

        JLabel subtitulo = new JLabel("Iniciar Sesión");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitulo.setForeground(Color.GRAY);
        subtitulo.setBounds(90, 60, 250, 20);
        add(subtitulo);

        JLabel etiquetaMatricula = new JLabel("Matrícula:");
        etiquetaMatricula.setFont(new Font("Arial", Font.BOLD, 14));
        etiquetaMatricula.setBounds(60, 110, 100, 25);
        add(etiquetaMatricula);

        EscribirMatricula = new JTextField();
        EscribirMatricula.setFont(new Font("Arial", Font.PLAIN, 14));
        EscribirMatricula.setBounds(60, 135, 270, 35);
        add(EscribirMatricula);

        JLabel etiquetaContraseña = new JLabel("Contraseña:");
        etiquetaContraseña.setFont(new Font("Arial", Font.BOLD, 14));
        etiquetaContraseña.setBounds(60, 185, 120, 25);
        add(etiquetaContraseña);

        EscribirContraseña = new JPasswordField();
        EscribirContraseña.setFont(new Font("Arial", Font.PLAIN, 14));
        EscribirContraseña.setBounds(60, 210, 270, 35);
        add(EscribirContraseña);

        JButton botonIngresar = new JButton("Iniciar Sesion");
        botonIngresar.setFont(new Font("Arial", Font.BOLD, 14));
        botonIngresar.setBackground(new Color(225, 228, 237)); // fondo azul
        botonIngresar.setForeground(Color.black);
        botonIngresar.setBounds(60, 265, 270, 40);

        botonIngresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evento) {
                intentarLogin();
            }
        });
        add(botonIngresar);
        setVisible(true);


    }

    private void intentarLogin() {
        String matricula = EscribirMatricula.getText();
        String contraseña = new String(EscribirContraseña.getPassword());

        if (matricula.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Los campos no deben de estar vacíos");
            return;
        }
        boolean loginCorrecto = controladorUsuario.iniciarSesion(matricula, contraseña);
        if(loginCorrecto==false) {
            JOptionPane.showMessageDialog(this,"Matrícula o contraseña incorrecta");
            EscribirContraseña.setText("");
        } else {
            dispose();
            RolUsuario rol = controladorUsuario.getUsuarioActivo().getRol();

            if(rol==RolUsuario.ADMIN){
                new VentanaMenuAdmin(controladorUsuario,controladorFinanzas);
                
            } else if (rol==RolUsuario.TESORERO) {
                new VentanaMenuTesorero(controladorUsuario,controladorFinanzas);
                
            } else if (rol==RolUsuario.SOCIO) {
                new VentanaMenuSocio(controladorUsuario,controladorFinanzas);
                
            }
        }
    }


}


