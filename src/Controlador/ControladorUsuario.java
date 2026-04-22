package Controlador;

import Modelo.Usuario;
import Modelo.RolUsuario;

import java.util.ArrayList;
import java.util.List;

public class ControladorUsuario {
    private List<Usuario> usuarios;

    private Usuario usuarioActivo;

    public ControladorUsuario() {
        this.usuarios = new ArrayList<>();
        this.usuarioActivo = null;

        Usuario admin = new Usuario("agrup-001", "chelo", "123", RolUsuario.ADMIN, "1");
        usuarios.add(admin);
    }

    public boolean iniciarSesion(String matricula, String contrasena) {
        for (Usuario usuario : usuarios) {
            if (usuario.getMatricula().equals(matricula)) {
                if (usuario.getContraseña().equals(contrasena)) {
                    this.usuarioActivo = usuario;
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
    public void cerrarSesion() {
        this.usuarioActivo = null;
    }
    public Usuario getUsuarioActivo() {
        return usuarioActivo;
    }
    public void registrarUsuario(String idAgrupacion, String nombre, String contrasena, RolUsuario rol, String matricula) {
        for (Usuario usuario : usuarios) {
            if (usuario.getMatricula().equals(matricula)) {
                System.out.println("Ya existe un usuario con esa matricula.");
            }
        }
        Usuario nuevo = new Usuario(idAgrupacion, nombre, contrasena, rol, matricula);
        usuarios.add(nuevo);
        System.out.println("Usuario registrado correctamente:" + nombre);
    }
}
