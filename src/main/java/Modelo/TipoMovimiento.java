package Modelo;

/**
 * Enumeración que define los tipos de movimiento financiero registrados
 * en el sistema.
 * <p>
 * Un <strong>ingreso</strong> suma al saldo de la agrupación, mientras que
 * un <strong>egreso</strong> lo resta.
 * </p>
 *
 * @author CLARA Team
 * @version 1.0
 */
public enum TipoMovimiento {
    /** Movimiento que incrementa el saldo de la agrupación. */
    INGRESO,
    /** Movimiento que decrementa el saldo de la agrupación. */
    EGRESO
}
