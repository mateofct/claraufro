package Controlador;

import Modelo.RolUsuario;
import Modelo.Usuario;
import Modelo.Agrupacion;
import Modelo.TipoMovimiento;
import Vista.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.List;
import java.util.ArrayList;

/**
 * Controlador principal que actúa como el orquestador central del sistema CLARA.
 * <p>
 * Implementa el patrón MVC puro al gestionar la visibilidad de las vistas
 * y responder a sus eventos de usuario. No contiene lógica de negocio propia;
 * en su lugar, delega todas las operaciones a los controladores especializados
 * ({@link ControladorUsuario}, {@link ControladorFinanzas} y
 * {@link ControladorAgrupacion}) y coordina la navegación entre ventanas
 * según el rol del usuario autenticado.
 * </p>
 *
 * @author CLARA Team
 * @version 1.0
 */
public class ControladorPrincipal {

    /**
     * Controlador encargado de la gestión de usuarios.
     */
    private ControladorUsuario controladorUsuario;

    /**
     * Controlador encargado de la gestión financiera.
     */
    private ControladorFinanzas controladorFinanzas;

    /**
     * Controlador encargado de la gestión de agrupaciones.
     */
    private ControladorAgrupacion controladorAgrupacion;

    /**
     * Referencia a la ventana de inicio de sesión para permitir su reutilización
     * y re-apertura tras cerrar sesión.
     */
    private VentanaIniciarSesion ventanaLogin;

    /**
     * Constructor que recibe las instancias de los controladores especializados
     * y los almacena para su uso en la orquestación del sistema.
     *
     * @param cu el controlador de usuarios
     * @param cf el controlador de finanzas
     * @param ca el controlador de agrupaciones
     */
    public ControladorPrincipal(ControladorUsuario cu, ControladorFinanzas cf, ControladorAgrupacion ca) {
        this.controladorUsuario = cu;
        this.controladorFinanzas = cf;
        this.controladorAgrupacion = ca;
    }

    /**
     * Inicia la aplicación mostrando la pantalla de inicio de sesión.
     */
    public void iniciar() {
        mostrarLogin();
    }

    /**
     * Muestra la ventana de inicio de sesión.
     * <p>
     * Si la ventana no existe, la crea y configura el listener del botón
     * "Ingresar" para procesar las credenciales proporcionadas.
     * </p>
     */
    public void mostrarLogin() {
        if (ventanaLogin == null) {
            ventanaLogin = new VentanaIniciarSesion();
            ventanaLogin.getBotonIngresar().addActionListener(e -> procesarLogin());
        }
        ventanaLogin.setVisible(true);
    }

    /**
     * Procesa el intento de inicio de sesión obteniendo las credenciales
     * de la vista de login y validándolas a través del controlador de usuarios.
     * <p>
     * Si el login es exitoso, cierra la ventana de login y abre el menú
     * correspondiente al rol del usuario. Si falla, muestra un mensaje de
     * error y limpia el campo de contraseña.
     * </p>
     */
    private void procesarLogin() {
        String matricula = ventanaLogin.getMatricula();
        String contrasena = ventanaLogin.getContrasena();
        if (controladorUsuario.iniciarSesion(matricula, contrasena)) {
            Usuario activo = controladorUsuario.getUsuarioActivo();
            ventanaLogin.dispose();
            ventanaLogin = null;
            abrirMenuSegunRol(activo);
        } else {
            JOptionPane.showMessageDialog(ventanaLogin, "Matrícula o contraseña incorrectos.", "Error de acceso", JOptionPane.ERROR_MESSAGE);
            ventanaLogin.limpiarContrasena();
        }
    }

    /**
     * Abre el menú principal correspondiente al rol del usuario autenticado.
     * <p>
     * Los roles admitidos son:
     * <ul>
     *   <li>{@link RolUsuario#ADMIN} - Abre la ventana del administrador.</li>
     *   <li>{@link RolUsuario#TESORERO} - Abre la ventana del tesorero.</li>
     *   <li>{@link RolUsuario#SOCIO} - Abre la ventana del socio.</li>
     * </ul>
     * </p>
     *
     * @param usuario el usuario autenticado cuyo rol determina el menú a mostrar
     */
    private void abrirMenuSegunRol(Usuario usuario) {
        switch (usuario.getRol()) {
            case ADMIN: new VentanaMenuAdmin(this, controladorUsuario); break;
            case TESORERO: new VentanaMenuTesorero(this, controladorUsuario, controladorFinanzas); break;
            case SOCIO: new VentanaMenuSocio(this, controladorUsuario, controladorFinanzas); break;
        }
    }

