package Controlador;

import Modelo.Agrupacion;
import Modelo.Usuario;

import java.util.List;

public class ControladorAgrupacion {

    private static final int MAX_LARGO_NOMBRE_AGRUPACION = 150;
    private static final String ID_AGRUPACION_PRINCIPAL = "agrup-001";

    private List<Agrupacion> agrupaciones;

    public ControladorAgrupacion() {
        this.agrupaciones = GestorArchivosCSV.cargarAgrupaciones();

        if (this.agrupaciones.isEmpty()) {
            Agrupacion principal = new Agrupacion("Agrupación Principal");
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
        Agrupacion agrupacion = buscarPorId(idAgrupacion);
        if (agrupacion == null) {
            throw new IllegalArgumentException("No existe una agrupación con ese id.");
        }

        agrupacion.agregarMiembro(usuario.getIdUsuario());
        usuario.setIdAgrupacion(idAgrupacion);

        GestorArchivosCSV.guardarTodasAgrupaciones(agrupaciones);
    }

    public void quitarMiembro(String idAgrupacion, Usuario usuario) {
        Agrupacion agrupacion = buscarPorId(idAgrupacion);
        if (agrupacion == null) {
            throw new IllegalArgumentException("No existe una agrupación con ese id.");
        }

        agrupacion.quitarMiembro(usuario.getIdUsuario());
        usuario.setIdAgrupacion(ID_AGRUPACION_PRINCIPAL);

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

    public List<Agrupacion> listarAgrupaciones() {
        return agrupaciones;
    }
}