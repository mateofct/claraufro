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

        Usuario admin = new Usuario("chelo", "Admin", "123", RolUsuario.ADMIN, "1");
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
}
