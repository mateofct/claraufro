package Vista;

import javax.swing.*;
import java.awt.*;
import Modelo.RolUsuario;
import Modelo.Usuario;
import Controlador.ControladorUsuario;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaRegistrarUsuario extends JFrame {
    private ControladorUsuario controladorUsuario;
    private JTextField EscribirMatricula;
    private JPasswordField EscribirContraseña;

    private JComboBox<String> selectorRol;

    public VentanaRegistrarUsuario(ControladorUsuario controladorUsuario){
        this.controladorUsuario = controladorUsuario;

        setTitle("Registrar nuevo usuario");
        setSize(400, 500);
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
        etiquetaMatricula.setBounds(20, 70, 340, 20);
        add(etiquetaMatricula);

        EscribirMatricula = new JTextField();
        EscribirMatricula.setBounds(20, 92, 340, 35);
        add(EscribirMatricula);

        JLabel etiquetaContraseña = new JLabel("Contraseña:");
        etiquetaContraseña.setFont(new Font("Arial", Font.BOLD, 13));
        etiquetaContraseña.setBounds(20, 170, 340, 20);
        add(etiquetaContraseña);

        EscribirContraseña = new JPasswordField();
        EscribirContraseña.setBounds(20, 190, 340, 35);
        add(EscribirContraseña);

        JLabel etiquetaRol = new JLabel("Rol del usuario:");
        etiquetaRol.setFont(new Font("Arial", Font.BOLD, 13));
        etiquetaRol.setBounds(20, 274, 200, 20);
        add(etiquetaRol);

        selectorRol = new JComboBox<>(new String[]{"Tesorero", "Socio"});
        selectorRol.setBounds(20, 296, 340, 35);
        add(selectorRol);

        JButton botonGuardar = new JButton("Registrar usuario");
        botonGuardar.setBackground(new Color(30, 100, 200));
        botonGuardar.setForeground(Color.WHITE);
        botonGuardar.setBounds(20, 360, 160, 38);

        botonGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarUsuario();
            }
        });
        add(botonGuardar);

        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.setBackground(Color.WHITE);
        botonCancelar.setBounds(200, 360, 160, 38);

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
        String contraseña = new String(EscribirContraseña.getPassword());

        if (matricula.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error de validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String rolSeleccionado = selectorRol.getSelectedItem().toString();
        RolUsuario rol = rolSeleccionado.equals("Tesorero") ? RolUsuario.TESORERO : RolUsuario.SOCIO;

        try {
            Usuario creado = controladorUsuario.registrarUsuario("agrup-001", rol, contraseña, matricula);
            JOptionPane.showMessageDialog(this, "Usuario '" + creado.getNombre() + "' registrado correctamente");
            dispose();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error al registrar", JOptionPane.ERROR_MESSAGE);
        }
    }
}