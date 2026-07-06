package Controlador;

import Modelo.Agrupacion;
import Modelo.Usuario;
import java.util.List;

/**
 * Controlador responsable de toda la lógica de negocio relacionada con las
 * agrupaciones del sistema.
 * <p>
 * Permite crear, editar y eliminar agrupaciones, así como gestionar los
 * miembros de cada una (trasladar usuarios entre agrupaciones y quitarlos).
 * Existe una agrupación por defecto llamada "Sin Agrupación" (ID: {@code agrup-001})
 * que no puede ser eliminada y sirve como destino para usuarios que no pertenecen
 * a ninguna agrupación específica.
 * </p>
 *
 * @author CLARA Team
 * @version 1.0
 */
public class ControladorAgrupacion {

    /**
     * Longitud máxima permitida para el nombre de una agrupación.
     */
    private static final int MAX_LARGO_NOMBRE_AGRUPACION = 150;

    /**
     * Identificador de la agrupación principal ("Sin Agrupación") que no puede
     * ser eliminada del sistema.
     */
    private static final String ID_AGRUPACION_PRINCIPAL = "agrup-001";

    /**
     * Lista de todas las agrupaciones registradas en el sistema.
     */
    private List<Agrupacion> agrupaciones;

    /**
     * Controlador de usuarios utilizado para actualizar la referencia de agrupación
     * de los usuarios cuando se mueven entre agrupaciones.
     */
    private ControladorUsuario controladorUsuario;

    /**
     * Constructor del controlador de agrupaciones.
     * <p>
     * Carga las agrupaciones desde el almacenamiento CSV y, si no existe ninguna,
     * crea automáticamente la agrupación por defecto "Sin Agrupación" con el ID
     * {@value #ID_AGRUPACION_PRINCIPAL}.
     * </p>
     *
     * @param controladorUsuario el controlador de usuarios necesario para mover
     *        usuarios entre agrupaciones
     */
    public ControladorAgrupacion(ControladorUsuario controladorUsuario) {
        this.agrupaciones = GestorArchivosCSV.cargarAgrupaciones();
        this.controladorUsuario = controladorUsuario;

        if (this.agrupaciones.isEmpty()) {
            Agrupacion principal = new Agrupacion("Sin Agrupación");
            principal.setIdAgrupacion(ID_AGRUPACION_PRINCIPAL);

            this.agrupaciones.add(principal);
            GestorArchivosCSV.guardarAgrupacion(principal);
        }
    }

    /**
     * Crea una nueva agrupación con el nombre proporcionado.
     * <p>
     * Valida que el nombre no esté vacío, no exceda los 150 caracteres y no
     * exista ya otra agrupación con el mismo nombre (insensible a mayúsculas/minúsculas).
     * </p>
     *
     * @param nombre el nombre de la nueva agrupación
     * @return la agrupación recién creada
     * @throws IllegalArgumentException si el nombre está vacío, supera los
     *         {@value #MAX_LARGO_NOMBRE_AGRUPACION} caracteres, o ya existe
     *         otra agrupación con ese nombre
     */
    public Agrupacion crearAgrupacion(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre de la agrupación no puede estar vacío.");
        }

        if (nombre.length() > MAX_LARGO_NOMBRE_AGRUPACION) {
            throw new IllegalArgumentException("El nombre de la agrupación no puede superar los " + MAX_LARGO_NOMBRE_AGRUPACION + " caracteres.");
        }

        for (Agrupacion a : agrupaciones) {
            if (a.getNombreAgrupacion().equalsIgnoreCase(nombre)) {
                throw new IllegalArgumentException("Ya existe una agrupación con ese nombre.");
            }
        }

        Agrupacion nueva = new Agrupacion(nombre);
        agrupaciones.add(nueva);
        GestorArchivosCSV.guardarAgrupacion(nueva);

