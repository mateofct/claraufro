package Controlador;

import Modelo.Movimiento;
import Modelo.TipoMovimiento;

import java.util.ArrayList;
import java.util.List;

public class ControladorFinanzas {

    private GestorDocumentos gestorDocumentos;

    public ControladorFinanzas(){
        this.gestorDocumentos = new GestorDocumentos();
    }

    public void registrarMovimiento(String idAgrupacion, TipoMovimiento tipo, int monto, String descripcion, String rutaOrigen, String idUsuario) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0.");
        }

        if (tipo == TipoMovimiento.EGRESO) {
            int saldoActual = calcularSaldo(idAgrupacion);
            if (monto > saldoActual) {
                throw new IllegalArgumentException("Saldo insuficiente.");
            }
        }

        String nombreComprobanteLocal = gestorDocumentos.guardarComprobanteLocal(rutaOrigen);

        Movimiento nuevo = new Movimiento(idAgrupacion, tipo, monto, descripcion, nombreComprobanteLocal, idUsuario);

        // por si gestorarchivoscsv falla
        GestorArchivosCSV.guardarMovimiento(nuevo);
    }

    public int calcularSaldo(String idAgrupacion) {
        List<String[]> historial = GestorArchivosCSV.leerLineasMovimientos(idAgrupacion);
        int saldoTotal = 0;

        for (String[] datos : historial) {
            // se ignoran las filas que no tengan todas las columnas
            if (datos.length < 8) continue;

            String tipo = datos[2];
            try {
                int monto = Integer.parseInt(datos[3]);
                if (tipo.equals("INGRESO")) {
                    saldoTotal += monto;
                } else if (tipo.equals("EGRESO")) {
                    saldoTotal -= monto;
                }
            } catch (NumberFormatException e) {
                // si hay letras en el monto se ignora la fila
                continue;
            }
        }
        return saldoTotal;
    }

    public List<String[]> filtrarMovimientos(String idAgrupacion, String fechaBuscada, String tipoBuscado) {
        List<String[]> todos = GestorArchivosCSV.leerLineasMovimientos(idAgrupacion);
        List<String[]> resultado = new ArrayList<>();

        for  (String[] mov : todos) {
            // se ignoran las filas incompletas
            if (mov.length < 8) continue;

            boolean coincideFecha = (fechaBuscada == null || fechaBuscada.isEmpty() || mov[4].equals(fechaBuscada));
            boolean coincideTipo = (tipoBuscado == null || tipoBuscado.isEmpty() || mov[2].equalsIgnoreCase(tipoBuscado));

            if (coincideFecha && coincideTipo) {
                resultado.add(mov);
            }
        }
        return resultado;
    }

    public void pedirAbrirComprobante(String nombreArchivo) {
        gestorDocumentos.abrirComprobante(nombreArchivo);
    }
}