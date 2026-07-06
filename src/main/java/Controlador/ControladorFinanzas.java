package Controlador;

import Modelo.*;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador responsable de toda la lógica de negocio relacionada con las
 * finanzas de las agrupaciones.
 * <p>
 * Proporciona funcionalidades para registrar movimientos financieros (ingresos
 * y egresos), calcular el saldo de una agrupación, filtrar el historial de
 * movimientos y gestionar la visualización de comprobantes asociados.
 * </p>
 *
 * @author CLARA Team
 * @version 1.0
 */
public class ControladorFinanzas {

    /**
     * Gestor de documentos responsable de almacenar y abrir los archivos
     * de comprobantes financieros.
     */
    private GestorDocumentos gestorDocumentos;

    /**
     * Constructor que inicializa el controlador creando una nueva instancia
     * del {@link GestorDocumentos}.
     */
    public ControladorFinanzas(){
        this.gestorDocumentos = new GestorDocumentos();
    }

    /**
     * Registra un nuevo movimiento financiero (ingreso o egreso) para una agrupación.
     * <p>
     * Antes de registrar, valida que el monto sea positivo. Para los egresos,
     * verifica que el saldo actual de la agrupación sea suficiente para cubrir
     * el monto. El comprobante asociado se guarda en el sistema de archivos
     * mediante el {@link GestorDocumentos}.
     * </p>
     *
     * @param idAgrupacion el ID de la agrupación a la que pertenece el movimiento
     * @param tipo el tipo de movimiento ({@link TipoMovimiento#INGRESO} o
     *        {@link TipoMovimiento#EGRESO})
     * @param monto el valor monetario del movimiento (debe ser mayor a 0)
     * @param descripcion una descripción textual del movimiento
     * @param rutaOrigen la ruta del archivo de comprobante en el sistema de archivos
     * @param idUsuario el ID del usuario que registra el movimiento
     * @throws IllegalArgumentException si el monto no es positivo o si se intenta
     *         registrar un egreso con saldo insuficiente
     */
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

    /**
     * Calcula el saldo total de una agrupación sumando todos sus movimientos
     * financieros registrados.
     * <p>
     * Los ingresos suman al saldo y los egresos lo restan. Las filas incompletas
     * (menos de 8 columnas) o con montos no numéricos se ignoran.
     * </p>
     *
     * @param idAgrupacion el ID de la agrupación cuyo saldo se desea calcular
     * @return el saldo total calculado (puede ser negativo si los egresos superan
     *         los ingresos)
     */
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
            }
        }
        return saldoTotal;
    }

    /**
     * Filtra los movimientos de una agrupación por fecha y/o tipo de movimiento.
     * <p>
     * Si se proporciona una fecha de búsqueda, solo se incluyen los movimientos
     * de esa fecha. Si se proporciona un tipo, solo se incluyen los movimientos
     * del tipo especificado (insensible a mayúsculas/minúsculas). Si ambos
     * parámetros son nulos o vacíos, se retornan todos los movimientos.
     * </p>
     * <p>
     * Además, reemplaza el ID del usuario registrador (columna 7) por el nombre
     * real del usuario para facilitar la visualización.
     * </p>
     *
     * @param idAgrupacion el ID de la agrupación cuyos movimientos se desean filtrar
     * @param fechaBuscada la fecha de filtro en formato {@code dd/MM/yyyy},
     *        o {@code null}/vacío para no filtrar por fecha
     * @param tipoBuscado el tipo de movimiento a filtrar ("INGRESO" o "EGRESO"),
     *        o {@code null}/vacío para no filtrar por tipo
     * @return lista de arreglos de cadenas con los movimientos filtrados,
     *         donde la columna 7 contiene el nombre del usuario en lugar del ID
     */
    public List<String[]> filtrarMovimientos(String idAgrupacion, String fechaBuscada, String tipoBuscado) {
        List<String[]> todos = GestorArchivosCSV.leerLineasMovimientos(idAgrupacion);
        List<Usuario> listaUsuarios = GestorArchivosCSV.cargarUsuarios();
        List<String[]> resultado = new ArrayList<>();

        String fechaLimpia = (fechaBuscada != null) ? fechaBuscada.trim() : "";
        String tipoLimpio = (tipoBuscado != null) ? tipoBuscado.trim() : "";

        for  (String[] mov : todos) {
            // se ignoran las filas incompletas
            if (mov.length < 8) continue;

            boolean coincideFecha = (fechaBuscada == null || fechaBuscada.isEmpty() || mov[4].equals(fechaBuscada));
            boolean coincideTipo = (tipoBuscado == null || tipoBuscado.isEmpty() || mov[2].equalsIgnoreCase(tipoBuscado));

            if (coincideFecha && coincideTipo) {

                String idUsuarioBuscado = mov[7];
                String nombreReal = "Desconocido";
                for (Usuario u : listaUsuarios) {
                    if (u.getIdUsuario().equals(idUsuarioBuscado)) {
                        nombreReal = u.getNombre();
                        break;
                    }
                }
                mov[7] =  nombreReal;
                resultado.add(mov);
            }
        }
        return resultado;
    }

    /**
     * Solicita abrir un archivo de comprobante en la aplicación asociada del sistema
     * operativo. Si ocurre un error, muestra un mensaje de error al usuario.
     *
     * @param nombreArchivo el nombre del archivo de comprobante a abrir
     */
    public void pedirAbrirComprobante(String nombreArchivo) {
        try {
            gestorDocumentos.abrirComprobante(nombreArchivo);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error al abrir comprobante", JOptionPane.ERROR_MESSAGE);
        }

    }
}
