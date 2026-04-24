package Controlador;

import Modelo.Movimiento;
import Modelo.Usuario;
import Modelo.RolUsuario;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorArchivosCSV {
    private static final String RUTA_MOVIMIENTOS = "data/movimientos_clara.csv";
    private static final String RUTA_USUARIOS = "data/usuarios.csv";

    public static void guardarMovimiento(Movimiento mov) {
        verificarCarpetaData();
        try (FileWriter fw = new FileWriter(RUTA_MOVIMIENTOS, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            String linea = mov.getIdMovimiento() + "," +
                    mov.getIdAgrupacion() + "," +
                    mov.getTipoMovimiento() + "," +
                    mov.getMonto() + "," +
                    mov.getFechaMovimiento() + "," +
                    mov.getDescripcionMovimiento() + "," +
                    mov.getRutaComprobante() + "," +
                    mov.getIdUsuarioQueRegistra();

            out.println(linea);

        } catch (IOException e) {
            System.out.println("Error al guardar CSV: " + e.getMessage());
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
                String[] datos = linea.split(",");
                if (datos.length >= 8 && datos[1].equals(idAgrupacionBuscada)) {
                    lista.add(datos);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer CSV: " + e.getMessage());
        }
        return lista;
    }

    public static void guardarUsuario(Usuario user) {
        verificarCarpetaData();
        try (FileWriter fw = new FileWriter(RUTA_USUARIOS, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            String linea = user.getIdUsuario() + "," +
                    user.getIdAgrupacion() + "," +
                    user.getNombre() + "," +
                    user.getContraseña() + "," +
                    user.getRol() + "," +
                    user.getMatricula();

            out.println(linea);

        } catch (IOException e) {
            System.out.println("Error al guardar usuario: " + e.getMessage());
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
                String[] d = linea.split(",");
                if (d.length >= 6) {
                    Usuario u = new Usuario(d[1], d[2], d[3], RolUsuario.valueOf(d[4]), d[5]);
                    lista.add(u);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar usuarios: " + e.getMessage());
        }
        return lista;
    }

    private static void verificarCarpetaData() {
        File directorio = new File("data");
        if (!directorio.exists()) {
            directorio.mkdir();
        }
    }
}