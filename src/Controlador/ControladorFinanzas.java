package Controlador;

import Modelo.Movimiento;
import Modelo.TipoMovimiento;
import java.util.List;

public class ControladorFinanzas {
    public void registrarMovimiento(String idAgrupacion, TipoMovimiento tipo, int monto, String descripcion, String rutaComprobante, String idUsuario) {
        if (monto <= 0) {
            System.out.println("Error: El monto debe ser mayor a 0.");
            return;
        }

        if (tipo == TipoMovimiento.EGRESO) {
            int saldoActual = calcularSaldo(idAgrupacion);
            if (monto > saldoActual) {
                System.out.println("Error: Saldo insuficiente.");
                return;
            }
        }

        Movimiento nuevo = new Movimiento(idAgrupacion, tipo, monto, descripcion, rutaComprobante, idUsuario);

        GestorArchivosCSV.guardarMovimiento(nuevo);
        System.out.println("Movimiento registrado con éxito.");
    }

    public int calcularSaldo(String idAgrupacion) {
        List<String[]> historial = GestorArchivosCSV.leerLineasMovimientos(idAgrupacion);
        int saldoTotal = 0;

        for (String[] datos : historial) {
            String tipo = datos[2];
            int monto = Integer.parseInt(datos[3]);
            if (tipo.equals("INGRESO")) {
                saldoTotal += monto;
            } else if (tipo.equals("EGRESO")) {
                saldoTotal -= monto;
            }
        }
        return saldoTotal;
    }

    public void mostrarHistorial(String idAgrupacion) {
        List<String[]> historial = GestorArchivosCSV.leerLineasMovimientos(idAgrupacion);

        System.out.println("\n---- Historial de Movimientos ----\n");
        if (historial.isEmpty()) {
            System.out.println("No hay movimientos.");
        } else {
            for (String[] d : historial) {
                // variables: d[4]=Fecha, d[2]=Tipo, d[3]=Monto, d[5]=Descripción, d[6]=RutaFoto
                System.out.println("[" + d[4] + "] " + d[2] + " por $" + d[3] + " | " + d[5] + " (Comprobante: " + d[6] + ")");
            }
        }
    }
}