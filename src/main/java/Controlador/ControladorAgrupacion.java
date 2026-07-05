package Controlador;

import Modelo.Agrupacion;
import Modelo.Usuario;
import java.util.List;


/**
 * Controla todo lo relacionado a agrupaciones, como crear una,
 * agergar o quitar miembros y cambiarlos de agrupación
 *
 * Hay una agrupación por defecto que se llama "Sin Agrupación"
 * esta agrupación no se puede eliminar.
 */

public class ControladorAgrupacion {

    private static final int MAX_LARGO_NOMBRE_AGRUPACION = 150;
    private static final String ID_AGRUPACION_PRINCIPAL = "agrup-001";

    private List<Agrupacion> agrupaciones;
    private ControladorUsuario controladorUsuario;

    /**
     * constructor del controlador, que recibe como
     * @param controladorUsuario, se usa para mover usuarios entre agrupaciones
     * si no hay una agrupación existente, crea la agrupación por defecto
     *
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
     * Crea una nueva agrupación y recibe como
     *@param nombre, que es el nombre de la nueva agrupación
     * @return la agrupación recién creada
     * @throws IllegalArgumentException, se lanza cuando el nombre está vacío, si supera
     * el largo máximo permitido o si ya existe una agrupación con ese nombre
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
     * Agrega un usuario a una agrupación
     * Si el usuario ya pertenecía a
     * otra agrupación que no sea la de por defecto,
     * primero se lo quita de ahi para que no quede en dos agrupaciones a la vez
     *
     * Tiene como parámetros:
     * @param idAgrupacion es el id de la agrupación destino
     * @param usuario es el usuario que se va a agregar
     * @throws IllegalArgumentException, se lanza si no existe la agrupación destino
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
     * Quita a un usuario de una agrupación, dejándolo en la agrupación
     * por defecto
     * Tiene los siguientes parámetros:
     * @param idAgrupacion es el id de la agrupación de la que se va a quitar el usuario
     * @param usuario es el usuario que se va a quitar
     * @throws IllegalArgumentException, se lanza si no existe una agrupación con ese id
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
     * Busca una agrupación por su id.
     * Tiene los siguientes parametro:
     * @param idAgrupacion es el id de la agrupación a buscar
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
     * Cambia el nombre de una agrupación existente.
     * Tiene los siguientes parametros:
     * @param idAgrupacion es el id de la agrupación a editar
     * @param nuevoNombre es el nuevo nombre para la agrupación
     * @throws IllegalArgumentException, se lanza si no existe una agrupación con
     * ese id, si el nuevo nombre está vacío, si supera el largo
     * máximo permitido, o si ya existe otra agrupación con ese nombre
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
     * Elimina una agrupación
     * Antes de eliminarla, mueve a todos sus miembros a la agrupación principal,
     * para que ningún usuario quede sin agrupación.
     * Tiene los siguientes parametros:
     * @param idAgrupacion es el id de la agrupación a eliminar
     * @throws IllegalArgumentException, se lanza si se intenta eliminar la
     * agrupación principal o si la agrupación a eliminar no existe
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

    public List<Agrupacion> listarAgrupaciones() {
        return agrupaciones;
    }
}