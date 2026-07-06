package Controlador;

import Modelo.Usuario;
import Modelo.RolUsuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Controlador responsable de toda la lógica de negocio relacionada con la
 * gestión de usuarios del sistema.
 * <p>
 * Se encarga de registrar, listar, buscar, editar y eliminar usuarios,
 * así como de gestionar el inicio y cierre de sesión. Las matrículas se
 * tratan de forma insensible a mayúsculas/minúsculas mediante normalización.
 * Las contraseñas se almacenan cifradas y se descifran solo para comparación
 * durante la autenticación.
 * </p>
 *
 * @author CLARA Team
 * @version 1.0
 */
public class ControladorUsuario {

    /**
     * Lista de todos los usuarios registrados en el sistema.
     */
    private List<Usuario> usuarios;

    /**
     * Usuario que ha iniciado sesión actualmente, o {@code null} si no hay
     * ninguna sesión activa.
     */
    private Usuario usuarioActivo;

    /**
     * Fuente de datos externa para consultar los nombres reales de los
     * estudiantes a partir de sus matrículas.
     */
    private IFuenteMatriculas fuenteMatriculas;

    /**
     * Longitud máxima permitida para las contraseñas de los usuarios.
     */
    private static final int MAX_LARGO_CONTRASENA = 24;

    /**
     * Constructor que inicializa el controlador con la fuente de matrículas
     * y carga los usuarios existentes desde el almacenamiento CSV.
     * <p>
     * Si no existen usuarios registrados, crea automáticamente tres usuarios
     * de prueba con roles Admin, Tesorero y Socio, todos asignados a la
     * agrupación {@code "agrup-001"} con contraseña {@code "123"}.
     * </p>
     *
     * @param fuenteMatriculas la fuente de datos para consultar nombres por matrícula
     * @throws RuntimeException si ocurre un error al registrar los usuarios iniciales
     */
    public ControladorUsuario(IFuenteMatriculas fuenteMatriculas) {
        this.fuenteMatriculas = fuenteMatriculas;
        this.usuarios = GestorArchivosCSV.cargarUsuarios();
        this.usuarioActivo = null;

        if (this.usuarios.isEmpty()) {
            try {
                registrarUsuario("agrup-001", RolUsuario.ADMIN, "123", "1111111111k");
                registrarUsuario("agrup-001", RolUsuario.TESORERO, "123", "2222222222k");
                registrarUsuario("agrup-001", RolUsuario.SOCIO, "123", "3333333333k");
            } catch (Exception e) {
                throw new RuntimeException("Error al registrar usuario inicial: " + e.getMessage());
            }
        }
    }

