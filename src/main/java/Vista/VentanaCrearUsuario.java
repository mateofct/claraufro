package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import Modelo.RolUsuario;

/**
 * Vista para registrar un nuevo usuario.
 * MVC Puro: No conoce al controlador. Expone métodos para obtener datos y asignar listeners.
 */
public class VentanaCrearUsuario extends JFrame {
    private JTextField EscribirMatricula;
    private JPasswordField EscribirContraseña;
    private JComboBox<String> seleccionarRol;
    private JButton btnRegistrar;

    public VentanaCrearUsuario(){
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

        btnRegistrar = ComponentesUI.crearBoton("Registrar", null);
        JButton btnCancelar = ComponentesUI.crearBotonPeligro("Cancelar", e -> dispose());

        panelInferior.add(btnRegistrar);
        panelInferior.add(btnCancelar);

        add(panelInferior, BorderLayout.SOUTH);
    }

    public String getMatricula() {
        return EscribirMatricula.getText().trim();
    }

    public String getContrasena() {
        return new String(EscribirContraseña.getPassword());
    }

    public RolUsuario getRolSeleccionado() {
        String rol = seleccionarRol.getSelectedItem().toString();
        return rol.equals("Tesorero") ? RolUsuario.TESORERO : RolUsuario.SOCIO;
    }

    public void setRegistrarListener(ActionListener listener) {
        btnRegistrar.addActionListener(listener);
    }
}
