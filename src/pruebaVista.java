import Vista.VentanaIniciarSesion;
import javax.swing.SwingUtilities;

public class pruebaVista {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaIniciarSesion ventana = new VentanaIniciarSesion();
            ventana.UISesion();
        });
    }
}
