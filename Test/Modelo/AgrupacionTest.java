package Modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AgrupacionTest {

    @Test
    void testAgregarMiembroNuevo() {

        Agrupacion a = new Agrupacion("Aprende POO");
        a.agregarMiembro("usuario-1");

        assertEquals(1, a.getIdMiembros().size());
        assertTrue(a.getIdMiembros().contains("usuario-1"));
    }

    @Test
    void testNoSeRepiteElMismoMiembroDosVeces() {

        Agrupacion a = new Agrupacion("Aprende POO");
        a.agregarMiembro("usuario-1");
        a.agregarMiembro("usuario-1");

        assertEquals(1, a.getIdMiembros().size());
    }

    @Test
    void testQuitarMiembroExistente() {

        Agrupacion a = new Agrupacion("Aprende POO");
        a.agregarMiembro("usuario-1");
        a.quitarMiembro("usuario-1");

        assertFalse(a.getIdMiembros().contains("usuario-1"));
    }

    @Test
    void testQuitarUnMiembroQueNoExiste() {

        Agrupacion a = new Agrupacion("Aprende POO");
        a.quitarMiembro("usuario-que-no-existe");


        assertTrue(a.getIdMiembros().isEmpty());

    }

    @Test
    void testCrearAgrupacionConNombre() {

        Agrupacion a = new Agrupacion("Aprende POO");

        assertEquals("Aprende POO", a.getNombreAgrupacion());
        assertEquals(0, a.getSaldoTotal());
        assertTrue(a.getIdMiembros().isEmpty());
        assertNotNull(a.getIdAgrupacion());

    }








}