        return nueva;
    }

    /**
     * Agrega un usuario a una agrupación destino.
     * <p>
     * Si el usuario ya pertenecía a otra agrupación distinta de la principal
     * ("Sin Agrupación"), primero se lo quita de su agrupación anterior para
     * evitar que quede asociado a dos agrupaciones simultáneamente.
     * </p>
     *
     * @param idAgrupacion el ID de la agrupación destino
     * @param usuario el usuario que se desea agregar a la agrupación
     * @throws IllegalArgumentException si no existe una agrupación con el ID destino
     */
    public void agregarMiembro(String idAgrupacion, Usuario usuario) {
        Agrupacion nuevaAgrupacion = buscarPorId(idAgrupacion);
        if (nuevaAgrupacion == null) {
            throw new IllegalArgumentException("No existe la agrupación destino.");
        }

        // Si el usuario ya pertenecía a otra agrupación, lo quitamos de ella primero
        String idAgrupacionAnterior = usuario.getIdAgrupacion();
        if (idAgrupacionAnterior != null && !idAgrupacionAnterior.equals(ID_AGRUPACION_PRINCIPAL)) {
            Agrupacion anterior = buscarPorId(idAgrupacionAnterior);
            if (anterior != null) {
                anterior.quitarMiembro(usuario.getIdUsuario());
            }
        }

        // Agregamos al usuario a la nueva agrupación
        nuevaAgrupacion.agregarMiembro(usuario.getIdUsuario());
        controladorUsuario.actualizarAgrupacionDeUsuario(usuario, idAgrupacion);

        GestorArchivosCSV.guardarTodasAgrupaciones(agrupaciones);
    }

    /**
     * Quita a un usuario de una agrupación, reasignándolo a la agrupación
     * por defecto "Sin Agrupación".
     * <p>
     * El usuario no queda sin agrupación: siempre se le asigna la agrupación
     * principal ({@value #ID_AGRUPACION_PRINCIPAL}) al ser removido.
     * </p>
     *
     * @param idAgrupacion el ID de la agrupación de la que se desea quitar el usuario
     * @param usuario el usuario que se desea remover de la agrupación
     * @throws IllegalArgumentException si no existe una agrupación con el ID proporcionado
     */
    public void quitarMiembro(String idAgrupacion, Usuario usuario) {
        Agrupacion agrupacion = buscarPorId(idAgrupacion);
        if (agrupacion == null) {
            throw new IllegalArgumentException("No existe una agrupación con ese id.");
        }

        agrupacion.quitarMiembro(usuario.getIdUsuario());
        controladorUsuario.actualizarAgrupacionDeUsuario(usuario, ID_AGRUPACION_PRINCIPAL);

        GestorArchivosCSV.guardarTodasAgrupaciones(agrupaciones);
    }

    /**
     * Busca una agrupación por su identificador único.
     *
     * @param idAgrupacion el ID de la agrupación a buscar
     * @return la agrupación encontrada, o {@code null} si no existe
     */
    public Agrupacion buscarPorId(String idAgrupacion) {
        for (Agrupacion a : agrupaciones) {
            if (a.getIdAgrupacion().equals(idAgrupacion)) {
                return a;
            }
        }
        return null;
    }

    /**
     * Edita el nombre de una agrupación existente.
     * <p>
     * Valida que la agrupación exista, que el nuevo nombre no esté vacío,
     * no exceda los 150 caracteres y no coincida con el nombre de otra
     * agrupación existente (insensible a mayúsculas/minúsculas).
     * </p>
     *
     * @param idAgrupacion el ID de la agrupación a editar
     * @param nuevoNombre el nuevo nombre para la agrupación
     * @throws IllegalArgumentException si la agrupación no existe, el nombre
     *         está vacío, supera los {@value #MAX_LARGO_NOMBRE_AGRUPACION}
     *         caracteres, o ya existe otra agrupación con ese nombre
     */
    public void editarAgrupacion(String idAgrupacion, String nuevoNombre) {
        Agrupacion agrupacion = buscarPorId(idAgrupacion);
        if (agrupacion == null) {
            throw new IllegalArgumentException("No existe una agrupación con ese id.");
        }
        if (nuevoNombre == null || nuevoNombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre de la agrupación no puede estar vacío.");
        }
        if (nuevoNombre.length() > MAX_LARGO_NOMBRE_AGRUPACION) {
            throw new IllegalArgumentException("El nombre de la agrupación no puede superar los " + MAX_LARGO_NOMBRE_AGRUPACION + " caracteres.");
        }
        for (Agrupacion a : agrupaciones) {
            if (!a.getIdAgrupacion().equals(idAgrupacion) && a.getNombreAgrupacion().equalsIgnoreCase(nuevoNombre)) {
                throw new IllegalArgumentException("Ya existe otra agrupación con ese nombre.");
            }
        }
        agrupacion.setNombreAgrupacion(nuevoNombre);
        GestorArchivosCSV.guardarTodasAgrupaciones(agrupaciones);
    }

    /**
     * Elimina una agrupación del sistema.
     * <p>
     * Antes de eliminarla, reasigna todos sus miembros a la agrupación principal
     * "Sin Agrupación" para garantizar que ningún usuario quede sin agrupación.
     * No permite eliminar la agrupación principal.
     * </p>
     *
     * @param idAgrupacion el ID de la agrupación a eliminar
     * @throws IllegalArgumentException si se intenta eliminar la agrupación
     *         principal o si la agrupación a eliminar no existe
     */
    public void eliminarAgrupacion(String idAgrupacion) {
        if (idAgrupacion.equals(ID_AGRUPACION_PRINCIPAL)) {
            throw new IllegalArgumentException("No se puede eliminar la agrupación principal 'Sin Agrupación'.");
        }

        Agrupacion agrupacionAEliminar = buscarPorId(idAgrupacion);
        if (agrupacionAEliminar == null) {
            throw new IllegalArgumentException("La agrupación a eliminar no existe.");
        }

        List<Usuario> miembros = controladorUsuario.listarUsuariosPorAgrupacion(idAgrupacion);
        for (Usuario miembro : miembros) {
            controladorUsuario.actualizarAgrupacionDeUsuario(miembro, ID_AGRUPACION_PRINCIPAL);
        }

        agrupaciones.remove(agrupacionAEliminar);
        GestorArchivosCSV.guardarTodasAgrupaciones(agrupaciones);
    }

    /**
     * Retorna la lista de todas las agrupaciones registradas en el sistema.
     *
     * @return lista de agrupaciones (puede incluir la agrupación principal)
     */
    public List<Agrupacion> listarAgrupaciones() {
        return agrupaciones;
    }
}
