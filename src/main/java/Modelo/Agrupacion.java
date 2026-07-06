package Modelo;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

/**
 * Modelo que representa una agrupación dentro del sistema.
 * <p>
 * Una agrupación es una entidad organizativa que agrupa a usuarios con roles
 * de tesorería o socio. Cada agrupación tiene un identificador único, un nombre,
 * un saldo total (inicialmente cero) y una lista de IDs de sus miembros.
 * </p>
 *
 * @author CLARA Team
 * @version 1.0
 */
public class Agrupacion {

    /**
     * Identificador único de la agrupación, generado como UUID.
     */
    private String idAgrupacion;

    /**
     * Nombre descriptivo de la agrupación.
     */
    private String nombreAgrupacion;

    /**
     * Saldo financiero total de la agrupación. Inicialmente se establece en cero.
     */
    private int saldoTotal;

    /**
     * Lista de identificadores de los usuarios que pertenecen a esta agrupación.
     */
    private List<String> idMiembros;

    /**
     * Constructor que crea una nueva agrupación con el nombre proporcionado.
     * <p>
     * Inicializa el ID con un UUID generado aleatoriamente, el saldo en cero
     * y la lista de miembros vacía.
     * </p>
     *
     * @param nombreAgrupacion el nombre de la nueva agrupación
     */
    public Agrupacion(String nombreAgrupacion) {
        this.idAgrupacion = UUID.randomUUID().toString();
        this.nombreAgrupacion = nombreAgrupacion;
        this.saldoTotal = 0;
        this.idMiembros = new ArrayList<>();
    }

    /**
     * Agrega un miembro a la agrupación por su ID.
     * <p>
     * Si el usuario ya es miembro, no se realiza ninguna acción.
     * </p>
     *
     * @param idUsuario el ID del usuario a agregar como miembro
     * @return {@code true} si el miembro fue agregado exitosamente,
     *         {@code false} si ya era miembro de la agrupación
     */
    public boolean agregarMiembro(String idUsuario) {
        if (!this.idMiembros.contains(idUsuario)) {
            this.idMiembros.add(idUsuario);
            return true;
        }
        return false;
    }

    /**
     * Quita un miembro de la agrupación por su ID.
     * <p>
     * Si el usuario no es miembro de la agrupación, no se realiza ninguna acción.
     * </p>
     *
     * @param idMiembro el ID del usuario a quitar de la agrupación
     * @return {@code true} si el miembro fue eliminado exitosamente,
     *         {@code false} si no era miembro de la agrupación
     */
    public boolean quitarMiembro(String idMiembro){
        if (this.idMiembros.contains(idMiembro)) {
            this.idMiembros.remove(idMiembro);
            return true;
        }
        return false;
    }

    /**
     * Obtiene el identificador único de la agrupación.
     *
     * @return el ID de la agrupación
     */
    public String getIdAgrupacion() {
        return this.idAgrupacion;
    }

    /**
     * Obtiene el nombre de la agrupación.
     *
     * @return el nombre de la agrupación
     */
    public String getNombreAgrupacion() {
        return this.nombreAgrupacion;
    }

    /**
     * Obtiene el saldo total de la agrupación.
     *
     * @return el saldo total
     */
    public int getSaldoTotal(){
        return this.saldoTotal;
    }

    /**
     * Obtiene la lista de IDs de los miembros de la agrupación.
     *
     * @return lista de IDs de los miembros
     */
    public List<String> getIdMiembros(){
        return this.idMiembros;
    }

    /**
     * Establece el nombre de la agrupación.
     *
     * @param nombreAgrupacion el nuevo nombre
     */
    public void setNombreAgrupacion(String nombreAgrupacion){
        this.nombreAgrupacion = nombreAgrupacion;
    }

    /**
     * Establece el saldo total de la agrupación.
     *
     * @param saldoTotal el nuevo saldo
     */
    public void setSaldoTotal(int saldoTotal) {
        this.saldoTotal = saldoTotal;
    }

    /**
     * Establece el identificador de la agrupación.
     *
     * @param idAgrupacion el nuevo ID
     */
    public void setIdAgrupacion(String idAgrupacion) {
        this.idAgrupacion = idAgrupacion;
    }

    /**
     * Retorna la representación en cadena de la agrupación.
     *
     * @return el nombre de la agrupación
     */
    @Override
    public String toString() {
        return this.nombreAgrupacion;
    }
}
