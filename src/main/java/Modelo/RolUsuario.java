package Modelo;

/**
 * Enumeración que define los roles de usuario disponibles en el sistema.
 * <p>
 * Los roles determinan las funcionalidades a las que un usuario puede acceder:
 * </p>
 * <ul>
 *   <li><strong>ADMIN</strong> - Tiene acceso completo: puede gestionar usuarios,
 *       agrupaciones y miembros, además de cambiar su propia contraseña.</li>
 *   <li><strong>TESORERO</strong> - Puede registrar ingresos y egresos, consultar
 *       el saldo y el historial de movimientos de su agrupación, y cambiar su
 *       contraseña.</li>
 *   <li><strong>SOCIO</strong> - Puede consultar el saldo y el historial de
 *       movimientos de su agrupación, y cambiar su contraseña.</li>
 * </ul>
 *
 * @author CLARA Team
 * @version 1.0
 */
public enum RolUsuario {
    /** Rol de administrador con acceso completo al sistema. */
    ADMIN,
    /** Rol de tesorero con capacidad de gestionar movimientos financieros. */
    TESORERO,
    /** Rol de socio con acceso de solo lectura a la información financiera. */
    SOCIO
}
