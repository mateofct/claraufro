package Vista;

import Modelo.Usuario;
import Modelo.RolUsuario;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Vista para gestionar usuarios.
 * MVC Puro: No conoce al controlador. Expone métodos para manipular la tabla y los campos.
 */
public class VentanaGestionarUsuario extends JFrame {
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTablaUsuarios;
    private JTextField campoBusqueda;
    private JTextField campoMatriculaEdicion;
    private JTextField campoNombreEdicion;
    private JComboBox<RolUsuario> comboRolEdicion;
    private JButton botonGuardarCambios;
    private JButton botonEliminar;

    public VentanaGestionarUsuario() {
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
        campoMatriculaEdicion.setEditable(false);
        panelEdicion.add(campoMatriculaEdicion, gbc);

        panelEdicion.add(ComponentesUI.crearEtiqueta("Nombre:"), gbc);
        campoNombreEdicion = ComponentesUI.crearCampoTexto();
        campoNombreEdicion.setEditable(false);
        panelEdicion.add(campoNombreEdicion, gbc);

        panelEdicion.add(ComponentesUI.crearEtiqueta("Rol:"), gbc);
        comboRolEdicion = new JComboBox<>(RolUsuario.values());
        comboRolEdicion.setFont(new Font("Arial", Font.PLAIN, 14));
        panelEdicion.add(comboRolEdicion, gbc);

        JPanel panelBotonesEdicion = ComponentesUI.crearPanel();
        panelBotonesEdicion.setLayout(new GridLayout(1, 2, 10, 0));
        botonGuardarCambios = ComponentesUI.crearBoton("Guardar Cambios", null);
        botonEliminar = ComponentesUI.crearBotonPeligro("Eliminar Usuario", null);
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
    }

    // --- MÉTODOS PARA EL CONTROLADOR ---

    public void cargarUsuariosEnTabla(List<Usuario> usuarios, String filtro) {
        modeloTablaUsuarios.setRowCount(0);
        for (Usuario user : usuarios) {
            if (filtro.isEmpty() || user.getMatricula().toLowerCase().contains(filtro) || user.getNombre().toLowerCase().contains(filtro)) {
                modeloTablaUsuarios.addRow(new Object[]{user.getIdUsuario(), user.getMatricula(), user.getNombre(), user.getRol(), user.getIdAgrupacion()});
            }
        }
    }

    public String getIdUsuarioSeleccionado() {
        int fila = tablaUsuarios.getSelectedRow();
        return (fila != -1) ? (String) modeloTablaUsuarios.getValueAt(fila, 0) : null;
    }

    public String getNombreUsuarioSeleccionado() {
        int fila = tablaUsuarios.getSelectedRow();
        return (fila != -1) ? (String) modeloTablaUsuarios.getValueAt(fila, 2) : null;
    }

    public void mostrarDatosUsuario(Usuario u) {
        if (u != null) {
            campoMatriculaEdicion.setText(u.getMatricula());
            campoNombreEdicion.setText(u.getNombre());
            comboRolEdicion.setSelectedItem(u.getRol());
        }
    }

    public RolUsuario getRolSeleccionado() {
        return (RolUsuario) comboRolEdicion.getSelectedItem();
    }

    public String getTextoBusqueda() {
        return campoBusqueda.getText().toLowerCase();
    }

    public void setBusquedaListener(DocumentListener listener) {
        campoBusqueda.getDocument().addDocumentListener(listener);
    }

    public void setSeleccionTablaListener(javax.swing.event.ListSelectionListener listener) {
        tablaUsuarios.getSelectionModel().addListSelectionListener(listener);
    }

    public void setGuardarListener(ActionListener listener) {
        botonGuardarCambios.addActionListener(listener);
    }

    public void setEliminarListener(ActionListener listener) {
        botonEliminar.addActionListener(listener);
    }

    public void limpiarCampos() {
        campoMatriculaEdicion.setText("");
        campoNombreEdicion.setText("");
        comboRolEdicion.setSelectedIndex(0);
    }
}
