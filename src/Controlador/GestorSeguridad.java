package Controlador;

public class GestorSeguridad {
    private static final int DESPLAZAMIENTO = 6;

    public static String cifrar(String texto) {
        if (texto == null) return null;
        StringBuilder resultado = new StringBuilder();
        for (char c : texto.toCharArray()) {
            resultado.append((char) (c + DESPLAZAMIENTO));
        }
        return resultado.toString();
    }
}