package modelo;
import java.util.UUID;
public class Usuario {
    private String idUsuario;
    private String idAgrupacion;
    private String nombre;
    private String contraseña;
    private RolUsuario rol;

    public Usuario(String idAgrupacion, String nombre, String contraseña, RolUsuario rol) {
        this.idUsuario = UUID.randomUUID().toString();
        this.idAgrupacion = idAgrupacion;
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.rol = rol;


    }

}
