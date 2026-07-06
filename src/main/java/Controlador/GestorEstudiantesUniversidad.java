package Controlador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Implementación de {@link IFuenteMatriculas} que consulta un archivo CSV
 * para obtener el nombre real de un estudiante a partir de su matrícula.
 * <p>
 * El archivo CSV esperado debe tener el formato {@code matricula;nombre} por línea,
 * donde la matrícula consta de 11 caracteres (dígitos 0-9 y letra 'k').
 * Por defecto, busca el archivo en {@code data/base_estudiantes_universidad.csv},
 * pero permite especificar una ruta alternativa mediante el constructor.
 * </p>
 *
 * @author CLARA Team
 * @version 1.0
 * @see IFuenteMatriculas
 */
public class GestorEstudiantesUniversidad implements IFuenteMatriculas {

    /**
     * Ruta por defecto del archivo CSV con la base de datos de estudiantes.
     */
    private static final String RUTA_BASE_ESTUDIANTES_UNIVERSIDAD = "data/base_estudiantes_universidad.csv";

    /**
     * Ruta del archivo CSV de estudiantes, que puede ser la ruta por defecto
     * o una ruta alternativa proporcionada en la construcción.
     */
    private final String rutaArchivo;

    /**
     * Constructor por defecto que utiliza la ruta estándar
     * {@code data/base_estudiantes_universidad.csv} para consultar
     * las matrículas de los estudiantes.
     */
    public GestorEstudiantesUniversidad() {
        this.rutaArchivo = RUTA_BASE_ESTUDIANTES_UNIVERSIDAD;
    }

    /**
     * Constructor que permite especificar una ruta alternativa para el archivo
     * CSV de estudiantes.
     *
     * @param rutaAlternativa la ruta del archivo CSV de estudiantes
     */
    public GestorEstudiantesUniversidad(String rutaAlternativa) {
        this.rutaArchivo = rutaAlternativa;
    }

    /**
     * Busca el nombre real de un estudiante dado su número de matrícula,
     * consultando el archivo CSV configurado.
     * <p>
     * El proceso de búsqueda realiza las siguientes acciones:
     * <ol>
     *   <li>Normaliza la matrícula a minúsculas y sin espacios.</li>
     *   <li>Valida que la matrícula tenga el formato correcto (11 caracteres).</li>
     *   <li>Verifica que el archivo CSV exista en la ruta configurada.</li>
     *   <li>Recorre el archivo línea por línea buscando una coincidencia exacta.</li>
     * </ol>
     * </p>
     *
     * @param matricula el número de matrícula del estudiante a buscar
     * @return el nombre completo del estudiante asociado a la matrícula
     * @throws IllegalArgumentException si la matrícula tiene formato inválido
     *                                  o no se encuentra en el archivo CSV
     * @throws RuntimeException si el archivo CSV no existe o ocurre un error
     *                          al leerlo
     */
    @Override
    public String buscarNombrePorMatricula(String matricula) {

        try {
            String matriculaBuscada = GestorMatriculas.normalizarMatricula(matricula);

            if (!GestorMatriculas.validarFormatoMatricula(matriculaBuscada)) {
                throw new IllegalArgumentException("La matrícula no tiene un formato válido: " + matricula + ". Debe tener exactamente 11 números (incluyendo la letra 'k')");
            }

            File archivo = new File(this.rutaArchivo);

            if (!archivo.exists()) {
                throw new RuntimeException("No se encontró el archivo de la base de datos de estudiantes en: " + this.rutaArchivo);
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
        } catch (IOException e) {
            throw new RuntimeException("Error al leer la base de datos de estudiantes: " + e.getMessage());
        }

        throw new IllegalArgumentException("La matrícula " + matricula + " no existe en el registro base de la universidad");
    }
}
