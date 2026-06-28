package Controlador;

import Modelo.Usuario;
import Modelo.RolUsuario;
import java.util.List;

public class ControladorUsuario {
    private List<Usuario> usuarios;
    private Usuario usuarioActivo;
    private IFuenteMatriculas fuenteMatriculas;

    private static final int MAX_LARGO_CONTRASEÑA = 24;

    public ControladorUsuario(IFuenteMatriculas fuenteMatriculas) {
        this.fuenteMatriculas = fuenteMatriculas;
        this.usuarios = GestorArchivosCSV.cargarUsuarios();
        this.usuarioActivo = null;

        if (this.usuarios.isEmpty()) {
            registrarUsuario("agrup-001", RolUsuario.ADMIN, "123", "1");
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

    public Usuario registrarUsuario(String idAgrupacion, RolUsuario rol, String contrasena, String matricula) {
        if (!GestorMatriculas.validarFormatoMatricula(matricula)) {
            throw new IllegalArgumentException("La matricula tiene un formato no valido");
        }
        for (Usuario u : usuarios) {
            if (u.getMatricula().equals(matricula)) {
                throw new IllegalArgumentException("Error: Matricula ya existe.");
            }
        }
        if (contrasena == null || contrasena.length() > MAX_LARGO_CONTRASEÑA) {
            throw new IllegalArgumentException("Error: La contraseña no puede superar los " + MAX_LARGO_CONTRASEÑA + " caracteres.");
        }

        String nombreReal = fuenteMatriculas.buscarNombrePorMatricula(matricula);
        String contrasenaCifrada = GestorSeguridad.cifrar(contrasena);
        Usuario nuevo = new Usuario(idAgrupacion, nombreReal, contrasenaCifrada, rol, matricula);
        usuarios.add(nuevo);

        GestorArchivosCSV.guardarUsuario(nuevo);
        return nuevo;
    }

    public void cerrarSesion() {
        this.usuarioActivo = null;
    }
    public Usuario getUsuarioActivo() {
        return usuarioActivo;
    }
}