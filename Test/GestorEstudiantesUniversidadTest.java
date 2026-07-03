import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import Controlador.GestorEstudiantesUniversidad;

public class GestorEstudiantesUniversidadTest {
    @Test
    public void testBuscarNombrePorMatriculaValida() {
        GestorEstudiantesUniversidad gestor = new GestorEstudiantesUniversidad();
        String nombre = gestor.buscarNombrePorMatricula("44444444444");
        assertEquals("Javiera Perez", nombre);
    }

    @Test
    public void testBuscarNombrePorMatriculaInvalida() {
        GestorEstudiantesUniversidad gestor = new GestorEstudiantesUniversidad();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gestor.buscarNombrePorMatricula("12345678k24");
        });
        assertEquals("La matrícula 12345678k24 no existe en el registro base de la universidad", exception.getMessage());
    }

    @Test
    public void testBuscarNombrePorMatriculaFormatoInvalido() {
        GestorEstudiantesUniversidad gestor = new GestorEstudiantesUniversidad();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gestor.buscarNombrePorMatricula("12345");
        });
        assertTrue(exception.getMessage().contains("La matrícula no tiene un formato válido: 12345. Debe tener exactamente 11 números (incluyendo la letra 'k')"));
    }



}
