package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ComponentesUI {
    // COLORES
    public static final Color COLOR_FONDO = new Color(102, 133, 183);
    // Fuente que usamos en todas las ventanas

    public static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 28);
    public static final Font FUENTE_SUBTITULO = new Font("Arial", Font.BOLD, 22);
    public static final Font FUENTE_ETIQUETA = new Font("Arial", Font.BOLD, 15);
    public static final Font FUENTE_BOTON = new Font("Arial", Font.BOLD, 14);
    public static Color COLOR_TEXTO_ETIQUETA = Color.WHITE;

    public static JLabel crearTitulo(String texto) {
        JLabel titulo = new JLabel(texto,  JLabel.CENTER);
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(Color.WHITE);
        return titulo;
    }

    public static JLabel crearSubtitulo(String texto) {
        JLabel subtitulo = new JLabel(texto,  JLabel.CENTER);
        subtitulo.setFont(FUENTE_SUBTITULO);
        subtitulo.setForeground(Color.WHITE);
        return subtitulo;
    }

    public static JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(FUENTE_ETIQUETA);
        etiqueta.setBackground(Color.WHITE);
        return etiqueta;
    }

    public static JButton crearBoton(String texto, ActionListener accion) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE_BOTON);
        boton.setBackground(Color.WHITE);
        boton.setForeground(Color.BLACK);
        boton.addActionListener(accion);
        return boton;
    }

    public static JButton crearBotonPeligro(String texto, ActionListener accion) {
        JButton boton = crearBoton(texto, accion);
        boton.setBackground(Color.RED);
        boton.setForeground(Color.WHITE);
        return boton;
    }

    public static JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return campo;
    }

    public static JPasswordField crearCampoContrasena() {
        JPasswordField campo = new JPasswordField();
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return campo;
    }

    public static JPanel crearPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        return panel;
    }

    public static void configurarFondo(JFrame frame) {
        frame.getContentPane().setBackground(COLOR_FONDO);
    } // es para el fondo que usualmente usamos.
}
