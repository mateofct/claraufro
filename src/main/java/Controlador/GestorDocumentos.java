package Controlador;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Gestor de comprobantes financieros que se encarga de almacenar, validar y abrir
 * los archivos de comprobante asociados a los movimientos de la agrupación.
 * <p>
 * Los comprobantes se almacenan en el directorio {@code data/comprobantes/}
 * con nombres generados mediante UUID para evitar colisiones. Se aceptan
 * únicamente archivos con extensión PDF, JPG, JPEG o PNG, con un tamaño
 * máximo de 5 MB.
 * </p>
 *
 * @author CLARA Team
 * @version 1.0
 */
public class GestorDocumentos {

    /**
     * Ruta del directorio donde se almacenan los archivos de comprobantes.
     */
    private static final String RUTA_COMPROBANTES = "data/comprobantes";

    /**
     * Tamaño máximo permitido para un archivo de comprobante (5 MB en bytes).
     */
    private static final long TAMAÑO_MAXIMO = 5 * 1024 * 1024;

    /**
     * Constructor que inicializa el gestor verificando y creando el directorio
     * de comprobantes si no existe.
     */
    public GestorDocumentos() {
        verificarCarpetaComprobantes();
    }

    /**
     * Verifica que el directorio de comprobantes exista. Si no existe, lo crea
     * junto con todos los directorios padre necesarios.
     */
    private void verificarCarpetaComprobantes() {
        File directorio = new File(RUTA_COMPROBANTES);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
    }

    /**
     * Guarda un archivo de comprobante en el directorio de comprobantes con un
     * nombre único basado en UUID.
     * <p>
     * Si la ruta de origen es {@code null} o está vacía, retorna el nombre
     * predeterminado {@code "sincomprobante.jpg"}. El método valida que el
     * archivo exista, no exceda los 5 MB y tenga una extensión permitida
     * (PDF, JPG, JPEG o PNG).
     * </p>
     *
     * @param rutaOrigen la ruta absoluta del archivo de comprobante a guardar
     * @return el nombre del archivo guardado (con extensión original),
     *         o {@code "sincomprobante.jpg"} si no se proporciona ruta
     * @throws IllegalArgumentException si el archivo no existe, excede los 5 MB
     *                                  o tiene una extensión no permitida
     * @throws RuntimeException si ocurre un error al copiar el archivo
     */
    public String guardarComprobanteLocal(String rutaOrigen) {
        if (rutaOrigen == null || rutaOrigen.isEmpty()) {
            return "sincomprobante.jpg";
        }

        File archivoOrigen = new File(rutaOrigen);
        if (!archivoOrigen.exists()) {
            throw new IllegalArgumentException("Error: El archivo original no existe.");
        }

        if (archivoOrigen.length() > TAMAÑO_MAXIMO) {
            throw new IllegalArgumentException("El archivo es muy pesado, el límite es de 5MB");
        }

        String nombreOriginal = archivoOrigen.getName().toLowerCase();
        if (!nombreOriginal.endsWith(".pdf") && !nombreOriginal.endsWith(".jpg") && !nombreOriginal.endsWith(".jpeg") && !nombreOriginal.endsWith(".png")) {
            throw new IllegalArgumentException("Formato no permitido. Solo se acepta PDF, JPG, JPEG o PNG.");
        }
        try {
            String extension = "";
            int i = nombreOriginal.lastIndexOf('.');
            if (i > 0) {
                extension = nombreOriginal.substring(i);
            }

            String nuevoNombre = UUID.randomUUID().toString() + extension;
            Path destino = Paths.get(RUTA_COMPROBANTES, nuevoNombre);
            Files.copy(archivoOrigen.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
            return nuevoNombre;

        } catch (IOException e) {
            throw new RuntimeException("Error al copiar comprobante: " + e.getMessage());
        }
    }

    /**
     * Abre un archivo de comprobante utilizando la aplicación asociada del sistema
     * operativo a través de {@link java.awt.Desktop}.
     * <p>
     * Rechaza los archivos con nombre predeterminado ({@code "sincomprobante.jpg"}
     * o {@code "comprobante_default.jpg"}) indicando que el movimiento no tiene
     * comprobante asociado.
     * </p>
     *
     * @param nombreArchivo el nombre del archivo de comprobante a abrir
     * @throws IllegalArgumentException si el archivo es un comprobante por defecto,
     *         no se encuentra en la carpeta, el SO no soporta Desktop, o ocurre
     *         un error al abrirlo
     */
    public void abrirComprobante(String nombreArchivo) {
        if (nombreArchivo.equals("sincomprobante.jpg") || nombreArchivo.equals("comprobante_default.jpg")) {
            throw  new IllegalArgumentException("El movimiento no tiene un comprobante asociado.");
        }

        File archivo = new File(RUTA_COMPROBANTES + "/" + nombreArchivo);
        if (archivo.exists()) {
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(archivo);
                } else {
                    throw  new IllegalArgumentException("El SO no soporta java.awt.Desktop.");
                }
            } catch (IOException e) {
                throw  new IllegalArgumentException("Error al abrir el archivo: " + e.getMessage());
            }
        } else {
            throw  new IllegalArgumentException("El comprobante ya no se encuentra en la carpeta.");
        }
    }
}
