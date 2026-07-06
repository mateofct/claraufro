package Controlador;

/**
 * Interfaz que define el contrato para obtener el nombre real de un estudiante
 * a partir de su matrícula universitaria.
 * <p>
 * Se utiliza para abstraer la fuente de datos de matrículas, permitiendo
 * intercambiar implementaciones (por ejemplo, leer desde un archivo CSV o
 * conectarse a una base de datos externa) sin modificar el código del cliente.
 * </p>
 *
 * @author CLARA Team
 * @version 1.0
 * @see Controlador.GestorEstudiantesUniversidad
 */
public interface IFuenteMatriculas {

    /**
     * Busca el nombre real de un estudiante dado su número de matrícula.
     * <p>
     * La matrícula puede estar en cualquier formato de mayúsculas/minúsculas;
     * la implementación debe normalizarla internamente antes de realizar la búsqueda.
     * </p>
     *
     * @param matricula el número de matrícula del estudiante (11 caracteres, dígitos y 'k')
     * @return el nombre completo del estudiante asociado a la matrícula
     * @throws IllegalArgumentException si la matrícula tiene un formato inválido
     *                                  o no se encuentra en la base de datos
     * @throws RuntimeException si ocurre un error al leer la fuente de datos
     */
    String buscarNombrePorMatricula(String matricula);
}
