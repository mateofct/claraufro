package Controlador;

import Modelo.RolUsuario;
import Modelo.Usuario;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ControladorUsuarioTest {

    @Test
    void testIniciarSesionConDatosCorrectos() {

        ControladorUsuario controlador = new ControladorUsuario(new GestorEstudiantesUniversidad());

        controlador.registrarUsuario("agrup-001", RolUsuario.SOCIO, "123", "11111111101");

        boolean resultado = controlador.iniciarSesion("11111111101", "123");

        assertTrue(resultado);
        assertNotNull(controlador.getUsuarioActivo());
    }

    @Test
    void testIniciarSesionConDatosIncorrectos() {

        ControladorUsuario controlador = new ControladorUsuario(new GestorEstudiantesUniversidad());
        controlador.registrarUsuario("agrup-001", RolUsuario.SOCIO, "clave123", "11111111102");

        boolean resultado = controlador.iniciarSesion("11111111102", "claveIncorrecta");

        assertFalse(resultado);
    }

    @Test
    void testRegistrarUsuarioNuevo() {

        ControladorUsuario controlador = new ControladorUsuario(new GestorEstudiantesUniversidad());
        Usuario nuevoUsuario = controlador.registrarUsuario("agrup-001", RolUsuario.SOCIO, "123", "11111111103");

        assertEquals("Estudiante Tres", nuevoUsuario.getNombre());
        assertEquals(RolUsuario.SOCIO, nuevoUsuario.getRol());
    }

    @Test
    void testNoRegistrarLaMismaMatriculaDosVeces() {

        ControladorUsuario controlador = new ControladorUsuario(new GestorEstudiantesUniversidad());
        controlador.registrarUsuario("agrup-001", RolUsuario.SOCIO, "123", "11111111104");

        assertThrows(IllegalArgumentException.class, () -> {
            controlador.registrarUsuario("agrup-001", RolUsuario.SOCIO, "123", "11111111104");
        });
    }

    @Test
    void testNoDejaRegistrarUnaMatriculaQueNoExisteEnLaUniversidad() {

        ControladorUsuario controlador = new ControladorUsuario(new GestorEstudiantesUniversidad());

        assertThrows(IllegalArgumentException.class, () -> {
            controlador.registrarUsuario("agrup-001", RolUsuario.SOCIO, "123", "00000000024");
        });
    }

    @Test
    void testNoDejaRegistrarConContraseñaMuyLarga() {

        ControladorUsuario controlador = new ControladorUsuario(new GestorEstudiantesUniversidad());
        String contraseñaMuyLarga = "1234567890123456789012345";

        assertThrows(IllegalArgumentException.class, () -> {
            controlador.registrarUsuario("agrup-001", RolUsuario.SOCIO, contraseñaMuyLarga, "11111111105");
        });
    }

    @Test
    void testNoSePuedeEliminarAlAdministrador() {

        ControladorUsuario controlador = new ControladorUsuario(new GestorEstudiantesUniversidad());
        Usuario admin = controlador.registrarUsuario("agrup-001", RolUsuario.ADMIN, "123", "11111111106");

        assertThrows(IllegalStateException.class, () -> {controlador.eliminarUsuario(admin.getIdUsuario());
        });
    }

    @Test
    void testCerrarSesionDejaSinUsuarioActivo() {

        ControladorUsuario controlador = new ControladorUsuario(new GestorEstudiantesUniversidad());
        controlador.registrarUsuario("agrup-001", RolUsuario.SOCIO, "123", "11111111107");
        controlador.iniciarSesion("11111111106", "123");
        controlador.cerrarSesion();

        assertNull(controlador.getUsuarioActivo());
    }
}
