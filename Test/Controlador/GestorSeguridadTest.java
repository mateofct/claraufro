package Controlador;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GestorSeguridadTest {

    @Test
    void testCifrarYLuegoDescifrarDevuelveElTextoOriginal() {
        String textoOriginal = "miContraseña123";

        String textoCifrado = GestorSeguridad.cifrar(textoOriginal);
        String textoDescifrado = GestorSeguridad.descifrar(textoCifrado);

        assertEquals(textoOriginal, textoDescifrado);
    }

    @Test
    void testElTextoCifradoNoEsIgualAlOriginal() {
        String textoOriginal = "123";

        String textoCifrado = GestorSeguridad.cifrar(textoOriginal);

        assertNotEquals(textoOriginal, textoCifrado);
    }

    @Test
    void testCifrarUnTextoNuloDevuelveNulo() {
        assertNull(GestorSeguridad.cifrar(null));
    }

    @Test
    void testDescifrarUnTextoNuloDevuelveNulo() {
        assertNull(GestorSeguridad.descifrar(null));
    }
}
