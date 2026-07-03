package Controlador;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class GestorEstudiantesUniversidadTest {
    @Test
    public void testBuscarNombrePorMatriculaValida() {
        GestorEstudiantesUniversidad gestor = new GestorEstudiantesUniversidad();
        String nombre = gestor.buscarNombrePorMatricula("44444444444");
        assertEquals("Javiera Perez", nombre);
    }

    @Test
    public void testBuscarNombrePorMatriculaNoExiste() {
        GestorEstudiantesUniversidad gestor = new GestorEstudiantesUniversidad();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestor.buscarNombrePorMatricula("12345678k24");
        });
        assertEquals("La matrícula 12345678k24 no existe en el registro base de la universidad", exception.getMessage());
    }

    @Test
    public void testBuscarNombrePorMatriculaFormatoInvalido() {
        GestorEstudiantesUniversidad gestor = new GestorEstudiantesUniversidad();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestor.buscarNombrePorMatricula("12345");
        });
        assertTrue(exception.getMessage().contains("La matrícula no tiene un formato válido: 12345. " +
                "Debe tener exactamente 11 números (incluyendo la letra 'k')"));
    }

    @Test
    public void testBaseDatosNoEncontrada(){
        String rutaFalsa = "data/base_que_no_existe.csv";
        GestorEstudiantesUniversidad gestor = new GestorEstudiantesUniversidad(rutaFalsa);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gestor.buscarNombrePorMatricula("44444444444");
        });
        assertEquals("No se encontró el archivo de la base de datos de estudiantes en: "
                + rutaFalsa, exception.getMessage());
    }



}
