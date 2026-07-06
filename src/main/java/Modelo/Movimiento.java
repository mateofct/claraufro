package Modelo;

import java.util.UUID;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Modelo que representa un movimiento financiero (ingreso o egreso)
 * registrado para una agrupación.
 * <p>
 * Cada movimiento contiene un identificador único (UUID), el ID de la agrupación
 * a la que pertenece, el tipo de movimiento, el monto, la fecha de registro
 * (generada automáticamente al crear el objeto), una descripción, la ruta del
 * comprobante adjunto y el ID del usuario que registró el movimiento.
 * </p>
 * <p>
 * La fecha del movimiento se fija al momento de la creación y utiliza el formato
 * {@code dd/MM/yyyy}.
 * </p>
 *
 * @author CLARA Team
 * @version 1.0
 * @see TipoMovimiento
 * @see Controlador.ControladorFinanzas
 */
public class Movimiento {

    /**
     * Identificador único de la agrupación a la que pertenece el movimiento.
     */
    private String idAgrupacion;

    /**
     * Identificador único del movimiento, generado como UUID.
     */
    private String idMovimiento;

    /**
     * Tipo del movimiento: ingreso o egreso.
     */
    private TipoMovimiento tipoMovimiento;

    /**
     * Monto monetario del movimiento.
     */
    private int monto;

    /**
     * Fecha en que se registró el movimiento, en formato {@code dd/MM/yyyy}.
     */
    private String  fechaMovimiento;

    /**
     * Descripción textual del movimiento.
     */
    private String descripcionMovimiento;

    /**
     * Nombre del archivo de comprobante asociado al movimiento.
     */
    private String rutaComprobante;

    /**
     * Identificador del usuario que registró el movimiento.
     */
    private String idUsuarioQueRegistra;

    /**
     * Constructor que crea un nuevo movimiento financiero.
     * <p>
     * Genera automáticamente un UUID como identificador y fija la fecha
     * al día actual en formato {@code dd/MM/yyyy}.
     * </p>
     *
     * @param idAgrupacion el ID de la agrupación a la que pertenece
     * @param tipoMovimiento el tipo de movimiento ({@link TipoMovimiento#INGRESO}
     *        o {@link TipoMovimiento#EGRESO})
     * @param monto el valor monetario del movimiento
     * @param descripcionMovimiento descripción del movimiento
     * @param rutaComprobante nombre del archivo de comprobante
     * @param idUsuarioQueRegistra el ID del usuario que realiza el registro
     */
    public Movimiento(String idAgrupacion, TipoMovimiento tipoMovimiento, int monto, String descripcionMovimiento, String rutaComprobante, String idUsuarioQueRegistra) {
        this.idMovimiento = UUID.randomUUID().toString();
        this.idAgrupacion = idAgrupacion;
        this.tipoMovimiento = tipoMovimiento;
        this.monto = monto;
        this.descripcionMovimiento = descripcionMovimiento;
        this.rutaComprobante = rutaComprobante;
        this.idUsuarioQueRegistra = idUsuarioQueRegistra;
        this.fechaMovimiento = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /**
     * Obtiene el identificador único del movimiento.
     *
     * @return el ID del movimiento
     */
    public String getIdMovimiento() {
        return idMovimiento;
    }

    /**
     * Obtiene el ID de la agrupación a la que pertenece el movimiento.
     *
     * @return el ID de la agrupación
     */
    public String getIdAgrupacion() {
        return idAgrupacion;
    }

    /**
     * Obtiene el tipo de movimiento (ingreso o egreso).
     *
     * @return el tipo de movimiento
     */
    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    /**
     * Obtiene el monto del movimiento.
     *
     * @return el monto
     */
    public int getMonto() {
        return monto;
    }

    /**
     * Obtiene la fecha de registro del movimiento.
     *
     * @return la fecha en formato {@code dd/MM/yyyy}
     */
    public String getFechaMovimiento() {
        return fechaMovimiento;
    }

    /**
     * Obtiene la descripción del movimiento.
     *
     * @return la descripción
     */
    public String getDescripcionMovimiento() {
        return descripcionMovimiento;
    }

    /**
     * Obtiene el nombre del archivo de comprobante asociado.
     *
     * @return el nombre del comprobante
     */
    public String getRutaComprobante() {
        return rutaComprobante;
    }

    /**
     * Obtiene el ID del usuario que registró el movimiento.
     *
     * @return el ID del usuario registrador
     */
    public String getIdUsuarioQueRegistra() {
        return idUsuarioQueRegistra;
    }
}
