package Controlador;

import Modelo.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase utilitaria que gestiona la persistencia de datos en archivos CSV.
 * <p>
 * Proporciona métodos estáticos para guardar y cargar usuarios, agrupaciones
 * y movimientos financieros en archivos CSV separados dentro del directorio
 * {@code data/}. Todos los archivos utilizan el punto y coma ({@code ;}) como
 * separador de campos.
 * </p>
 *
 * <h3>Estructura de los archivos CSV:</h3>
 * <table border="1" cellpadding="4" cellspacing="0">
 *   <caption>Formato de los archivos de datos</caption>
 *   <tr><th>Archivo</th><th>Columnas</th></tr>
 *   <tr>
 *     <td>{@code movimientos_clara.csv}</td>
 *     <td>idMovimiento ; idAgrupacion ; tipoMovimiento ; monto ; fecha ; descripcion ; rutaComprobante ; idUsuario</td>
 *   </tr>
 *   <tr>
 *     <td>{@code usuarios.csv}</td>
 *     <td>idUsuario ; idAgrupacion ; nombre ; contrasena ; rol ; matricula</td>
 *   </tr>
 *   <tr>
 *     <td>{@code agrupaciones.csv}</td>
 *     <td>idAgrupacion ; nombre ; saldoTotal ; listaIdsMiembros (separados por '-')</td>
 *   </tr>
 * </table>
 *
 * @author CLARA Team
 * @version 1.0
 */
public class GestorArchivosCSV {

    /**
     * Ruta del archivo CSV donde se almacenan los registros de movimientos financieros.
     */
    private static final String RUTA_MOVIMIENTOS = "data/movimientos_clara.csv";

    /**
     * Ruta del archivo CSV donde se almacenan los registros de usuarios del sistema.
     */
    private static final String RUTA_USUARIOS = "data/usuarios.csv";

    /**
     * Ruta del archivo CSV donde se almacenan los registros de agrupaciones.
     */
    private static final String RUTA_AGRUPACIONES = "data/agrupaciones.csv";

    /**
     * Guarda un movimiento financiero como una nueva línea en el archivo CSV
     * de movimientos. Si el directorio {@code data/} no existe, lo crea.
     *
     * @param mov el movimiento financiero a guardar
     * @throws RuntimeException si ocurre un error de I/O al escribir el archivo
     */
    public static void guardarMovimiento(Movimiento mov) {
        verificarCarpetaData();
        String linea = mov.getIdMovimiento() + ";" +
                mov.getIdAgrupacion() + ";" +
                mov.getTipoMovimiento() + ";" +
                mov.getMonto() + ";" +
                mov.getFechaMovimiento() + ";" +
                mov.getDescripcionMovimiento() + ";" +
                mov.getRutaComprobante() + ";" +
                mov.getIdUsuarioQueRegistra() + System.lineSeparator();
        try {
            Files.writeString(Paths.get(RUTA_MOVIMIENTOS), linea, StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.SYNC);
        } catch (IOException e) {
            throw new RuntimeException("Error crítico al guardar movimiento en CSV: " + e.getMessage());
        }
    }

