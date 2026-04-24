package Vista;
import java.util.Scanner;

public class VistaConsola {
    private final Scanner in;

    public VistaConsola() {
        this.in = new Scanner(System.in);

    }
    public String mostrarMenuPrincipal() {
        System.out.println("\n---- Menu Principal - CLARA ----\n");
        System.out.println("1. Iniciar sesion");
        System.out.println("2. Salir");
        System.out.println("Elige una opcion:");
        return in.nextLine();
    }

    public String mostrarMenuAdministrador(String nombre) {
        System.out.println("\n---- Menu Administrador - CLARA ----\n");
        System.out.println("Bienvenido, " + nombre);
        System.out.println("-- Agrupaciones --");
        System.out.println("1. Crear agrupacion (pendiente)");
        System.out.println("-- Usuarios --");
        System.out.println("2. Registrar usuario");
        System.out.println("3. Editar datos (pendiente");
        System.out.println("-- Consultas --");
        System.out.println("4. Ver saldo de una agrupacion");
        System.out.println("5. Ver historial de movimientos");
        System.out.println("-- Sesion --");
        System.out.println("6. Cerrar sesion");
        System.out.println("Elige una opcion:");
        return in.nextLine();
    }

    public String mostrarMenuTesorero(String nombre) {
        System.out.println("\n---- Menu Tesorero - CLARA ----\n");
        System.out.println("Bienvenido, " + nombre);
        System.out.println("-- Movimientos --");
        System.out.println("1. Registrar ingreso");
        System.out.println("2. Registrar egreso");
        System.out.println("-- Consultas --");
        System.out.println("3. Ver saldo actual");
        System.out.println("4. Ver historial de movimientos");
        System.out.println("-- Sesion --");
        System.out.println("5. Cerrar sesion");
        System.out.print("Elige una opcion: ");
        return in.nextLine();
    }

    public String mostrarMenuSocio(String nombre) {
        System.out.println("\n---- Menu Socio - CLARA ----\n");
        System.out.println("Bienvenido, " + nombre);
        System.out.println("-- Consultas --");
        System.out.println("1. Ver saldo actual de mi agrupacion");
        System.out.println("2. Ver historial de movimientos de mi agrupacion");
        System.out.println("-- Sesion --");
        System.out.println("3. Cerrar sesion");
        System.out.print("Elige una opcion: ");
        return in.nextLine();
    }
    public String[] pedirDatosLogin() {
        System.out.println("--- Iniciar Sesion ---");
        System.out.println("Ingresa tu matricula:");
        String matricula = in.nextLine();
        System.out.println("Ingresa tu contraseña:");
        String contrasena = in.nextLine();
        return new String[]{matricula, contrasena};
    }

    public String[] pedirDatosNuevoUsuario() {
        System.out.println("\n--- Registrar Nuevo Usuario ---");
        System.out.println("Ingresa la matricula:");
        String matricula = in.nextLine();
        System.out.println("Ingresa el nombre:");
        String nombre = in.nextLine();
        System.out.println("Ingresa la contraseña:");
        String contrasena = in.nextLine();
        System.out.println("Ingresa el rol (1=tesorero, 2=socio):");
        String opcionRol = in.nextLine();
        return new String[]{matricula, nombre, contrasena, opcionRol};
    }

    public int pedirMonto() {
        System.out.println("Ingrese el monto:");
        try {
            return Integer.parseInt(in.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese un número válido.");
            return 0;
        }
    }

    public String pedirDescripcion() {
        System.out.println("Ingrese una descripcion del movimiento:");
        return in.nextLine();
    }

    public void cerrar() {
        in.close();
    }
}