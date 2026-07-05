package App;

import Controlador.*;

/**
 * Punto de entrada de la aplicación.
 * Configura las dependencias iniciales e inicia el Controlador Principal.
 */
public class Launcher {
    public static void main(String[] args) {
        // 1. Inicializar la fuente de datos
        IFuenteMatriculas fuenteMatriculas = new GestorEstudiantesUniversidad();

        // 2. Inicializar los controladores de lógica de negocio
        ControladorUsuario controladorUsuario = new ControladorUsuario(fuenteMatriculas);
        ControladorFinanzas controladorFinanzas = new ControladorFinanzas();
        ControladorAgrupacion controladorAgrupacion = new ControladorAgrupacion(controladorUsuario);

        // 3. Inicializar el Controlador Principal (Orquestador)
        ControladorPrincipal controladorPrincipal = new ControladorPrincipal(
                controladorUsuario,
                controladorFinanzas,
                controladorAgrupacion
        );

        // 4. Iniciar la aplicación
        controladorPrincipal.iniciar();
    }
}