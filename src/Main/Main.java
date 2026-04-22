package Main;

import Vista.VistaConsola;

public class Main {
    public static void main(String[] args) {
        VistaConsola vista = new VistaConsola();
        boolean corriendo = true;

        while (corriendo) {
            String opcion = vista.mostrarMenuPrincipal();

            switch (opcion) {
                case "1":
                    String[] datosLogin = vista.pedirDatosLogin();
                    System.out.println("Login recibido para: " + datosLogin[0]);
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
}
