package Main;

import Controlador.ControladorUsuario;
import Controlador.ControladorFinanzas;
import Vista.VentanaIniciarSesion;

public class Main {
    public static void main(String[] args) {
        ControladorUsuario controladorUsuario = new ControladorUsuario();
        ControladorFinanzas controladorFinanzas = new ControladorFinanzas();

        VentanaIniciarSesion ventanaLogin = new VentanaIniciarSesion(controladorUsuario, controladorFinanzas);
        ventanaLogin.setVisible(true);
    }
}