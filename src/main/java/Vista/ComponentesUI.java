package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Clase utilitaria que centraliza la creación y configuración de componentes
 * de interfaz gráfica para todas las ventanas del sistema.
 * <p>
 * Define constantes visuales (colores y fuentes) compartidas por el diseño
 * uniforme de la aplicación, y proporciona métodos de fábrica para crear
 * componentes Swing con estilos preconfigurados. Esto garantiza consistencia
 * visual en toda la aplicación y facilita el mantenimiento del diseño.
 * </p>
 *
 * <h3>Constantes visuales:</h3>
 * <table border="1" cellpadding="4" cellspacing="0">
 *   <caption>Constantes de estilo</caption>
 *   <tr><th>Nombre</th><th>Descripción</th></tr>
 *   <tr><td>{@code COLOR_FONDO}</td><td>Color de fondo principal (azul corporativo)</td></tr>
 *   <tr><td>{@code FUENTE_TITULO}</td><td>Fuente Arial Bold 28pt para títulos</td></tr>
 *   <tr><td>{@code FUENTE_SUBTITULO}</td><td>Fuente Arial Bold 22pt para subtítulos</td></tr>
 *   <tr><td>{@code FUENTE_ETIQUETA}</td><td>Fuente Arial Bold 15pt para etiquetas</td></tr>
 *   <tr><td>{@code FUENTE_BOTON}</td><td>Fuente Arial Bold 14pt para botones</td></tr>
 *   <tr><td>{@code COLOR_TEXTO_ETIQUETA}</td><td>Color blanco para texto de etiquetas</td></tr>
 * </table>
 *
 * @author CLARA Team
 * @version 1.0
 */
public class ComponentesUI {

    /**
     * Color de fondo principal utilizado en todas las ventanas (azul corporativo).
     */
    public static final Color COLOR_FONDO = new Color(102, 133, 183);

    /**
     * Fuente para los títulos principales: Arial, negrita, 28 puntos.
     */
    public static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 28);

    /**
     * Fuente para los subtítulos: Arial, negrita, 22 puntos.
     */
    public static final Font FUENTE_SUBTITULO = new Font("Arial", Font.BOLD, 22);

    /**
     * Fuente para las etiquetas de formulario: Arial, negrita, 15 puntos.
     */
    public static final Font FUENTE_ETIQUETA = new Font("Arial", Font.BOLD, 15);

    /**
     * Fuente para los botones: Arial, negrita, 14 puntos.
     */
    public static final Font FUENTE_BOTON = new Font("Arial", Font.BOLD, 14);

    /**
     * Color del texto de las etiquetas (blanco).
     */
    public static Color COLOR_TEXTO_ETIQUETA = Color.WHITE;

    /**
     * Crea un componente {@link JLabel} configurado como título principal.
     * El texto se centra horizontalmente, utiliza la fuente de título y
     * el color blanco.
     *
     * @param texto el texto del título
     * @return el {@code JLabel} configurado como título
     */
    public static JLabel crearTitulo(String texto) {
        JLabel titulo = new JLabel(texto,  JLabel.CENTER);
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(Color.WHITE);
        return titulo;
    }

    /**
     * Crea un componente {@link JLabel} configurado como subtítulo.
     * El texto se centra horizontalmente, utiliza la fuente de subtítulo
     * y el color blanco.
     *
     * @param texto el texto del subtítulo
     * @return el {@code JLabel} configurado como subtítulo
     */
    public static JLabel crearSubtitulo(String texto) {
        JLabel subtitulo = new JLabel(texto,  JLabel.CENTER);
        subtitulo.setFont(FUENTE_SUBTITULO);
        subtitulo.setForeground(Color.WHITE);
        return subtitulo;
    }

    /**
     * Crea un componente {@link JLabel} configurado como etiqueta de formulario.
     * Utiliza la fuente de etiqueta y fondo blanco.
     *
     * @param texto el texto de la etiqueta
     * @return el {@code JLabel} configurado como etiqueta
     */
    public static JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(FUENTE_ETIQUETA);
        etiqueta.setBackground(Color.WHITE);
        return etiqueta;
    }

    /**
     * Crea un botón estándar con fondo blanco, texto negro y el listener
     * de acción proporcionado.
     *
     * @param texto el texto que se muestra en el botón
     * @param accion el {@code ActionListener} que se ejecuta al presionar el botón
     * @return el {@code JButton} configurado
     */
    public static JButton crearBoton(String texto, ActionListener accion) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE_BOTON);
        boton.setBackground(Color.WHITE);
        boton.setForeground(Color.BLACK);
        boton.addActionListener(accion);
        return boton;
    }

    /**
     * Crea un botón de peligro con fondo rojo, texto blanco y el listener
     * de acción proporcionado. Se utiliza para acciones destructivas como
     * eliminación o cierre de sesión.
     *
     * @param texto el texto que se muestra en el botón
     * @param accion el {@code ActionListener} que se ejecuta al presionar el botón
     * @return el {@code JButton} configurado como botón de peligro
     */
    public static JButton crearBotonPeligro(String texto, ActionListener accion) {
        JButton boton = crearBoton(texto, accion);
        boton.setBackground(Color.RED);
        boton.setForeground(Color.WHITE);
        return boton;
    }

    /**
     * Crea un campo de texto estándar con fuente Arial 14pt y un borde
     * compuesto (línea gris de 1px con espaciado interior de 5px).
     *
     * @return el {@code JTextField} configurado
     */
    public static JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return campo;
    }

    /**
     * Crea un campo de contraseña con fuente Arial 14pt y un borde
     * compuesto (línea gris de 1px con espaciado interior de 8x10px).
     *
     * @return el {@code JPasswordField} configurado
     */
    public static JPasswordField crearCampoContrasena() {
        JPasswordField campo = new JPasswordField();
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return campo;
    }

    /**
     * Crea un panel transparente sin fondo.
     *
     * @return el {@code JPanel} con fondo transparente
     */
    public static JPanel crearPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        return panel;
    }

    /**
     * Configura el color de fondo del contenido de un {@link JFrame}
     * con el color corporativo definido en {@link #COLOR_FONDO}.
     *
     * @param frame el marco de ventana cuyo fondo se desea configurar
     */
    public static void configurarFondo(JFrame frame) {
        frame.getContentPane().setBackground(COLOR_FONDO);
    }
}