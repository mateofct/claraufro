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
 * El Controlador Principal actúa como el "Orquestador" del sistema.
 * Implementa el patrón MVC puro al gestionar la visibilidad de las vistas y responder a sus eventos.
 */
public class ControladorPrincipal {
    private ControladorUsuario controladorUsuario;
    private ControladorFinanzas controladorFinanzas;
    private ControladorAgrupacion controladorAgrupacion;

    private VentanaIniciarSesion ventanaLogin;

    public ControladorPrincipal(ControladorUsuario cu, ControladorFinanzas cf, ControladorAgrupacion ca) {
        this.controladorUsuario = cu;
        this.controladorFinanzas = cf;
        this.controladorAgrupacion = ca;
    }

    public void iniciar() {
        mostrarLogin();
    }

    public void mostrarLogin() {
        if (ventanaLogin == null) {
            ventanaLogin = new VentanaIniciarSesion();
            ventanaLogin.getBotonIngresar().addActionListener(e -> procesarLogin());
        }
        ventanaLogin.setVisible(true);
    }

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

    private void abrirMenuSegunRol(Usuario usuario) {
        switch (usuario.getRol()) {
            case ADMIN: new VentanaMenuAdmin(this, controladorUsuario); break;
            case TESORERO: new VentanaMenuTesorero(this, controladorUsuario, controladorFinanzas); break;
            case SOCIO: new VentanaMenuSocio(this, controladorUsuario, controladorFinanzas); break;
        }
    }

    // --- USUARIOS ---
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
    public void mostrarVerSaldo() {
        int saldo = controladorFinanzas.calcularSaldo(controladorUsuario.getUsuarioActivo().getIdAgrupacion());
        new VentanaSaldo(saldo).setVisible(true);
    }

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

    public void cerrarSesion() {
        controladorUsuario.cerrarSesion();
        mostrarLogin();
    }
}