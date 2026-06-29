package Controlador;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


public class GestorDocumentos {
    private static final String RUTA_COMPROBANTES = "data/comprobantes";
    private static final long TAMAÑO_MAXIMO = 5 * 1024 * 1024;

    public GestorDocumentos() {
        verificarCarpetaComprobantes();
    }

    private void verificarCarpetaComprobantes() {
        File directorio = new File(RUTA_COMPROBANTES);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
    }

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

    public void abrirComprobante(String nombreArchivo) {
        if (nombreArchivo.equals("sincomprobante.jpg") || nombreArchivo.equals("comprobante_default.jpg")) {
            System.out.println("El movimiento no tiene un comprobante asociado.");
            return;
        }

        File archivo = new File(RUTA_COMPROBANTES + "/" + nombreArchivo);
        if (archivo.exists()) {
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(archivo);
                } else {
                    System.out.println("El SO no soporta java.awt.Desktop.");
                }
            } catch (IOException e) {
                System.out.println("Error al abrir el archivo: " + e.getMessage());
            }
        } else {
            System.out.println("El comprobante ya no se encuentra en la carpeta.");
        }
    }
}