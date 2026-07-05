package Vista;

import Controlador.ControladorUsuario;
import Modelo.Usuario;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaGestionarUsuario extends JFrame {
    private ControladorUsuario controladorUsuario;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTablaUsuarios;
    private JTextField campoBusqueda;
    private JTextField campoMatriculaEdicion;
    private JTextField campoNombreEdicion;
    private JComboBox<Modelo.RolUsuario> comboRolEdicion;

    public VentanaGestionarUsuario(ControladorUsuario controladorUsuario) {
        this.controladorUsuario = controladorUsuario;

        setTitle("CLARA - Gestionar Usuarios");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        ComponentesUI.configurarFondo(this);

        JPanel panelSuperior = ComponentesUI.crearPanel();
        panelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        panelSuperior.add(ComponentesUI.crearEtiqueta("Buscar (Matrícula/Nombre):"));
        campoBusqueda = ComponentesUI.crearCampoTexto();
        campoBusqueda.setPreferredSize(new Dimension(200, 30));
        campoBusqueda.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { cargarUsuarios(); }
            public void removeUpdate(DocumentEvent e) { cargarUsuarios(); }
            public void changedUpdate(DocumentEvent e) { cargarUsuarios(); }
        });
        panelSuperior.add(campoBusqueda);

        add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCentral = ComponentesUI.crearPanel();
        panelCentral.setLayout(new BorderLayout(10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        String[] columnas = {"ID", "Matrícula", "Nombre", "Rol", "Agrupación"};
        modeloTablaUsuarios = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaUsuarios = new JTable(modeloTablaUsuarios);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaUsuarios.getSelectedRow() != -1) {
                cargarDatosUsuarioSeleccionado();
            }
        });
        JScrollPane scrollTabla = new JScrollPane(tablaUsuarios);
        panelCentral.add(scrollTabla, BorderLayout.CENTER);

        JPanel panelEdicion = ComponentesUI.crearPanel();
        panelEdicion.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        panelEdicion.add(ComponentesUI.crearEtiqueta("Matrícula:"), gbc);
        campoMatriculaEdicion = ComponentesUI.crearCampoTexto();
        campoMatriculaEdicion.setEditable(false); // La matrícula no se edita
        panelEdicion.add(campoMatriculaEdicion, gbc);

        panelEdicion.add(ComponentesUI.crearEtiqueta("Nombre:"), gbc);
        campoNombreEdicion = ComponentesUI.crearCampoTexto();
        campoNombreEdicion.setEditable(false);
        panelEdicion.add(campoNombreEdicion, gbc);

        panelEdicion.add(ComponentesUI.crearEtiqueta("Rol:"), gbc);
        comboRolEdicion = new JComboBox<>(Modelo.RolUsuario.values());
        comboRolEdicion.setFont(new Font("Arial", Font.PLAIN, 14));
        panelEdicion.add(comboRolEdicion, gbc);

        JPanel panelBotonesEdicion = ComponentesUI.crearPanel();
        panelBotonesEdicion.setLayout(new GridLayout(1, 2, 10, 0));
        JButton botonGuardarCambios = ComponentesUI.crearBoton("Guardar Cambios", e -> guardarCambios());
        JButton botonEliminar = ComponentesUI.crearBotonPeligro("Eliminar Usuario", e -> eliminarUsuario());
        panelBotonesEdicion.add(botonGuardarCambios);
        panelBotonesEdicion.add(botonEliminar);
        gbc.insets = new Insets(15, 0, 5, 0);
        panelEdicion.add(panelBotonesEdicion, gbc);

        panelCentral.add(panelEdicion, BorderLayout.SOUTH);

        add(panelCentral, BorderLayout.CENTER);
        
        JPanel panelInferior = ComponentesUI.crearPanel();
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        panelInferior.setLayout(new BorderLayout());
        JButton botonCerrar = ComponentesUI.crearBotonPeligro("Cerrar", e -> dispose());
        panelInferior.add(botonCerrar, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        cargarUsuarios();
        setVisible(true);
    }

    private void cargarUsuarios() {
        modeloTablaUsuarios.setRowCount(0);
        String filtro = campoBusqueda.getText().toLowerCase();
        List<Usuario> usuarios = controladorUsuario.listarUsuarios();

        for (Usuario user : usuarios) {
            if (filtro.isEmpty() || user.getMatricula().toLowerCase().contains(filtro) || user.getNombre().toLowerCase().contains(filtro)) {
                modeloTablaUsuarios.addRow(new Object[]{user.getIdUsuario(), user.getMatricula(), user.getNombre(), user.getRol(), user.getIdAgrupacion()});
            }
        }
    }

    private void cargarDatosUsuarioSeleccionado() {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        if (filaSeleccionada != -1) {
            String idUsuario = (String) modeloTablaUsuarios.getValueAt(filaSeleccionada, 0);
            Usuario usuario = controladorUsuario.buscarUsuarioPorId(idUsuario);
            if (usuario != null) {
                campoMatriculaEdicion.setText(usuario.getMatricula());
                campoNombreEdicion.setText(usuario.getNombre());
                comboRolEdicion.setSelectedItem(usuario.getRol());
            }
        }
    }

    private void guardarCambios() {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idUsuario = (String) modeloTablaUsuarios.getValueAt(filaSeleccionada, 0);
        Modelo.RolUsuario nuevoRol = (Modelo.RolUsuario) comboRolEdicion.getSelectedItem();

        try {
            controladorUsuario.editarUsuarioComoAdmin(idUsuario, null, nuevoRol);
            JOptionPane.showMessageDialog(this, "Usuario actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarUsuarios();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarUsuario() {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idUsuario = (String) modeloTablaUsuarios.getValueAt(filaSeleccionada, 0);
        String nombreUsuario = (String) modeloTablaUsuarios.getValueAt(filaSeleccionada, 2);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar al usuario \"" + nombreUsuario + "\"? Esta acción es irreversible.",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                controladorUsuario.eliminarUsuario(idUsuario);
                JOptionPane.showMessageDialog(this, "Usuario eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarUsuarios();
                campoMatriculaEdicion.setText("");
                campoNombreEdicion.setText("");
                comboRolEdicion.setSelectedIndex(0);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}