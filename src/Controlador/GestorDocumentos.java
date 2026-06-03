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

    public GestorDocumentos() {
        verificarCarpetaComprobantes();
    }

    private void verificarCarpetaComprobantes() {
        File directorio = new File(RUTA_COMPROBANTES);
        if (!directorio.exists()) {
            directorio.mkdir();
        }
    }

    public String guardarComprobanteLocal(String rutaOrigen) {
        if (rutaOrigen == null || rutaOrigen.isEmpty()) {
            return "sincomprobante.jpg";
        }

        File archivoOrigen = new File(rutaOrigen);
        if (!archivoOrigen.exists()) {
            System.out.println("Error: El archivo original no existe.");
            return "sincomprobante.jpg";
        }

        try {
            String nombreOriginal = archivoOrigen.getName();
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
            System.out.println("Error al copiar el comprobante " + e.getMessage());
            return "sincomprobante.jpg";
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