    /**
     * Intenta iniciar sesión verificando la matrícula y contraseña proporcionadas.
     * <p>
     * La matrícula se normaliza antes de la comparación para que no importe
     * el uso de mayúsculas o minúsculas (por ejemplo, "K" o "k").
     * Si las credenciales son válidas, se establece el usuario como activo.
     * </p>
     *
     * @param matricula la matrícula del usuario que desea iniciar sesión
     * @param contrasena la contraseña en texto plano para la verificación
     * @return {@code true} si las credenciales son válidas y la sesión se inicia
     *         correctamente, {@code false} en caso contrario
     */
    public boolean iniciarSesion(String matricula, String contrasena) {
        // NORMALIZACIÓN: Usamos normalización para que no importe K o k
        String matriculaBuscada = GestorMatriculas.normalizarMatricula(matricula);

        for (Usuario usuario : usuarios) {
            // COMPARACIÓN INSENSIBLE: Usamos equalsIgnoreCase para mayor seguridad
            if (usuario.getMatricula().equalsIgnoreCase(matriculaBuscada)) {
                String contrasenaDescifrada = GestorSeguridad.descifrar(usuario.getContraseña());
                if (contrasenaDescifrada.equals(contrasena)) {
                    this.usuarioActivo = usuario;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * <p>
     * El proceso de registro valida que la matrícula no exista previamente,
     * que la contraseña no exceda los 24 caracteres, y consulta el nombre
     * real del estudiante desde la fuente de datos externa. La contraseña
     * se almacena cifrada en el objeto usuario y en el archivo CSV.
     * </p>
     *
     * @param idAgrupacion el ID de la agrupación a la que se asigna el usuario
     * @param rol el rol del usuario ({@link RolUsuario#TESORERO} o {@link RolUsuario#SOCIO})
     * @param contrasena la contraseña en texto plano para el nuevo usuario
     * @param matricula la matrícula del estudiante
     * @return el nuevo usuario creado
     * @throws IllegalArgumentException si la matrícula ya existe, si la contraseña
     *         supera los {@value #MAX_LARGO_CONTRASENA} caracteres, si la contraseña
     *         es {@code null} o si la matrícula tiene formato inválido
     */
    public Usuario registrarUsuario(String idAgrupacion, RolUsuario rol, String contrasena, String matricula){
        // NORMALIZACIÓN: La matrícula se guarda siempre normalizada
        String matriculaNormalizada = GestorMatriculas.normalizarMatricula(matricula);

        for (Usuario u : usuarios) {
            if (u.getMatricula().equalsIgnoreCase(matriculaNormalizada)) {
                throw new IllegalArgumentException("La matricula ya existe en el registro de CLARA.");
            }
        }
        if (contrasena == null || contrasena.length() > MAX_LARGO_CONTRASENA) {
            throw new IllegalArgumentException("La contraseña no puede superar los " + MAX_LARGO_CONTRASENA + " caracteres.");
        }

        String nombreReal = fuenteMatriculas.buscarNombrePorMatricula(matriculaNormalizada);

        String contrasenaCifrada = GestorSeguridad.cifrar(contrasena);
        Usuario nuevo = new Usuario(idAgrupacion, nombreReal, contrasenaCifrada, rol, matriculaNormalizada);
        usuarios.add(nuevo);

        GestorArchivosCSV.guardarUsuario(nuevo);
        return nuevo;
    }

    /**
     * Retorna una lista inmutable con todos los usuarios registrados en el sistema.
     *
     * @return lista de todos los usuarios del sistema
     */
    public List<Usuario> listarUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    /**
     * Retorna una lista con los usuarios que pertenecen a una agrupación específica.
     *
     * @param idAgrupacion el ID de la agrupación a filtrar
     * @return lista de usuarios pertenecientes a la agrupación indicada
     */
    public List<Usuario> listarUsuariosPorAgrupacion(String idAgrupacion) {
        List<Usuario> resultado = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario.getIdAgrupacion().equals(idAgrupacion)) {
                resultado.add(usuario);
            }
        }
        return resultado;
    }

    /**
     * Cambia la contraseña del usuario que tiene la sesión activa.
     * <p>
     * Valida que la contraseña actual sea correcta y que la nueva contraseña
     * no esté vacía ni exceda los 24 caracteres. La nueva contraseña se
     * almacena cifrada.
     * </p>
     *
     * @param contrasenaActual la contraseña actual en texto plano para verificación
     * @param contrasenaNueva la nueva contraseña en texto plano
     * @throws IllegalArgumentException si no hay sesión activa, si la contraseña
     *         actual es incorrecta, o si la nueva contraseña es inválida
     */
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

    /**
     * Edita los datos de un usuario como administrador. Solo puede ser
     * invocado por un usuario con rol {@link RolUsuario#ADMIN}.
     * <p>
     * Permite actualizar el rol del usuario y opcionalmente su contraseña.
     * Si la contraseña proporcionada es {@code null} o está vacía, no se modifica.
     * </p>
     *
     * @param idUsuarioObjetivo el ID del usuario a editar
     * @param nuevaContrasenaSinCifrar la nueva contraseña en texto plano,
     *        o {@code null}/vacía para no modificarla
     * @param nuevoRol el nuevo rol para el usuario, o {@code null} para no modificarlo
     * @throws IllegalArgumentException si no hay sesión activa, si el usuario
     *         actual no es administrador, si el usuario objetivo no existe,
     *         o si la contraseña supera los {@value #MAX_LARGO_CONTRASENA} caracteres
     */
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

    /**
     * Busca un usuario por su identificador único.
     *
     * @param idUsuario el ID del usuario a buscar
     * @return el usuario encontrado, o {@code null} si no existe
     */
    public Usuario buscarUsuarioPorId(String idUsuario) {
        for (Usuario u : usuarios) {
            if (u.getIdUsuario().equals(idUsuario)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Elimina un usuario del sistema por su identificador.
     * <p>
     * No permite eliminar usuarios con rol {@link RolUsuario#ADMIN} para
     * garantizar que siempre exista al menos un administrador en el sistema.
     * </p>
     *
     * @param idUsuario el ID del usuario a eliminar
     * @throws IllegalArgumentException si el usuario no existe o si intenta
     *         eliminar a un usuario con rol administrador
     */
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

    /**
     * Actualiza la agrupación a la que pertenece un usuario y persiste
     * el cambio en el archivo CSV.
     *
     * @param usuario el usuario cuya agrupación se desea actualizar
     * @param nuevaIdAgrupacion el ID de la nueva agrupación
     */
    public void actualizarAgrupacionDeUsuario(Usuario usuario, String nuevaIdAgrupacion) {
        usuario.setIdAgrupacion(nuevaIdAgrupacion);
        GestorArchivosCSV.guardarTodosUsuarios(usuarios);
    }

    /**
     * Obtiene el usuario que tiene la sesión activa actualmente.
     *
     * @return el usuario activo, o {@code null} si no hay sesión activa
     */
    public Usuario getUsuarioActivo() {
        return usuarioActivo;
    }

    /**
     * Cierra la sesión del usuario activo estableciendo la referencia a {@code null}.
     */
    public void cerrarSesion() {
        this.usuarioActivo = null;
    }

}
