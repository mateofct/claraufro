package Main;

import Vista.VistaConsola;
import Controlador.ControladorUsuario;
import Modelo.Usuario;
import Modelo.RolUsuario;

public class Main {
    public static void main(String[] args) {
        VistaConsola vista = new VistaConsola();
        ControladorUsuario controladorUsuario = new ControladorUsuario();
        boolean corriendo = true;

        while (corriendo) {
            String opcion = vista.mostrarMenuPrincipal();

            switch (opcion) {
                case "1":
                    String[] datosLogin = vista.pedirDatosLogin();
                    boolean loginExitoso = controladorUsuario.iniciarSesion(datosLogin[0], datosLogin[1]);
                    if (loginExitoso) {
                        corriendo = manejarSesion(vista, controladorUsuario);
                    } else {
                        System.out.println("Matricula o contraseña incorrecta. Intenta de nuevo.");
                    }
                    break;
                case "2":
                    System.out.println("Saliendo del programa...");
                    corriendo = false;
                    break;
                default:
                    System.out.println("Opcion no valida. Intenta de nuevo.");
            }
        }
        vista.cerrar();
    }

    //TODO
    private static boolean manejarMenuAdmin(String opcion, ControladorUsuario controladorUsuario) {
        switch (opcion) {
            case "6":
                controladorUsuario.cerrarSesion();
                System.out.println("Hasta luego...");
                return false;
            default:
                System.out.println("Opcion no implementada aun. Elige otra.");
                return true;
        }
    }
    //TODO
    private static boolean manejarMenuTesorero(String opcion, ControladorUsuario controladorUsuario) {
        switch (opcion) {
            case "5":
                controladorUsuario.cerrarSesion();
                System.out.println("Hasta luego...");
                return false;
            default:
                System.out.println("Opcion no implementada aun. Elige otra.");
                return true;
        }
    }
    //TODO
    private static boolean manejarMenuSocio(String opcion, ControladorUsuario controladorUsuario) {
        switch (opcion) {
            case "3":
                controladorUsuario.cerrarSesion();
                System.out.println("Hasta luego...");
                return false;
            default:
                System.out.println("Opcion no implementada aun. Elige otra.");
                return true;
        }
    }

    // Esto es para manejar el menu según el usuario que ingresó, enSesion es para que se inicie, false si termina.
    private static boolean manejarSesion(VistaConsola vista, ControladorUsuario controladorUsuario) {
        Usuario activo = controladorUsuario.getUsuarioActivo();
        boolean enSesion = true;

        while (enSesion) {
            String opcion;

            switch (activo.getRol()) {
                case ADMIN:
                    opcion = vista.mostrarMenuAdministrador(activo.getNombre());
                    enSesion = manejarMenuAdmin(opcion, controladorUsuario);
                    break;
                case TESORERO:
                    opcion = vista.mostrarMenuTesorero(activo.getNombre());
                    enSesion = manejarMenuTesorero(opcion, controladorUsuario);
                    break;
                case SOCIO:
                    opcion = vista.mostrarMenuSocio(activo.getNombre());
                    enSesion = manejarMenuSocio(opcion, controladorUsuario);
                    break;

                default:
                    System.out.println("Rol no reconocido. Cerrando sesion.");
                    enSesion = false;
            }
        }
        return true;
    }

}
