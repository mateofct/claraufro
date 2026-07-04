package Controlador;

import Modelo.TipoMovimiento;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ControladorFinanzasTest {

    @Test
    void testRegistrarMovimientoConMontoCeroNegativo() {
        ControladorFinanzas controlador = new ControladorFinanzas();

        assertThrows(IllegalArgumentException.class, () -> {
            controlador.registrarMovimiento("agrup-001", TipoMovimiento.INGRESO, 0, "Cuota", "", "usuario-1");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            controlador.registrarMovimiento("agrup-001", TipoMovimiento.INGRESO, -500, "Cuota", "", "usuario-1");
        });
    }

    @Test
    void testRegistrarEgresoSiNoHaySaldoSuficiente() {

        ControladorFinanzas controlador = new ControladorFinanzas();
        String idAgrupacionDePrueba = "agrup-prueba-" + System.currentTimeMillis();

        assertThrows(IllegalArgumentException.class, () -> {
            controlador.registrarMovimiento(idAgrupacionDePrueba, TipoMovimiento.EGRESO, 10000, "Compra", "", "usuario-1");
        });
    }

    @Test
    void testRegistrarUnIngresoAumentaElSaldo() {

        ControladorFinanzas controlador = new ControladorFinanzas();
        String idAgrupacionDePrueba = "agrup-prueba-" + System.currentTimeMillis();

        int saldoAntes = controlador.calcularSaldo(idAgrupacionDePrueba);
        controlador.registrarMovimiento(idAgrupacionDePrueba, TipoMovimiento.INGRESO, 5000, "Cuota mensual", "", "usuario-1");
        int saldoDespues = controlador.calcularSaldo(idAgrupacionDePrueba);

        assertEquals(saldoAntes + 5000, saldoDespues);


    }

    @Test
    void testIngresoSeguidoDeUnEgresoDejaElSaldoCorrecto() {

        ControladorFinanzas controlador = new ControladorFinanzas();
        String idAgrupacionDePrueba = "agrup-prueba-" + System.currentTimeMillis();

        controlador.registrarMovimiento(idAgrupacionDePrueba, TipoMovimiento.INGRESO, 10000, "Cuota", "", "usuario-1");
        controlador.registrarMovimiento(idAgrupacionDePrueba, TipoMovimiento.EGRESO, 4000, "Compra computador", "", "usuario-1");

        int saldoFinal = controlador.calcularSaldo(idAgrupacionDePrueba);

        assertEquals(6000, saldoFinal);
    }

    @Test
    void testFiltrarSinFiltros() {

        ControladorFinanzas controlador = new ControladorFinanzas();
        String idAgrupacionDePrueba = "agrup-prueba-" + System.currentTimeMillis();

        controlador.registrarMovimiento(idAgrupacionDePrueba, TipoMovimiento.INGRESO, 1000, "Cuota", "", "usuario-1");
        controlador.registrarMovimiento(idAgrupacionDePrueba, TipoMovimiento.EGRESO, 500, "Compra", "", "usuario-1");

        List<String[]> resultado = controlador.filtrarMovimientos(idAgrupacionDePrueba, "", "");

        assertEquals(2, resultado.size());
    }

    @Test
    void testFiltrarPorTipo() {
        ControladorFinanzas controlador = new ControladorFinanzas();
        String idAgrupacionDePrueba = "agrup-prueba-" + System.currentTimeMillis();

        controlador.registrarMovimiento(idAgrupacionDePrueba, TipoMovimiento.INGRESO, 5000, "Cuota", "", "usuario-1");
        controlador.registrarMovimiento(idAgrupacionDePrueba, TipoMovimiento.EGRESO, 1000, "Compra", "", "usuario-1");

        List<String[]> resultado = controlador.filtrarMovimientos(idAgrupacionDePrueba, null, "INGRESO");

        assertEquals(1, resultado.size());
        assertEquals("INGRESO", resultado.get(0)[2]);
    }

    @Test
    void testFiltrarPorFechaDeHoy() {

        ControladorFinanzas controlador = new ControladorFinanzas();
        String idAgrupacionDePrueba = "agrup-prueba-" + System.currentTimeMillis();

        controlador.registrarMovimiento(idAgrupacionDePrueba, TipoMovimiento.INGRESO, 3000, "Cuota", "", "usuario-1");

        String fechaDeHoy = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        List<String[]> resultado = controlador.filtrarMovimientos(idAgrupacionDePrueba, fechaDeHoy, null);

        assertEquals(1, resultado.size());
    }

    @Test
    void testFiltrarPorUnaFechaQueNoCoincide() {

        ControladorFinanzas controlador = new ControladorFinanzas();
        String idAgrupacionDePrueba = "agrup-prueba-" + System.currentTimeMillis();

        controlador.registrarMovimiento(idAgrupacionDePrueba, TipoMovimiento.INGRESO, 3000, "Cuota", "", "usuario-1");

        List<String[]> resultado = controlador.filtrarMovimientos(idAgrupacionDePrueba, "01/01/1999", null);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void testElNombreDelUsuarioApareceComoDesconocidoSiNoExiste() {
        ControladorFinanzas controlador = new ControladorFinanzas();
        String idAgrupacionDePrueba = "agrup-prueba-" + System.currentTimeMillis();

        controlador.registrarMovimiento(idAgrupacionDePrueba, TipoMovimiento.INGRESO, 1000, "Cuota", "", "usuario-que-no-existe");

        List<String[]> resultado = controlador.filtrarMovimientos(idAgrupacionDePrueba, null, null);

        assertEquals("Desconocido", resultado.get(0)[7]);


    }


}
