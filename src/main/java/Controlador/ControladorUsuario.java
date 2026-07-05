package Controlador;

import Modelo.Usuario;
import Modelo.RolUsuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import Vista.*;
import java.awt.event.ActionEvent;

public class ControladorUsuario {
    private List<Usuario> usuarios;
    private Usuario usuarioActivo;
    private IFuenteMatriculas fuenteMatriculas;
    private static final int MAX_LARGO_CONTRASENA = 24;

    public ControladorUsuario(IFuenteMatriculas fuenteMatriculas) {
        this.fuenteMatriculas = fuenteMatriculas;
        this.usuarios = GestorArchivosCSV.cargarUsuarios();
        this.usuarioActivo = null;

        if (this.usuarios.isEmpty()) {
            try {
                registrarUsuario("agrup-001", RolUsuario.ADMIN, "123", "1111111111k");
                // Los agregué para ver el POV sin tener que ingresar una matricula enorme siempre
                registrarUsuario("agrup-001", RolUsuario.TESORERO, "123", "2222222222k");
                registrarUsuario("agrup-001", RolUsuario.SOCIO, "123", "3333333333k");
            } catch (Exception e) {
                throw new RuntimeException("Error al registrar usuario inicial: " + e.getMessage());
            }
        }
    }

    public void mostrarLogin(ControladorFinanzas cf, ControladorAgrupacion ca) {
        VentanaIniciarSesion vista = new VentanaIniciarSesion();


        vista.setAccionLogin(e -> {
            String mat = vista.getMatricula();
            String pass = vista.getContrasena();

            if (mat.isEmpty() || pass.isEmpty()) {
                vista.mostrarError("Los campos no deben estar vacíos");
                return;
            }

            if (this.iniciarSesion(mat, pass)) {
                Usuario activo = this.getUsuarioActivo();
                vista.dispose(); // Cerramos login

                // El controlador decide qué ventana abrir
                switch (activo.getRol()) {
                    case ADMIN:
                        new VentanaMenuAdmin(this, cf, ca); // Deberías refactorizar estas también
                        break;
                    case TESORERO:
                        new VentanaMenuTesorero(this, cf, ca);
                        break;
                    case SOCIO:
                        new VentanaMenuSocio(this, cf, ca);
                        break;
                }
            } else {
                vista.mostrarError("Matrícula o contraseña incorrectos.");
                vista.limpiarContrasena();
            }
        });

        vista.setVisible(true);
    }

    public boolean iniciarSesion(String matricula, String contrasena) {
        String matriculaNormalizada = GestorMatriculas.normalizarMatricula(matricula);
        for (Usuario usuario : usuarios) {
            if (usuario.getMatricula().equalsIgnoreCase(matriculaNormalizada)) {
                String contrasenaDescifrada = GestorSeguridad.descifrar(usuario.getContraseña());
                if (contrasenaDescifrada.equals(contrasena)) {
                    this.usuarioActivo = usuario;
                    return true;
                }
            }
        }
        return false;
    }

    public Usuario registrarUsuario(String idAgrupacion, RolUsuario rol, String contrasena, String matricula){

        String matriculaNormalizada = GestorMatriculas.normalizarMatricula(matricula);

        for (Usuario u : usuarios) {
            if (u.getMatricula().equalsIgnoreCase(matriculaNormalizada)) {
                throw new IllegalArgumentException("La matricula ya existe en el registro de CLARA.");
            }
        }
        if (contrasena == null || contrasena.length() > MAX_LARGO_CONTRASENA) {
            throw new IllegalArgumentException("La contraseña no puede superar los " + MAX_LARGO_CONTRASENA + " caracteres.");
        }

        String nombreReal = fuenteMatriculas.buscarNombrePorMatricula(matricula);

        String contrasenaCifrada = GestorSeguridad.cifrar(contrasena);
        Usuario nuevo = new Usuario(idAgrupacion, nombreReal, contrasenaCifrada, rol, matricula);
        usuarios.add(nuevo);

        GestorArchivosCSV.guardarUsuario(nuevo);
        return nuevo;
    }

    public List<Usuario> listarUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    public List<Usuario> listarUsuariosPorAgrupacion(String idAgrupacion) {
        List<Usuario> resultado = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario.getIdAgrupacion().equals(idAgrupacion)) {
                resultado.add(usuario);
            }
        }
        return resultado;
    }

    public void cambiarPropiaContrasena(String contrasenaActual, String contrasenaNueva) {
        if (usuarioActivo == null) {
            throw new IllegalArgumentException("No hay una sesión activa.");
        }

        String contrasenaDescifrada = GestorSeguridad.descifrar(usuarioActivo.getContraseña());
        if (!contrasenaDescifrada.equals(contrasenaActual)) {
            throw new IllegalArgumentException("La contraseña actual no es correcta.");
        }

        if (contrasenaNueva == null || contrasenaNueva.isEmpty() || contrasenaNueva.length() > MAX_LARGO_CONTRASENA) {
            throw new IllegalArgumentException("La nueva contraseña no puede superar los " + MAX_LARGO_CONTRASENA + " caracteres.");
        }

        usuarioActivo.setContraseña(GestorSeguridad.cifrar(contrasenaNueva));
        GestorArchivosCSV.guardarTodosUsuarios(usuarios);
    }

    public void editarUsuarioComoAdmin(String idUsuarioObjetivo, String nuevaContrasenaSinCifrar, RolUsuario nuevoRol) {
        if (usuarioActivo == null || usuarioActivo.getRol() != RolUsuario.ADMIN) {
            throw new IllegalArgumentException("Solo el Administrador puede editar otros usuarios.");
        }

        Usuario objetivo = buscarUsuarioPorId(idUsuarioObjetivo);
        if (objetivo == null) {
            throw new IllegalArgumentException("No existe un usuario con ese id.");
        }

        if (nuevaContrasenaSinCifrar != null && !nuevaContrasenaSinCifrar.isEmpty()) {
            if (nuevaContrasenaSinCifrar.length() > MAX_LARGO_CONTRASENA) {
                throw new IllegalArgumentException("La contraseña no puede superar los " + MAX_LARGO_CONTRASENA + " caracteres.");
            }
            objetivo.setContraseña(GestorSeguridad.cifrar(nuevaContrasenaSinCifrar));
        }

        if (nuevoRol != null) {
            objetivo.setRol(nuevoRol);
        }

        GestorArchivosCSV.guardarTodosUsuarios(usuarios);
    }

    public Usuario buscarUsuarioPorId(String idUsuario) {
        for (Usuario u : usuarios) {
            if (u.getIdUsuario().equals(idUsuario)) {
                return u;
            }
        }
        return null;
    }

    public void eliminarUsuario(String idUsuario) {
        Usuario usuarioSeleccionado = buscarUsuarioPorId(idUsuario);
        if (usuarioSeleccionado == null) {
            throw new IllegalArgumentException("No existe un usuario con ese id.");
        }
        if (usuarioSeleccionado.getRol() == RolUsuario.ADMIN) {
            throw new IllegalArgumentException("No se puede eliminar al Administrador.");
        }
        usuarios.remove(usuarioSeleccionado);
        GestorArchivosCSV.guardarTodosUsuarios(usuarios);
    }

    public void actualizarAgrupacionDeUsuario(Usuario usuario, String nuevaIdAgrupacion) {
        usuario.setIdAgrupacion(nuevaIdAgrupacion);
        GestorArchivosCSV.guardarTodosUsuarios(usuarios);
    }

    public Usuario getUsuarioActivo() {
        return usuarioActivo;
    }

    public void cerrarSesion() {
        this.usuarioActivo = null;
    }

}