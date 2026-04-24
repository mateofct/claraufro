package Controlador;

import Modelo.Movimiento;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorArchivosCSV {
    private static final String RUTA_MOVIMIENTOS = "data/movimientos_clara.csv";

    public static void guardarMovimiento(Movimiento mov) {
        try (FileWriter fw = new FileWriter(RUTA_MOVIMIENTOS, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            String linea = mov.getIdMovimiento() + "," +
                    mov.getIdAgrupacion() + "," +
                    mov.getTipoMovimiento() + "," +
                    mov.getMonto() + "," +
                    mov.getFechaMovimiento() + "," +
                    mov.getDescripcionMovimiento() + "," +
                    mov.getRutaComprobante() + "," +
                    mov.getIdUsuarioQueRegistra();

            out.println(linea);

        } catch (IOException e) {
            System.out.println("Error al guardar CSV: " + e.getMessage());
        }
    }

    public static List<String[]> leerLineasMovimientos(String idAgrupacionBuscada) {
        List<String[]> lista = new ArrayList<>();
        File archivo = new File(RUTA_MOVIMIENTOS);

        if (!archivo.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 8 && datos[1].equals(idAgrupacionBuscada)) {
                    lista.add(datos);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer CSV: " + e.getMessage());
        }
        return lista;
    }
}