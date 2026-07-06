package Modelo;

import java.util.UUID;

/**
 * Modelo que representa un usuario del sistema.
 * <p>
 * Un usuario puede tener uno de tres roles: administrador, tesorero o socio.
 * Cada usuario está asociado a una agrupación y se identifica mediante su
 * matrícula universitaria. La contraseña se almacena cifrada para mayor seguridad.
 * </p>
 *
 * @author CLARA Team
 * @version 1.0
 * @see RolUsuario
 * @see Controlador.ControladorUsuario
 */
public class Usuario {

    /**
     * Identificador único del usuario, generado como UUID.
     */
    private String idUsuario;

    /**
     * Identificador de la agrupación a la que pertenece el usuario.
     */
    private String idAgrupacion;

    /**
     * Nombre real del usuario (obtenido de la base de datos de estudiantes).
     */
    private String nombre;

    /**
     * Contraseña del usuario almacenada en formato cifrado.
     */
    private String contraseña;

    /**
     * Rol del usuario dentro del sistema (ADMIN, TESORERO o SOCIO).
     */
    private RolUsuario rol;

    /**
     * Matrícula universitaria del usuario, normalizada en minúsculas.
     */
    private String matricula;

    /**
     * Constructor que crea un nuevo usuario.
     * <p>
     * Genera automáticamente un UUID como identificador. La contraseña
     * debe estar ya cifrada antes de pasarla a este constructor.
     * </p>
     *
     * @param idAgrupacion el ID de la agrupación a la que se asigna el usuario
     * @param nombre el nombre real del usuario
     * @param contraseña la contraseña ya cifrada
     * @param rol el rol del usuario en el sistema
     * @param matricula la matrícula universitaria normalizada
     */
    public Usuario(String idAgrupacion, String nombre, String contraseña, RolUsuario rol, String matricula) {
        this.idUsuario = UUID.randomUUID().toString();
        this.idAgrupacion = idAgrupacion;
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.rol = rol;
        this.matricula = matricula;
    }

    /**
     * Obtiene el identificador único del usuario.
     *
     * @return el ID del usuario
     */
    public String getIdUsuario() {
        return idUsuario;
    }

    /**
     * Obtiene el ID de la agrupación a la que pertenece el usuario.
     *
     * @return el ID de la agrupación
     */
    public String getIdAgrupacion() {
        return idAgrupacion;
    }

    /**
     * Obtiene el nombre real del usuario.
     *
     * @return el nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la contraseña del usuario (cifrada).
     *
     * @return la contraseña cifrada
     */
    public String getContraseña() {
        return contraseña;
    }

    /**
     * Obtiene el rol del usuario en el sistema.
     *
     * @return el rol ({@link RolUsuario#ADMIN}, {@link RolUsuario#TESORERO}
     *         o {@link RolUsuario#SOCIO})
     */
    public RolUsuario getRol() {
        return rol;
    }

    /**
     * Obtiene la matrícula universitaria del usuario.
     *
     * @return la matrícula normalizada
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * Establece la contraseña del usuario (debe estar cifrada).
     *
     * @param contraseña la contraseña cifrada
     */
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    /**
     * Establece el rol del usuario.
     *
     * @param rol el nuevo rol
     */
    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    /**
     * Establece el identificador del usuario.
     *
     * @param idUsuario el nuevo ID
     */
    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Establece el ID de la agrupación a la que pertenece el usuario.
     *
     * @param idAgrupacion el nuevo ID de agrupación
     */
    public void setIdAgrupacion(String idAgrupacion) {
        this.idAgrupacion = idAgrupacion;
    }

    /**
     * Retorna una representación en cadena del usuario.
     *
     * @return cadena en formato {@code "nombre (matricula)"}
     */
    @Override
    public String toString() {
        return this.nombre + " (" + this.matricula + ")";
    }
}
