package Controlador;

import java.util.regex.Pattern;

/**
 * Clase utilitaria que proporciona funciones para validar y normalizar
 * matrículas de estudiantes universitarios.
 * <p>
 * Las matrículas válidas constan de exactamente 11 caracteres compuestos
 * por dígitos (0-9) y la letra 'k'. Esta clase normaliza todas las
 * matrículas a minúsculas para garantizar comparaciones consistentes.
 * </p>
 *
 * @author CLARA Team
 * @version 1.0
 */
public class GestorMatriculas {

    /**
     * Patrón de expresión regular que define el formato válido de una matrícula:
     * exactamente 11 caracteres de dígitos (0-9) y la letra 'k'.
     */
    private static final String PATRON_MATRICULA = "^[0-9k]{11}$";

    /**
     * Normaliza una matrícula eliminando espacios en blanco extremos y
     * convirtiéndola a minúsculas para garantizar comparaciones uniformes.
     *
     * @param matricula la matrícula a normalizar
     * @return la matrícula normalizada en minúsculas y sin espacios,
     *         o una cadena vacía si el parámetro es {@code null}
     */
    public static String normalizarMatricula(String matricula) {
        if (matricula == null) {
            return "";
        }
        return matricula.trim().toLowerCase();
    }

    /**
     * Valida que una matrícula cumpla con el formato exigido:
     * exactamente 11 caracteres compuestos por dígitos (0-9) y la letra 'k'.
     * <p>
     * La validación se realiza sobre la versión normalizada de la matrícula
     * (minúsculas y sin espacios extremos).
     * </p>
     *
     * @param matricula la matrícula a validar
     * @return {@code true} si la matrícula es válida, {@code false} en caso contrario
     */
    public static boolean validarFormatoMatricula(String matricula) {
        if (matricula == null || matricula.isEmpty()) {
            return false;
        }
        String matriculaNormalizada = normalizarMatricula(matricula);
        return Pattern.matches(PATRON_MATRICULA, matriculaNormalizada);
    }
}
