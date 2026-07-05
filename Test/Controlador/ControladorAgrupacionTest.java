package Controlador;

import Modelo.RolUsuario;
import Modelo.Usuario;
import Modelo.Agrupacion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ControladorAgrupacionTest {

    @Test
    void testCrearAgrupacionConNombreValido() {
        ControladorUsuario controladorUsuario = new ControladorUsuario(new GestorEstudiantesUniversidad());
        ControladorAgrupacion controladorAgrupacion = new ControladorAgrupacion(controladorUsuario);

        Agrupacion nueva = controladorAgrupacion.crearAgrupacion("Agrupacion de prueba " + System.currentTimeMillis());

        assertNotNull(nueva);
    }

    @Test
    void testNoDejaCrearAgrupacionConNombreVacio() {
        ControladorUsuario controladorUsuario = new ControladorUsuario(new GestorEstudiantesUniversidad());
        ControladorAgrupacion controladorAgrupacion = new ControladorAgrupacion(controladorUsuario);

        assertThrows(IllegalArgumentException.class, () -> {
            controladorAgrupacion.crearAgrupacion("");
        });
    }

    @Test
    void testNoDejaCrearAgrupacionConNombreDemasiadoLargo() {
        ControladorUsuario controladorUsuario = new ControladorUsuario(new GestorEstudiantesUniversidad());
        ControladorAgrupacion controladorAgrupacion = new ControladorAgrupacion(controladorUsuario);


        String nombreDemasiadoLargo = "a".repeat(151);

        assertThrows(IllegalArgumentException.class, () -> {
            controladorAgrupacion.crearAgrupacion(nombreDemasiadoLargo);
        });
    }

    @Test
    void testNoDejaCrearDosAgrupacionesConElMismoNombre() {

        ControladorUsuario controladorUsuario = new ControladorUsuario(new GestorEstudiantesUniversidad());
        ControladorAgrupacion controladorAgrupacion = new ControladorAgrupacion(controladorUsuario);

        String nombreRepetido = "Agrupacion repetida " + System.currentTimeMillis();
        controladorAgrupacion.crearAgrupacion(nombreRepetido);

        assertThrows(IllegalArgumentException.class, () -> {
            controladorAgrupacion.crearAgrupacion(nombreRepetido);
        });
    }

    @Test
    void testNoDejaEliminarLaAgrupacionPrincipal() {
        ControladorUsuario controladorUsuario = new ControladorUsuario(new GestorEstudiantesUniversidad());
        ControladorAgrupacion controladorAgrupacion = new ControladorAgrupacion(controladorUsuario);

        assertThrows(IllegalArgumentException.class, () -> {
            controladorAgrupacion.eliminarAgrupacion("agrup-001");
        });
    }

    @Test
    void testAgregarYQuitarMiembroDeUnaAgrupacion() {

        ControladorUsuario controladorUsuario = new ControladorUsuario(new GestorEstudiantesUniversidad());
        ControladorAgrupacion controladorAgrupacion = new ControladorAgrupacion(controladorUsuario);

        Usuario usuario = controladorUsuario.registrarUsuario("agrup-001", RolUsuario.SOCIO, "123", "11111111108");
        Agrupacion nueva = controladorAgrupacion.crearAgrupacion("Grupo de prueba " + System.currentTimeMillis());

        controladorAgrupacion.agregarMiembro(nueva.getIdAgrupacion(), usuario);
        assertTrue(nueva.getIdMiembros().contains(usuario.getIdUsuario()));

        controladorAgrupacion.quitarMiembro(nueva.getIdAgrupacion(), usuario);
        assertFalse(nueva.getIdMiembros().contains(usuario.getIdUsuario()));
    }









}
