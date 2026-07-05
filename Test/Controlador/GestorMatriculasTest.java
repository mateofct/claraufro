package Controlador;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GestorMatriculasTest {

    @Test
    void testNormalizarQuitaEspaciosDeLosExtremos() {
        String resultado = GestorMatriculas.normalizarMatricula("  1111111111k  ");

        assertEquals("1111111111k", resultado);
    }

    @Test
    void testNormalizarPasaTodoAMinusculas() {
        String resultado = GestorMatriculas.normalizarMatricula("1111111111K");

        assertEquals("1111111111k", resultado);
    }

    @Test
    void testNormalizarUnTextoNuloDevuelveTextoVacio() {
        assertEquals("", GestorMatriculas.normalizarMatricula(null));
    }

    @Test
    void testValidarFormatoConMatriculaValidaDe11Caracteres() {

        assertTrue(GestorMatriculas.validarFormatoMatricula("12345678901"));
    }

    @Test
    void testValidarFormatoConMatriculaDeLargoIncorrecto() {

        assertFalse(GestorMatriculas.validarFormatoMatricula("12345"));
    }

    @Test
    void testValidarFormatoConLetrasNoPermitidas() {

        assertFalse(GestorMatriculas.validarFormatoMatricula("1234567890a"));
    }

    @Test
    void testValidarFormatoConMayusculasYEspaciosFuncionanJuntos() {

        assertTrue(GestorMatriculas.validarFormatoMatricula("  1111111111K  "));
    }
}
