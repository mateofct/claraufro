package Controlador;

import Modelo.Agrupacion;
import Modelo.Usuario;

import java.util.List;

public class ControladorAgrupacion {

    private static final int MAX_LARGO_NOMBRE_AGRUPACION = 150;
    private static final String ID_AGRUPACION_PRINCIPAL = "agrup-001";

    private List<Agrupacion> agrupaciones;
    private ControladorUsuario controladorUsuario;

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

    public void quitarMiembro(String idAgrupacion, Usuario usuario) {
        Agrupacion agrupacion = buscarPorId(idAgrupacion);
        if (agrupacion == null) {
            throw new IllegalArgumentException("No existe una agrupación con ese id.");
        }

        agrupacion.quitarMiembro(usuario.getIdUsuario());
        controladorUsuario.actualizarAgrupacionDeUsuario(usuario, ID_AGRUPACION_PRINCIPAL);

        GestorArchivosCSV.guardarTodasAgrupaciones(agrupaciones);
    }

    public Agrupacion buscarPorId(String idAgrupacion) {
        for (Agrupacion a : agrupaciones) {
            if (a.getIdAgrupacion().equals(idAgrupacion)) {
                return a;
            }
        }
        return null;
    }

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

    public void eliminarAgrupacion(String idAgrupacion) {
        if (idAgrupacion.equals(ID_AGRUPACION_PRINCIPAL)) {
            throw new IllegalStateException("No se puede eliminar la agrupación principal 'Sin Agrupación'.");
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