package Vista;
import javax.swing.*;
import java.awt.*;

public class VentanaIniciarSesion {

    private final JFrame frame = new JFrame();
    private JTextField EscribirMatricula;
    private JPasswordField EscribirContraseña;
    private JButton btnIngresar;


    public VentanaIniciarSesion(){

    }

    private void UISension() {
        frame.setTitle("Iniciar Sesión - CLARA");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);


}


