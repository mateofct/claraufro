package Controlador;

import Modelo.Usuario;
import Modelo.RolUsuario;
import java.util.List;

public class ControladorUsuario {
    private List<Usuario> usuarios;
    private Usuario usuarioActivo;

    public ControladorUsuario() {
        this.usuarios = GestorArchivosCSV.cargarUsuarios();
        this.usuarioActivo = null;

        if (this.usuarios.isEmpty()) {
            registrarUsuario("agrup-001", "Admin", "123", RolUsuario.ADMIN, "1");
        }
    }

    public boolean iniciarSesion(String matricula, String contrasena) {
        for (Usuario usuario : usuarios) {
            if (usuario.getMatricula().equals(matricula)) {

                String contrasenaDescifrada = GestorSeguridad.descifrar(usuario.getContraseña());
                if (contrasenaDescifrada.equals(contrasena)) {
                    this.usuarioActivo = usuario;
                    return true;
                }
            }
        }
        return false;
    }

    public void registrarUsuario(String idAgrupacion, String nombre, String contrasena, RolUsuario rol, String matricula) {
        for (Usuario u : usuarios) {
            if (u.getMatricula().equals(matricula)) {
                System.out.println("Error: Matricula ya existe.");
                return;
            }
        }
        String contrasenaCifrada = GestorSeguridad.cifrar(contrasena);
        Usuario nuevo = new Usuario(idAgrupacion, nombre, contrasenaCifrada, rol, matricula);
        usuarios.add(nuevo);

        GestorArchivosCSV.guardarUsuario(nuevo);

        System.out.println("Usuario registrado: " + nombre);
    }

    public void cerrarSesion() {
        this.usuarioActivo = null;
    }
    public Usuario getUsuarioActivo() {
        return usuarioActivo;
    }
}