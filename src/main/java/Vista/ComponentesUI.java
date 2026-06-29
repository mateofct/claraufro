package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ComponentesUI {
    // COLORES
    public static final Color COLOR_FONDO = new Color(102, 133, 183);
    public static final Color COLOR_TEXTO_BOTON = new Color(50, 50, 50);
    // Fuente que usamos en todas las ventanas
    public static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 24);
    public static final Font FUENTE_ETIQUETA = new Font("Arial", Font.BOLD, 15);
    public static final Font FUENTE_BOTON = new Font("Arial", Font.BOLD, 14);

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
        boton.setForeground(COLOR_TEXTO_BOTON);
        boton.addActionListener(accion);
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
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return campo;
    }
    public static void configurarFondo(JFrame frame) {
        frame.getContentPane().setBackground(COLOR_FONDO);
    }
}
