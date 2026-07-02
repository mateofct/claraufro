package Vista;

import Controlador.ControladorUsuario;
import Modelo.RolUsuario;
import Modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaGestionarUsuario extends JFrame {
    private ControladorUsuario controladorUsuario;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private JTextField campoBusqueda;
    private JButton botonEditar;
    private JButton botonEliminar;

    public VentanaGestionarUsuario(ControladorUsuario controladorUsuario) {
        this.controladorUsuario = controladorUsuario;

        setTitle("CLARA - Gestionar Usuarios");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(102, 133, 183));

        // Panel Superior (Búsqueda)
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelSuperior.setBackground(new Color(102, 133, 183));
        panelSuperior.add(ComponentesUI.crearEtiqueta("Buscar por Matrícula:"));
        campoBusqueda = new JTextField(15);
        panelSuperior.add(campoBusqueda);
        JButton botonBuscar = ComponentesUI.crearBoton("Buscar",
                e -> cargarUsuarios(campoBusqueda.getText()));
        panelSuperior.add(botonBuscar);
        add(panelSuperior, BorderLayout.NORTH);

        // Tabla de Usuarios
        String[] columnas = {"ID Usuario", "Matrícula", "Nombre", "Rol", "ID Agrupación"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Las celdas no son editables directamente en la tabla
            }
        };
        tablaUsuarios = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        add(scrollPane, BorderLayout.CENTER);

        // Panel Inferior (Botones de Acción)
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelInferior.setBackground(new Color(102, 133, 183));

        botonEditar = ComponentesUI.crearBoton("Editar Usuario",
                e -> editarUsuario());
        panelInferior.add(botonEditar);

        botonEliminar = ComponentesUI.crearBoton("Eliminar Usuario",
                e -> eliminarUsuario());
        panelInferior.add(botonEliminar);

        add(panelInferior, BorderLayout.SOUTH);

        cargarUsuarios(null);
        setVisible(true);
    }

    private void cargarUsuarios(String filtroMatricula) {
        modeloTabla.setRowCount(0); // Limpiar tabla
        List<Usuario> usuarios = controladorUsuario.listarUsuarios();

        for (Usuario usuario : usuarios) {
            if (filtroMatricula == null || filtroMatricula.isEmpty() || usuario.getMatricula().contains(filtroMatricula)) {
                modeloTabla.addRow(new Object[]{
                        usuario.getIdUsuario(),
                        usuario.getMatricula(),
                        usuario.getNombre(),
                        usuario.getRol(),
                        usuario.getIdAgrupacion()
                });
            }
        }
    }

    private void editarUsuario() {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idUsuario = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        Usuario usuarioAEditar = controladorUsuario.buscarUsuarioPorId(idUsuario);

        if (usuarioAEditar == null) {
            JOptionPane.showMessageDialog(this, "Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Diálogo para editar usuario
        JTextField campoNuevaContrasena = new JPasswordField();
        JComboBox<RolUsuario> comboRol = new JComboBox<>(RolUsuario.values());
        comboRol.setSelectedItem(usuarioAEditar.getRol());

        JPanel panelEdicion = new JPanel(new GridLayout(0, 2, 5, 5));
        panelEdicion.add(new JLabel("Nueva Contraseña (dejar vacío para no cambiar):"));
        panelEdicion.add(campoNuevaContrasena);
        panelEdicion.add(new JLabel("Nuevo Rol:"));
        panelEdicion.add(comboRol);

        int result = JOptionPane.showConfirmDialog(this, panelEdicion, "Editar Usuario: " + usuarioAEditar.getNombre(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String nuevaContrasena = new String(((JPasswordField) campoNuevaContrasena).getPassword());
            RolUsuario nuevoRol = (RolUsuario) comboRol.getSelectedItem();

            try {
                controladorUsuario.editarUsuarioComoAdmin(usuarioAEditar.getIdUsuario(), nuevaContrasena, nuevoRol);
                JOptionPane.showMessageDialog(this, "Usuario editado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarUsuarios(campoBusqueda.getText()); // Recargar la tabla
            } catch (IllegalArgumentException | IllegalStateException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarUsuario() {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idUsuario = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        Usuario usuarioAEliminar = controladorUsuario.buscarUsuarioPorId(idUsuario);

        if (usuarioAEliminar == null) {
            JOptionPane.showMessageDialog(this, "Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar al usuario " + usuarioAEliminar.getNombre() + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                controladorUsuario.eliminarUsuario(idUsuario);
                JOptionPane.showMessageDialog(this, "Usuario eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarUsuarios(campoBusqueda.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}