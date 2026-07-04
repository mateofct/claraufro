package Controlador;

import java.util.regex.Pattern;

public class GestorMatriculas implements IFuenteMatriculas {

    private static final String PATRON_MATRICULA = "^[0-9k]{11}$";

    public static String normalizarMatricula(String matricula) {
        if (matricula == null) {
            return "";
        }
        return matricula.trim().toLowerCase();
    }

    public static boolean validarFormatoMatricula(String matricula) {
        if (matricula == null || matricula.isEmpty()) {
            return false;
        }
        String matriculaNormalizada = normalizarMatricula(matricula);
        return Pattern.matches(PATRON_MATRICULA, matriculaNormalizada);
    }

}