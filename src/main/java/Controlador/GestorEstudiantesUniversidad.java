package Controlador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GestorEstudiantesUniversidad {
    private static final String RUTA_BASE_ESTUDIANTES_UNIVERSIDAD = "data/estudiantes_universidad.csv";

    public GestorEstudiantesUniversidad() {

    }

    public String buscarNombrePorMatricula(String matricula) throws IOException {
        String matriculaBuscada = GestorMatriculas.normalizarMatricula(matricula);

        if (!GestorMatriculas.validarFormatoMatricula(matriculaBuscada)) {
            throw new IllegalArgumentException("La matrícula no tiene un formato válido: " + matricula +
                    ". Debe tener exactamente 11 números (incluyendo la letra 'k')");
        }

        File archivo = new File(RUTA_BASE_ESTUDIANTES_UNIVERSIDAD);
        if (!archivo.exists()) {
            throw new IOException("No se encontró el archivo de la base de datos de estudiantes en: " + RUTA_BASE_ESTUDIANTES_UNIVERSIDAD);
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                String[] datos = linea.split(";");

                if (datos.length >= 2) {
                    String matriculaArchivo = GestorMatriculas.normalizarMatricula(datos[0]);

                    if (matriculaArchivo.equals(matriculaBuscada)) {
                        return datos[1];
                    }
                }
            }
        }

        throw new IllegalArgumentException("La matrícula " + matricula + " no existe en el registro base de la universidad");
    }
}
