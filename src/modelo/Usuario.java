package modelo;
import java.util.UUID;
public class Usuario {
    private String idUsuario;
    private String idAgrupacion;
    private String nombre;
    private String contraseña;
    private RolUsuario rol;
    private String matricula;

    public Usuario(String idAgrupacion, String nombre, String contraseña, RolUsuario rol, String matricula) {
        this.idUsuario = UUID.randomUUID().toString();
        this.idAgrupacion = idAgrupacion;
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.rol = rol;
        this.matricula = matricula;
    }


    public String getIdUsuario() {
        return idUsuario;
    }
    public String getIdAgrupacion() {
        return idAgrupacion;
    }
    public String getNombre() {
        return nombre;
    }
    public String getContraseña() {
        return contraseña;
    }
    public RolUsuario getRol() {
        return rol;
    }
    public String getMatricula() {
        return matricula;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }


}
