package Vista;

import javax.swing.*;
import java.awt.*;
import Modelo.RolUsuario;
import Modelo.Usuario;
import Controlador.ControladorUsuario;

public class VentanaCrearUsuario extends JFrame {
    private ControladorUsuario controladorUsuario;
    private JTextField EscribirMatricula;
    private JPasswordField EscribirContraseña;
    private JComboBox<String> seleccionarRol;

    public VentanaCrearUsuario(ControladorUsuario controladorUsuario){
        this.controladorUsuario = controladorUsuario;

        setTitle("CLARA - Registrar nuevo usuario");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        ComponentesUI.configurarFondo(this);

        JPanel panelSuperior = ComponentesUI.crearPanel();
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panelSuperior.add(ComponentesUI.crearTitulo("Registro de Usuario"));
        add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCentral = ComponentesUI.crearPanel();
        panelCentral.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 40, 8, 40);

        panelCentral.add(ComponentesUI.crearEtiqueta("Matrícula:"), gbc);
        EscribirMatricula = ComponentesUI.crearCampoTexto();
        panelCentral.add(EscribirMatricula, gbc);

        panelCentral.add(ComponentesUI.crearEtiqueta("Contraseña:"), gbc);
        EscribirContraseña = ComponentesUI.crearCampoContrasena();
        panelCentral.add(EscribirContraseña, gbc);

        panelCentral.add(ComponentesUI.crearEtiqueta("Rol del usuario:"), gbc);
        seleccionarRol = new JComboBox<>(new String[]{"Tesorero", "Socio"});
        seleccionarRol.setFont(new Font("Arial", Font.PLAIN, 14));
        panelCentral.add(seleccionarRol, gbc);

        add(panelCentral, BorderLayout.CENTER);

        JPanel panelInferior = ComponentesUI.crearPanel();
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 40, 30, 40));
        panelInferior.setLayout(new GridLayout(1, 2, 10, 0));

        JButton btnRegistrar = ComponentesUI.crearBoton("Registrar",
                e -> guardarUsuario());
        JButton btnCancelar = ComponentesUI.crearBotonPeligro("Cancelar",
                e -> dispose());

        panelInferior.add(btnRegistrar);
        panelInferior.add(btnCancelar);

        add(panelInferior, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void guardarUsuario() {
        String matricula  = EscribirMatricula.getText().trim();
        String contraseña = new String(EscribirContraseña.getPassword());

        if (matricula.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error de validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String rolSeleccionado = seleccionarRol.getSelectedItem().toString();
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