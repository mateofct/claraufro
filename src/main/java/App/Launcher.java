package App;

import Controlador.ControladorUsuario;
import Controlador.ControladorFinanzas;
import Controlador.ControladorAgrupacion;
import Controlador.GestorMatriculas;
import Controlador.IFuenteMatriculas;
import Vista.VentanaIniciarSesion;

public class Launcher {
    public static void main(String[] args) {
        IFuenteMatriculas fuenteMatriculas = new GestorMatriculas();
        ControladorUsuario controladorUsuario   = new ControladorUsuario(fuenteMatriculas); // Para poder cambiarlo si se puede, a google sheets
        ControladorFinanzas controladorFinanzas = new ControladorFinanzas();
        ControladorAgrupacion controladorAgrupacion = new ControladorAgrupacion(controladorUsuario);
        new VentanaIniciarSesion(controladorUsuario, controladorFinanzas, controladorAgrupacion);
    }
}