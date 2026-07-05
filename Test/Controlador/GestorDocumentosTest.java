package Controlador;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;

//Estas pruebas crean archivos de prueba temporales en el sistema
// (usando File.createTempFile, que Java borra despues de usarlos con
// deleteOnExit()), pero SI copian algunos de esos archivos hacia la
// carpeta real data/comprobantes del proyecto, como hace la app de verdad

public class GestorDocumentosTest {

    @Test
    void testSinComprobanteNombrePorDefecto() {
        GestorDocumentos gestor = new GestorDocumentos();

        String resultado = gestor.guardarComprobanteLocal("");

        assertEquals("sincomprobante.jpg", resultado);
    }

    @Test
    void testArchivoQueNoExisteLanzaExcepcion() {
        GestorDocumentos gestor = new GestorDocumentos();

        assertThrows(IllegalArgumentException.class, () -> {
            gestor.guardarComprobanteLocal("archivo/que/no/existe.pdf");
        });
    }

    @Test
    void testExtensionNoPermitidaLanzaExcepcion() throws IOException {
        GestorDocumentos gestor = new GestorDocumentos();

        // creamos un archivo de prueba con una extension que no deberia aceptarse
        File archivoDePrueba = File.createTempFile("comprobante_prueba", ".exe");
        archivoDePrueba.deleteOnExit();
        Files.writeString(archivoDePrueba.toPath(), "contenido de prueba");

        assertThrows(IllegalArgumentException.class, () -> {
            gestor.guardarComprobanteLocal(archivoDePrueba.getAbsolutePath());
        });
    }

    @Test
    void testArchivoDemasiadoPesadoLanzaExcepcion() throws IOException {
        GestorDocumentos gestor = new GestorDocumentos();

        // creamos un archivo .jpg de 6MB, mas del limite permitido (5MB)
        File archivoPesado = File.createTempFile("comprobante_pesado", ".jpg");
        archivoPesado.deleteOnExit();
        try (RandomAccessFile archivoTemporal = new RandomAccessFile(archivoPesado, "rw")) {
            archivoTemporal.setLength(6L * 1024 * 1024);
        }

        assertThrows(IllegalArgumentException.class, () -> {
            gestor.guardarComprobanteLocal(archivoPesado.getAbsolutePath());
        });
    }

    @Test
    void testGuardarUnComprobanteValidoFunciona() throws IOException {
        GestorDocumentos gestor = new GestorDocumentos();

        File archivoValido = File.createTempFile("comprobante_valido", ".jpg");
        archivoValido.deleteOnExit();
        Files.writeString(archivoValido.toPath(), "contenido de un comprobante valido");

        String nombreGuardado = gestor.guardarComprobanteLocal(archivoValido.getAbsolutePath());

        assertNotNull(nombreGuardado);
        assertTrue(nombreGuardado.endsWith(".jpg"));

        // el archivo deberia haberse copiado a la carpeta real de comprobantes
        File archivoCopiado = new File("data/comprobantes/" + nombreGuardado);
        assertTrue(archivoCopiado.exists());
    }

    @Test
    void testLaCarpetaDeComprobantesExisteDespuesDeCrearElGestor() {
        // No se borra la carpeta real para no perder comprobantes que ya
        // existen, asi comprobamos quesin importar el estado inicial,
        // despues de crear el GestorDocumentos la carpeta siempre existe
        // y en caso de el constructor la crea sola si no esta
        new GestorDocumentos();

        File carpetaComprobantes = new File("data/comprobantes");
        assertTrue(carpetaComprobantes.exists());
    }


}
