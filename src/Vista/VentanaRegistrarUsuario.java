package Vista;
import javax.swing.*;
import java.awt.*;
import Modelo.RolUsuario;
import Controlador.ControladorUsuario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaRegistrarUsuario extends JFrame{
    private ControladorUsuario controladorUsuario;
    private JTextField EscribirMatricula;
    private JTextField EscribirNombre;
    private JPasswordField EscribirContraseña;
    private JTextField EscribirNumeroRol;

    public VentanaRegistrarUsuario(ControladorUsuario controladorUsuario){
        this.controladorUsuario = controladorUsuario;

        setTitle("Registrar nuevo usuario");
        setSize(400, 480);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(102, 133, 183));

        JLabel titulo = new JLabel("Registrar nuevo usuario");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(new Color(30, 100, 200));
        titulo.setBounds(20, 15, 340, 30);
        add(titulo);

        JLabel etiquetaMatricula = new JLabel("Matrícula:");
        etiquetaMatricula.setFont(new Font("Arial", Font.BOLD, 13));
        etiquetaMatricula.setBounds(20, 80, 340, 20);
        add(etiquetaMatricula);

        EscribirMatricula = new JTextField();
        EscribirMatricula.setBounds(20, 102, 340, 35);
        add(EscribirMatricula);

        JLabel etiquetaNombre = new JLabel("Nombre completo:");
        etiquetaNombre.setFont(new Font("Arial", Font.BOLD, 13));
        etiquetaNombre.setBounds(20, 148, 340, 20);
        add(etiquetaNombre);

        EscribirNombre = new JTextField();
        EscribirNombre.setBounds(20, 170, 340, 35);
        add(EscribirNombre);

        JLabel etiquetaContraseña = new JLabel("Contraseña:");
        etiquetaContraseña.setFont(new Font("Arial", Font.BOLD, 13));
        etiquetaContraseña.setBounds(20, 216, 340, 20);
        add(etiquetaContraseña);

        EscribirContraseña = new JPasswordField();
        EscribirContraseña.setBounds(20, 238, 340, 35);
        add(EscribirContraseña);

        JLabel etiquetaRol = new JLabel("-----Rol del usuario-----\n 1. Tesorero\n 2. Socio \n Escriba el número del Rol");
        etiquetaRol.setFont(new Font("Arial", Font.BOLD, 13));
        etiquetaRol.setBounds(20, 352, 200, 20);
        add(etiquetaRol);

        EscribirNumeroRol = new JTextField();
        EscribirNumeroRol.setBounds(20, 374, 340, 35);
        add(EscribirNumeroRol);


        JButton botonGuardar = new JButton("Registrar usuario");
        botonGuardar.setBackground(new Color(30, 100, 200));
        botonGuardar.setForeground(Color.WHITE);
        botonGuardar.setBounds(20, 415, 160, 38);

        botonGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarUsuario();
            }
        });
        add(botonGuardar);

        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.setBackground(Color.WHITE);
        botonCancelar.setBounds(200, 415, 160, 38);

        botonCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        add(botonCancelar);
        setVisible(true);

    }
    private void guardarUsuario() {
        String matricula  = EscribirMatricula.getText().trim();
        String nombre     = EscribirNombre.getText().trim();
        String contraseña = new String(EscribirContraseña.getPassword());
        String numeroRol = EscribirNumeroRol.getText().trim();
        RolUsuario rol;
        if (numeroRol.equals("1")) {
            rol = RolUsuario.TESORERO;
        } else {
            rol = RolUsuario.SOCIO;
        }
        controladorUsuario.registrarUsuario("agrup-001", nombre, contraseña, rol, matricula);
        JOptionPane.showMessageDialog(this, "Usuario '" + nombre + "' registrado correctamente");
        dispose();
    }



}
