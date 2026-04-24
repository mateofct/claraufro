package Main;

import Vista.VistaConsola;
import Controlador.ControladorUsuario;
import Controlador.ControladorFinanzas;
import Modelo.Usuario;
import Modelo.TipoMovimiento;
import Modelo.RolUsuario;

public class Main {
    public static void main(String[] args) {
        VistaConsola vista = new VistaConsola();
        ControladorUsuario controladorUsuario = new ControladorUsuario();
        ControladorFinanzas controladorFinanzas = new ControladorFinanzas();
        boolean corriendo = true;

        while (corriendo) {
            String opcion = vista.mostrarMenuPrincipal();

            switch (opcion) {
                case "1":
                    String[] datosLogin = vista.pedirDatosLogin();
                    boolean loginExitoso = controladorUsuario.iniciarSesion(datosLogin[0], datosLogin[1]);
                    if (loginExitoso) {
                        manejarSesion(vista, controladorUsuario, controladorFinanzas);
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

    private static boolean manejarMenuAdmin(String opcion, VistaConsola vista, ControladorUsuario controladorUsuario, ControladorFinanzas controladorFinanzas) {
        Usuario activo = controladorUsuario.getUsuarioActivo();
        switch (opcion) {
            case "2":
                String[] datos = vista.pedirDatosNuevoUsuario();
                RolUsuario rolElegido;
                if (datos[3].equals("1")) {
                    rolElegido = RolUsuario.TESORERO;
                } else if (datos[3].equals("2")) {
                    rolElegido = RolUsuario.SOCIO;
                } else {
                    System.out.println("Opcion no valida. Intenta de nuevo.");
                    return true;
                }
                controladorUsuario.registrarUsuario("agrup-001", datos[1], datos[2], rolElegido, datos[0]);
                return true;
            case "6":
                controladorUsuario.cerrarSesion();
                System.out.println("Hasta luego...");
                return false;
            case "4":
                System.out.println("\nSaldo actual de la agrupacion: $" + controladorFinanzas.calcularSaldo(activo.getIdAgrupacion()));
                return true;
            case "5":
                controladorFinanzas.mostrarHistorial(activo.getIdAgrupacion());
                return true;
            default:
                System.out.println("Opcion no implementada aun. Elige otra.");
                return true;
        }
    }

    private static boolean manejarMenuTesorero(String opcion, VistaConsola vista, ControladorUsuario controladorUsuario, ControladorFinanzas controladorFinanzas) {
        Usuario activo = controladorUsuario.getUsuarioActivo();
        switch (opcion) {
            case "1":
                int montoI = vista.pedirMonto();
                String descI = vista.pedirDescripcion();
                controladorFinanzas.registrarMovimiento(activo.getIdAgrupacion(), TipoMovimiento.INGRESO, montoI, descI, "comprobante_default.jpg", activo.getIdUsuario());
                return true;
            case "2":
                int montoE = vista.pedirMonto();
                String descE = vista.pedirDescripcion();
                controladorFinanzas.registrarMovimiento(activo.getIdAgrupacion(), TipoMovimiento.EGRESO, montoE, descE, "comprobante_default.jpg", activo.getIdUsuario());
                return true;
            case "3":
                System.out.println("\nSaldo actual: $" + controladorFinanzas.calcularSaldo(activo.getIdAgrupacion()));
                return true;
            case "4":
                controladorFinanzas.mostrarHistorial(activo.getIdAgrupacion());
                return true;
            case "5":
                controladorUsuario.cerrarSesion();
                System.out.println("Hasta luego...");
                return false;
            default:
                System.out.println("Opcion no valida.");
                return true;
        }
    }

    private static boolean manejarMenuSocio(String opcion, ControladorUsuario controladorUsuario, ControladorFinanzas controladorFinanzas) {
        Usuario activo = controladorUsuario.getUsuarioActivo();
        switch (opcion) {
            case "1":
                System.out.println("\nSaldo actual: $" + controladorFinanzas.calcularSaldo(activo.getIdAgrupacion()));
                return true;
            case "2":
                controladorFinanzas.mostrarHistorial(activo.getIdAgrupacion());
                return true;
            case "3":
                controladorUsuario.cerrarSesion();
                System.out.println("Hasta luego...");
                return false;
            default:
                System.out.println("Opcion no valida.");
                return true;
        }
    }

    private static void manejarSesion(VistaConsola vista, ControladorUsuario controladorUsuario, ControladorFinanzas controladorFinanzas) {
        boolean enSesion = true;

        while (enSesion) {
            Usuario activo = controladorUsuario.getUsuarioActivo();
            String opcion;

            switch (activo.getRol()) {
                case ADMIN:
                    opcion = vista.mostrarMenuAdministrador(activo.getNombre());
                    enSesion = manejarMenuAdmin(opcion, vista, controladorUsuario, controladorFinanzas);
                    break;
                case TESORERO:
                    opcion = vista.mostrarMenuTesorero(activo.getNombre());
                    enSesion = manejarMenuTesorero(opcion, vista, controladorUsuario, controladorFinanzas);
                    break;
                case SOCIO:
                    opcion = vista.mostrarMenuSocio(activo.getNombre());
                    enSesion = manejarMenuSocio(opcion, controladorUsuario, controladorFinanzas);
                    break;
                default:
                    enSesion = false;
            }
        }
    }
}