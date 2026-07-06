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
 * <p>
 * MVC Puro: No conoce al controlador. Expone métodos para manipular la tabla
 * y los campos de edición. Proporciona una tabla con los usuarios registrados
 * (columnas: ID, Matrícula, Nombre, Rol, Agrupación), un campo de búsqueda
 * en tiempo real por matrícula o nombre, y paneles de edición con los datos
 * del usuario seleccionado.
 * </p>
 *
 * <h3>Elementos de la interfaz:</h3>
 * <table border="1" cellpadding="4" cellspacing="0">
 *   <caption>Componentes de la ventana de gestión de usuarios</caption>
 *   <tr><th>Elemento</th><th>Descripción</th></tr>
 *   <tr><td>Tabla</td><td>Columnas: ID, Matrícula, Nombre, Rol, Agrupación (no editable)</td></tr>
 *   <tr><td>Campo de búsqueda</td><td>Filtra usuarios por matrícula o nombre en tiempo real</td></tr>
 *   <tr><td>Campo "Matrícula" (edición)</td><td>Solo lectura, muestra la matrícula del seleccionado</td></tr>
 *   <tr><td>Campo "Nombre" (edición)</td><td>Solo lectura, muestra el nombre del seleccionado</td></tr>
 *   <tr><td>Combo "Rol"</td><td>Permite cambiar el rol del usuario seleccionado</td></tr>
 *   <tr><td>Botón "Guardar Cambios"</td><td>Guarda los cambios realizados al usuario</td></tr>
 *   <tr><td>Botón "Eliminar Usuario"</td><td>Elimina el usuario seleccionado (excepto administradores)</td></tr>
 * </table>
 *
 * @author CLARA Team
 * @version 1.0
 * @see Controlador.ControladorPrincipal#mostrarGestionarUsuarios()
 */
public class VentanaGestionarUsuario extends JFrame {

    /**
     * Tabla que muestra la lista de usuarios filtrados.
     */
    private JTable tablaUsuarios;

    /**
     * Modelo de datos de la tabla de usuarios.
     */
    private DefaultTableModel modeloTablaUsuarios;

    /**
     * Campo de texto para buscar usuarios por matrícula o nombre.
     */
    private JTextField campoBusqueda;

    /**
     * Campo de texto de solo lectura que muestra la matrícula del usuario
     * seleccionado en la tabla.
     */
    private JTextField campoMatriculaEdicion;

    /**
     * Campo de texto de solo lectura que muestra el nombre del usuario
     * seleccionado en la tabla.
     */
    private JTextField campoNombreEdicion;

    /**
     * Combo box para seleccionar/cambiar el rol del usuario.
     * Contiene todas las constantes del enum {@link RolUsuario}.
     */
    private JComboBox<RolUsuario> comboRolEdicion;

    /**
     * Botón para guardar los cambios del usuario seleccionado.
     */
    private JButton botonGuardarCambios;

    /**
     * Botón para eliminar el usuario seleccionado.
     */
    private JButton botonEliminar;

    /**
     * Constructor que construye y configura la ventana de gestión de usuarios.
     * <p>
     * Configura el título, tamaño, comportamiento de cierre y posición
     * centrada en pantalla. La tabla tiene las columnas ID, Matrícula,
     * Nombre, Rol y Agrupación, todas no editables. La selección es
     * individual (una fila a la vez). Utiliza {@link ComponentesUI}
     * para garantizar un diseño visual consistente.
     * </p>
     */
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

    /**
     * Carga los usuarios en la tabla, aplicando opcionalmente un filtro
     * por matrícula o nombre (insensible a mayúsculas/minúsculas).
     *
     * @param usuarios lista de todos los usuarios a mostrar
     * @param filtro texto de búsqueda para filtrar (se ignora si es vacío)
     */
    public void cargarUsuariosEnTabla(List<Usuario> usuarios, String filtro) {
        modeloTablaUsuarios.setRowCount(0);
        for (Usuario user : usuarios) {
            if (filtro.isEmpty() || user.getMatricula().toLowerCase().contains(filtro) || user.getNombre().toLowerCase().contains(filtro)) {
                modeloTablaUsuarios.addRow(new Object[]{user.getIdUsuario(), user.getMatricula(), user.getNombre(), user.getRol(), user.getIdAgrupacion()});
            }
        }
    }

    /**
     * Obtiene el ID del usuario seleccionado en la tabla.
     *
     * @return el ID del usuario seleccionado, o {@code null} si no hay
     *         selección
     */
    public String getIdUsuarioSeleccionado() {
        int fila = tablaUsuarios.getSelectedRow();
        return (fila != -1) ? (String) modeloTablaUsuarios.getValueAt(fila, 0) : null;
    }

    /**
     * Obtiene el nombre del usuario seleccionado en la tabla.
     *
     * @return el nombre del usuario seleccionado, o {@code null} si no hay
     *         selección
     */
    public String getNombreUsuarioSeleccionado() {
        int fila = tablaUsuarios.getSelectedRow();
        return (fila != -1) ? (String) modeloTablaUsuarios.getValueAt(fila, 2) : null;
    }

    /**
     * Muestra los datos del usuario proporcionado en los campos de edición
     * (matrícula, nombre y rol).
     *
     * @param u el usuario cuyos datos se desean mostrar
     */
    public void mostrarDatosUsuario(Usuario u) {
        if (u != null) {
            campoMatriculaEdicion.setText(u.getMatricula());
            campoNombreEdicion.setText(u.getNombre());
            comboRolEdicion.setSelectedItem(u.getRol());
        }
    }

    /**
     * Obtiene el rol seleccionado en el combo box.
     *
     * @return el rol como constante del enum {@link RolUsuario}
     */
    public RolUsuario getRolSeleccionado() {
        return (RolUsuario) comboRolEdicion.getSelectedItem();
    }

    /**
     * Obtiene el texto de búsqueda ingresado en el campo de filtro,
     * convertido a minúsculas.
     *
     * @return el texto de búsqueda en minúsculas
     */
    public String getTextoBusqueda() {
        return campoBusqueda.getText().toLowerCase();
    }

    /**
     * Registra un {@link DocumentListener} para el campo de búsqueda que
     * se ejecuta en tiempo real al escribir.
     *
     * @param listener el listener de documento
     */
    public void setBusquedaListener(DocumentListener listener) {
        campoBusqueda.getDocument().addDocumentListener(listener);
    }

    /**
     * Registra un listener de selección para la tabla de usuarios.
     *
     * @param listener el listener de selección de lista
     */
    public void setSeleccionTablaListener(javax.swing.event.ListSelectionListener listener) {
        tablaUsuarios.getSelectionModel().addListSelectionListener(listener);
    }

    /**
     * Registra un {@link ActionListener} para el botón "Guardar Cambios".
     *
     * @param listener el listener a ejecutar al presionar "Guardar Cambios"
     */
    public void setGuardarListener(ActionListener listener) {
        botonGuardarCambios.addActionListener(listener);
    }

    /**
     * Registra un {@link ActionListener} para el botón "Eliminar Usuario".
     *
     * @param listener el listener a ejecutar al presionar "Eliminar Usuario"
     */
    public void setEliminarListener(ActionListener listener) {
        botonEliminar.addActionListener(listener);
    }

    /**
     * Limpia los campos de edición de la ventana.
     */
    public void limpiarCampos() {
        campoMatriculaEdicion.setText("");
        campoNombreEdicion.setText("");
        comboRolEdicion.setSelectedIndex(0);
    }
}
