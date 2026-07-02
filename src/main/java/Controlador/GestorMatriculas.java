package Controlador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class GestorMatriculas implements IFuenteMatriculas {

    private static final String RUTA_MATRICULAS_EXTERNAS = "data/matriculas_externas.csv";
    private static final String PATRON_MATRICULA = "^[0-9k]{11}$";

    public static String normalizarMatricula(String matricula) {
        if (matricula == null) {
            return "";
        }
        return matricula.trim().toLowerCase();
    }

    public static boolean validarFormatoMatricula(String matricula) {
        if (matricula == null || matricula.isEmpty()) {
            return false;
        }
        String matriculaNormalizada = normalizarMatricula(matricula);
        return Pattern.matches(PATRON_MATRICULA, matriculaNormalizada);
    }

    @Override
    public String buscarNombrePorMatricula(String matricula) {
        String matriculaBuscada = normalizarMatricula(matricula);
        File archivo = new File(RUTA_MATRICULAS_EXTERNAS);

        if (!archivo.exists()) {
            throw new IllegalStateException("No se encontró la base de datos de matrículas.");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length < 2) continue;

                String matriculaArchivo = normalizarMatricula(datos[0]);
                if (matriculaArchivo.equals(matriculaBuscada)) {
                    return datos[1];
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer la base de matrículas: " + e.getMessage());
        }
        throw new IllegalArgumentException("La matrícula ingresada no existe en el registro de la universidad.");
    }
}