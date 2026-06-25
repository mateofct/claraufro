package Controlador;

import Modelo.Movimiento;
import Modelo.Usuario;
import Modelo.RolUsuario;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class GestorArchivosCSV {
    private static final String RUTA_MOVIMIENTOS = "data/movimientos_clara.csv";
    private static final String RUTA_USUARIOS = "data/usuarios.csv";
    private static final String RUTA_AGRUPACIONES = "data/agrupaciones.csv";


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

    private static void verificarCarpetaData() {
        File directorio = new File("data");
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
    }
}