    // --- USUARIOS ---

    /**
     * Muestra la ventana de registro de un nuevo usuario.
     * <p>
     * Configura el listener del botón "Registrar" para delegar la creación
     * del usuario al {@link ControladorUsuario}, mostrando mensajes de
     * éxito o error según el resultado.
     * </p>
     */
    public void mostrarRegistrarUsuario() {
        VentanaCrearUsuario v = new VentanaCrearUsuario();
        v.setRegistrarListener(e -> {
            try {
                controladorUsuario.registrarUsuario("agrup-001", v.getRolSeleccionado(), v.getContrasena(), v.getMatricula());
                JOptionPane.showMessageDialog(v, "Usuario registrado correctamente");
                v.dispose();
            } catch (Exception ex) { JOptionPane.showMessageDialog(v, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });
        v.setVisible(true);
    }

    /**
     * Muestra la ventana de gestión de usuarios (CRUD).
     * <p>
     * Configura listeners para:
     * <ul>
     *   <li>Búsqueda en tiempo real por matrícula o nombre.</li>
     *   <li>Selección de un usuario en la tabla para ver sus datos.</li>
     *   <li>Guardar cambios en el rol del usuario seleccionado.</li>
     *   <li>Eliminar el usuario seleccionado (excepto administradores).</li>
     * </ul>
     * </p>
     */
    public void mostrarGestionarUsuarios() {
        VentanaGestionarUsuario v = new VentanaGestionarUsuario();
        v.cargarUsuariosEnTabla(controladorUsuario.listarUsuarios(), "");
        v.setBusquedaListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { v.cargarUsuariosEnTabla(controladorUsuario.listarUsuarios(), v.getTextoBusqueda()); }
            public void removeUpdate(DocumentEvent e) { v.cargarUsuariosEnTabla(controladorUsuario.listarUsuarios(), v.getTextoBusqueda()); }
            public void changedUpdate(DocumentEvent e) { v.cargarUsuariosEnTabla(controladorUsuario.listarUsuarios(), v.getTextoBusqueda()); }
        });
        v.setSeleccionTablaListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String id = v.getIdUsuarioSeleccionado();
                if (id != null) v.mostrarDatosUsuario(controladorUsuario.buscarUsuarioPorId(id));
            }
        });
        v.setGuardarListener(e -> {
            String id = v.getIdUsuarioSeleccionado();
            if (id == null) return;
            try {
                controladorUsuario.editarUsuarioComoAdmin(id, null, v.getRolSeleccionado());
                JOptionPane.showMessageDialog(v, "Usuario actualizado");
                v.cargarUsuariosEnTabla(controladorUsuario.listarUsuarios(), v.getTextoBusqueda());
            } catch (Exception ex) { JOptionPane.showMessageDialog(v, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });
        v.setEliminarListener(e -> {
            String id = v.getIdUsuarioSeleccionado();
            if (id == null) return;
            int conf = JOptionPane.showConfirmDialog(v, "¿Eliminar a " + v.getNombreUsuarioSeleccionado() + "?");
            if (conf == JOptionPane.YES_OPTION) {
                try {
                    controladorUsuario.eliminarUsuario(id);
                    v.cargarUsuariosEnTabla(controladorUsuario.listarUsuarios(), v.getTextoBusqueda());
                    v.limpiarCampos();
                } catch (Exception ex) { JOptionPane.showMessageDialog(v, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
            }
        });
        v.setVisible(true);
    }

    // --- AGRUPACIONES ---

    /**
     * Muestra la ventana de creación de una nueva agrupación.
     * <p>
     * Configura el listener del botón "Guardar" para delegar la creación
     * de la agrupación al {@link ControladorAgrupacion}.
     * </p>
     */
    public void mostrarRegistrarAgrupacion() {
        VentanaCrearAgrupacion v = new VentanaCrearAgrupacion();
        v.setGuardarListener(e -> {
            try {
                controladorAgrupacion.crearAgrupacion(v.getNombreAgrupacion());
                JOptionPane.showMessageDialog(v, "Agrupación creada");
                v.dispose();
            } catch (Exception ex) { JOptionPane.showMessageDialog(v, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });
        v.setVisible(true);
    }

    /**
     * Muestra la ventana de gestión de agrupaciones (CRUD).
     * <p>
     * Configura listeners para:
     * <ul>
     *   <li>Selección de una agrupación en la tabla para ver su nombre.</li>
     *   <li>Guardar cambios en el nombre de la agrupación seleccionada.</li>
     *   <li>Eliminar la agrupación seleccionada (tras reasignar sus miembros).</li>
     * </ul>
     * </p>
     */
    public void mostrarGestionarAgrupaciones() {
        VentanaGestionarAgrupacion v = new VentanaGestionarAgrupacion();
        v.cargarAgrupaciones(controladorAgrupacion.listarAgrupaciones());
        v.setSeleccionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String id = v.getIdSeleccionado();
                if (id != null) {
                    Agrupacion a = controladorAgrupacion.buscarPorId(id);
                    if (a != null) v.setNombreAgrupacion(a.getNombreAgrupacion());
                }
            }
        });
        v.setGuardarListener(e -> {
            String id = v.getIdSeleccionado();
            if (id == null) return;
            try {
                controladorAgrupacion.editarAgrupacion(id, v.getNombreAgrupacion());
                JOptionPane.showMessageDialog(v, "Agrupación actualizada");
                v.cargarAgrupaciones(controladorAgrupacion.listarAgrupaciones());
            } catch (Exception ex) { JOptionPane.showMessageDialog(v, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });
        v.setEliminarListener(e -> {
            String id = v.getIdSeleccionado();
            if (id == null) return;
            if (JOptionPane.showConfirmDialog(v, "¿Eliminar agrupación?") == JOptionPane.YES_OPTION) {
                try {
                    controladorAgrupacion.eliminarAgrupacion(id);
                    v.cargarAgrupaciones(controladorAgrupacion.listarAgrupaciones());
                } catch (Exception ex) { JOptionPane.showMessageDialog(v, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
            }
        });
        v.setVisible(true);
    }

    /**
     * Muestra la ventana de gestión de miembros de una agrupación.
     * <p>
     * Permite al administrador:
     * <ul>
     *   <li>Seleccionar la agrupación a gestionar desde un combo box.</li>
     *   <li>Filtrar usuarios por matrícula o nombre.</li>
     *   <li>Trasladar candidatos a la agrupación seleccionada.</li>
     *   <li>Quitar miembros de la agrupación (los envía a "Sin Agrupación").</li>
     * </ul>
     * </p>
     */
    public void mostrarGestionarMiembros() {
        VentanaGestionarMiembros v = new VentanaGestionarMiembros();
        v.setAgrupaciones(controladorAgrupacion.listarAgrupaciones());

        Runnable actualizarTablas = () -> {
            Agrupacion sel = v.getAgrupacionSeleccionada();
            if (sel == null) return;
            List<Object[]> candidatos = new ArrayList<>();
            List<Object[]> miembros = new ArrayList<>();
            for (Usuario u : controladorUsuario.listarUsuarios()) {
                if (u.getRol() == RolUsuario.ADMIN) continue;
                if (!u.getMatricula().toLowerCase().contains(v.getFiltro()) && !u.getNombre().toLowerCase().contains(v.getFiltro())) continue;
                if (u.getIdAgrupacion().equals(sel.getIdAgrupacion())) {
                    miembros.add(new Object[]{u.getIdUsuario(), u.getMatricula(), u.getNombre()});
                } else {
                    Agrupacion actual = controladorAgrupacion.buscarPorId(u.getIdAgrupacion());
                    candidatos.add(new Object[]{u.getIdUsuario(), u.getMatricula(), u.getNombre(), (actual != null ? actual.getNombreAgrupacion() : "Ninguna")});
                }
            }
            v.cargarTablas(candidatos, miembros);
        };

        v.setAgrupacionListener(e -> actualizarTablas.run());
        v.setBusquedaListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarTablas.run(); }
            public void removeUpdate(DocumentEvent e) { actualizarTablas.run(); }
            public void changedUpdate(DocumentEvent e) { actualizarTablas.run(); }
        });
        v.setMoverListener(e -> {
            String id = v.getIdCandidatoSeleccionado();
            if (id != null) {
                controladorAgrupacion.agregarMiembro(v.getAgrupacionSeleccionada().getIdAgrupacion(), controladorUsuario.buscarUsuarioPorId(id));
                actualizarTablas.run();
            }
        });
        v.setQuitarListener(e -> {
            String id = v.getIdMiembroSeleccionado();
            if (id != null) {
                controladorAgrupacion.quitarMiembro(v.getAgrupacionSeleccionada().getIdAgrupacion(), controladorUsuario.buscarUsuarioPorId(id));
                actualizarTablas.run();
            }
        });
        actualizarTablas.run();
        v.setVisible(true);
    }

    // --- FINANZAS ---

    /**
     * Muestra la ventana con el saldo actual de la agrupación del usuario activo.
     * <p>
     * Calcula el saldo a través del {@link ControladorFinanzas} y lo presenta
     * en la {@link VentanaSaldo}.
     * </p>
     */
    public void mostrarVerSaldo() {
        int saldo = controladorFinanzas.calcularSaldo(controladorUsuario.getUsuarioActivo().getIdAgrupacion());
        new VentanaSaldo(saldo).setVisible(true);
    }

    /**
     * Muestra la ventana del historial de movimientos financieros.
     * <p>
     * Configura listeners para filtrar movimientos por fecha y tipo, así como
     * para abrir los comprobantes asociados a los movimientos seleccionados.
     * </p>
     */
    public void mostrarHistorial() {
        VentanaHistorial v = new VentanaHistorial();
        Runnable filtrar = () -> {
            List<String[]> res = controladorFinanzas.filtrarMovimientos(controladorUsuario.getUsuarioActivo().getIdAgrupacion(), v.getFechaFiltro(), v.getTipoFiltro());
            v.cargarMovimientos(res);
        };
        v.setFiltrarListener(e -> filtrar.run());
        v.setVerComprobanteListener(e -> {
            String nom = v.getNombreComprobanteSeleccionado();
            if (nom != null) controladorFinanzas.pedirAbrirComprobante(nom);
        });
        filtrar.run();
        v.setVisible(true);
    }

    /**
     * Muestra la ventana para registrar un nuevo movimiento financiero.
     *
     * @param esIngreso {@code true} para registrar un ingreso,
     *        {@code false} para registrar un egreso
     */
    public void mostrarRegistrarMovimiento(boolean esIngreso) {
        int saldo = controladorFinanzas.calcularSaldo(controladorUsuario.getUsuarioActivo().getIdAgrupacion());
        VentanaRegistrarMovimiento v = new VentanaRegistrarMovimiento(esIngreso, saldo);
        v.setGuardarListener(e -> {
            try {
                int monto = Integer.parseInt(v.getMonto());
                TipoMovimiento tipo = esIngreso ? TipoMovimiento.INGRESO : TipoMovimiento.EGRESO;
                controladorFinanzas.registrarMovimiento(controladorUsuario.getUsuarioActivo().getIdAgrupacion(), tipo, monto, v.getDescripcion(), v.getRutaComprobante(), controladorUsuario.getUsuarioActivo().getIdUsuario());
                JOptionPane.showMessageDialog(v, "Movimiento registrado");
                v.dispose();
            } catch (Exception ex) { JOptionPane.showMessageDialog(v, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });
        v.setVisible(true);
    }

    /**
     * Muestra la ventana para que el usuario cambie su propia contraseña.
     */
    public void mostrarCambiarContrasena() {
        VentanaCambiarContrasena v = new VentanaCambiarContrasena();
        v.setGuardarListener(e -> {
            if (!v.getNuevaContrasena().equals(v.getConfirmacionContrasena())) {
                JOptionPane.showMessageDialog(v, "Las contraseñas no coinciden");
                return;
            }
            try {
                controladorUsuario.cambiarPropiaContrasena(v.getContrasenaActual(), v.getNuevaContrasena());
                JOptionPane.showMessageDialog(v, "Contraseña cambiada");
                v.dispose();
            } catch (Exception ex) { JOptionPane.showMessageDialog(v, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });
        v.setVisible(true);
    }

    /**
     * Cierra la sesión del usuario activo y vuelve a mostrar la pantalla
     * de inicio de sesión.
     */
    public void cerrarSesion() {
        controladorUsuario.cerrarSesion();
        mostrarLogin();
    }
}
