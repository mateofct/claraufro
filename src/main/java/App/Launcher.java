package App;

import Controlador.*;

/**
 * Punto de entrada principal de la aplicación CLARA (Gestión de Agrupaciones).
 * <p>
 * Esta clase actúa como el punto de arranque del sistema y se encarga de
 * inicializar todas las dependencias necesarias: la fuente de datos de matrículas,
 * los controladores de lógica de negocio y el orquestador principal.
 * Una vez configurado el entorno, delega el control al {@link Controlador.ControladorPrincipal}.
 * </p>
 *
 * @author CLARA Team
 * @version 1.0
 */
public class Launcher {

    /**
     * Método principal de la aplicación.
     * <p>
     * El flujo de inicialización es el siguiente:
     * <ol>
     *   <li>Crea una instancia de {@link Controlador.GestorEstudiantesUniversidad}
     *       como fuente de datos de matrículas.</li>
     *   <li>Instancia los controladores {@link Controlador.ControladorUsuario},
     *       {@link Controlador.ControladorFinanzas} y
     *       {@link Controlador.ControladorAgrupacion}.</li>
     *   <li>Crea el orquestador {@link Controlador.ControladorPrincipal} con los
     *       controladores anteriores.</li>
     *   <li>Inicia la aplicación llamando a {@code iniciar()} del controlador principal.</li>
     * </ol>
     * </p>
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
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
