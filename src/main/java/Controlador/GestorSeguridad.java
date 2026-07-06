package Controlador;

/**
 * Clase utilitaria que proporciona funciones básicas de cifrado y descifrado
 * de texto mediante un desplazamiento fijo de caracteres.
 * <p>
 * Utiliza un cifrado de César con un desplazamiento de 6 posiciones para
 * proteger las contraseñas almacenadas en archivos CSV. Cada carácter del
 * texto original se desplaza 6 posiciones hacia adelante en la tabla ASCII
 * para cifrar, y 6 posiciones hacia atrás para descifrar.
 * </p>
 * <p>
 * <strong>Nota:</strong> Este método de cifrado es simple y no debe considerarse
 * seguro para entornos de producción reales. Se recomienda el uso de
 * algoritmos de hash robustos (como BCrypt o Argon2) en aplicaciones
 * comerciales.
 * </p>
 *
 * @author CLARA Team
 * @version 1.0
 */
public class GestorSeguridad {

    /**
     * Constante que define el desplazamiento fijo (en posiciones de la tabla ASCII)
     * aplicado a cada carácter durante el proceso de cifrado y descifrado.
     */
    private static final int DESPLAZAMIENTO = 6;

    /**
     * Cifra un texto aplicando un desplazamiento fijo a cada carácter.
     * <p>
     * Cada carácter del texto de entrada se desplaza {@value #DESPLAZAMIENTO}
     * posiciones hacia adelante en la tabla de códigos Unicode.
     * </p>
     *
     * @param texto el texto sin cifrar
     * @return el texto cifrado, o {@code null} si el parámetro es {@code null}
     */
    public static String cifrar(String texto) {
        if (texto == null) return null;
        StringBuilder resultado = new StringBuilder();
        for (char c : texto.toCharArray()) {
            resultado.append((char) (c + DESPLAZAMIENTO));
        }
        return resultado.toString();
    }

    /**
     * Descifra un texto que fue previamente cifrado con {@link #cifrar(String)}.
     * <p>
     * Cada carácter del texto cifrado se desplaza {@value #DESPLAZAMIENTO}
     * posiciones hacia atrás en la tabla de códigos Unicode para recuperar
     * el texto original.
     * </p>
     *
     * @param texto el texto cifrado
     * @return el texto original descifrado, o {@code null} si el parámetro es {@code null}
     */
    public static String descifrar(String texto) {
        if (texto == null) return null;
        StringBuilder resultado = new StringBuilder();
        for (char c : texto.toCharArray()) {
            resultado.append((char) (c - DESPLAZAMIENTO));
        }
        return resultado.toString();
    }
}