    /**
     * Lee y filtra los movimientos del archivo CSV para una agrupación específica.
     * <p>
     * Retorna únicamente las líneas cuyo campo de agrupación (columna 1) coincida
     * con el ID proporcionado. Las filas incompletas (menos de 8 columnas) se ignoran.
     * </p>
     *
     * @param idAgrupacionBuscada el ID de la agrupación cuyos movimientos se desean leer
     * @return lista de arreglos de cadenas, donde cada arreglo representa una fila
     *         del CSV con los datos del movimiento
     * @throws RuntimeException si ocurre un error de I/O al leer el archivo
     */
    public static List<String[]> leerLineasMovimientos(String idAgrupacionBuscada) {
        List<String[]> lista = new ArrayList<>();
        File archivo = new File(RUTA_MOVIMIENTOS);

        if (!archivo.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 8 && datos[1].equals(idAgrupacionBuscada)) {
                    lista.add(datos);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer movimientos del CSV: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Guarda un usuario como una nueva línea en el archivo CSV de usuarios.
     * Si el directorio {@code data/} no existe, lo crea.
     *
     * @param user el usuario a guardar
     * @throws RuntimeException si ocurre un error de I/O al escribir el archivo
     */
    public static void guardarUsuario(Usuario user) {
        verificarCarpetaData();
        String linea = user.getIdUsuario() + ";" +
                user.getIdAgrupacion() + ";" +
                user.getNombre() + ";" +
                user.getContraseña() + ";" +
                user.getRol() + ";" +
                user.getMatricula() + System.lineSeparator();

        try {
            Files.writeString(Paths.get(RUTA_USUARIOS), linea, StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.SYNC);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar usuario en CSV: " + e.getMessage());
        }
    }

    /**
     * Carga todos los usuarios desde el archivo CSV de usuarios.
     * <p>
     * Las filas incompletas (menos de 6 columnas) se ignoran durante la lectura.
     * </p>
     *
     * @return lista de usuarios cargados desde el archivo, o lista vacía
     *         si el archivo no existe
     * @throws RuntimeException si ocurre un error de I/O al leer el archivo
     */
    public static List<Usuario> cargarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        File archivo = new File(RUTA_USUARIOS);

        if (!archivo.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] d = linea.split(";");
                if (d.length >= 6) {
                    Usuario u = new Usuario(d[1], d[2], d[3], RolUsuario.valueOf(d[4]), d[5]);
                    u.setIdUsuario(d[0]);
                    lista.add(u);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar usuarios del CSV: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Guarda una agrupación como una nueva línea en el archivo CSV de agrupaciones.
     * La lista de miembros se serializa como IDs separados por guiones ({@code -}).
     *
     * @param agrupacion la agrupación a guardar
     * @throws RuntimeException si ocurre un error de I/O al escribir el archivo
     */
    public static void guardarAgrupacion(Agrupacion agrupacion) {
        verificarCarpetaData();
        String linea = agrupacion.getIdAgrupacion() + ";" +
                agrupacion.getNombreAgrupacion() + ";" +
                agrupacion.getSaldoTotal() + ";" +
                String.join("-", agrupacion.getIdMiembros()) + System.lineSeparator();
        try {
            Files.writeString(Paths.get(RUTA_AGRUPACIONES), linea, StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.SYNC);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar agrupación en CSV: " + e.getMessage());
        }
    }

    /**
     * Carga todas las agrupaciones desde el archivo CSV de agrupaciones.
     * <p>
     * Las filas incompletas (menos de 3 columnas) se ignoran. Los IDs de
     * miembros se recuperan separando el cuarto campo por guiones ({@code -}).
     * </p>
     *
     * @return lista de agrupaciones cargadas desde el archivo, o lista vacía
     *         si el archivo no existe
     * @throws RuntimeException si ocurre un error de I/O al leer el archivo
     */
    public static List<Agrupacion> cargarAgrupaciones() {
        List<Agrupacion> lista = new ArrayList<>();
        File archivo = new File(RUTA_AGRUPACIONES);

        if (!archivo.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] d = linea.split(";");
                if (d.length < 3) continue;

                Agrupacion a = new Agrupacion(d[1]);
                a.setIdAgrupacion(d[0]);
                a.setSaldoTotal(Integer.parseInt(d[2]));

                if (d.length >= 4 && !d[3].isEmpty()) {
                    String[] miembros = d[3].split("-");
                    for (String idMiembro : miembros) {
                        a.agregarMiembro(idMiembro);
                    }
                }

                lista.add(a);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar agrupaciones del CSV: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Reescribe completamente el archivo CSV de agrupaciones con los datos
     * proporcionados. A diferencia de {@link #guardarAgrupacion(Agrupacion)},
     * este método sobrescribe todo el contenido existente.
     *
     * @param agrupaciones la lista de agrupaciones a escribir en el archivo
     * @throws RuntimeException si ocurre un error de I/O al escribir el archivo
     */
    public static void guardarTodasAgrupaciones(List<Agrupacion> agrupaciones) {
        verificarCarpetaData();
        StringBuilder contenido = new StringBuilder();

        for (Agrupacion a : agrupaciones) {
            contenido.append(a.getIdAgrupacion()).append(";")
                    .append(a.getNombreAgrupacion()).append(";")
                    .append(a.getSaldoTotal()).append(";")
                    .append(String.join("-", a.getIdMiembros()))
                    .append(System.lineSeparator());
        }

        try {
            Files.writeString(Paths.get(RUTA_AGRUPACIONES), contenido.toString(),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.SYNC);
        } catch (IOException e) {
            throw new RuntimeException("Error al reescribir agrupaciones en CSV: " + e.getMessage());
        }
    }

    /**
     * Reescribe completamente el archivo CSV de usuarios con los datos
     * proporcionados. A diferencia de {@link #guardarUsuario(Usuario)},
     * este método sobrescribe todo el contenido existente.
     *
     * @param usuarios la lista de usuarios a escribir en el archivo
     * @throws RuntimeException si ocurre un error de I/O al escribir el archivo
     */
    public static void guardarTodosUsuarios(List<Usuario> usuarios) {
        verificarCarpetaData();
        StringBuilder contenido = new StringBuilder();

        for (Usuario u : usuarios) {
            contenido.append(u.getIdUsuario()).append(";")
                    .append(u.getIdAgrupacion()).append(";")
                    .append(u.getNombre()).append(";")
                    .append(u.getContraseña()).append(";")
                    .append(u.getRol()).append(";")
                    .append(u.getMatricula())
                    .append(System.lineSeparator());
        }

        try {
            Files.writeString(Paths.get(RUTA_USUARIOS), contenido.toString(),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.SYNC);
        } catch (IOException e) {
            throw new RuntimeException("Error al reescribir usuarios en CSV: " + e.getMessage());
        }
    }

    /**
     * Verifica que el directorio {@code data/} exista. Si no existe, lo crea
     * junto con todos los directorios padre necesarios.
     */
    private static void verificarCarpetaData() {
        File directorio = new File("data");
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
    }
}
