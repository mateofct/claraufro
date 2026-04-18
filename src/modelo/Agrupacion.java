package modelo;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

public class Agrupacion {
    private String idAgrupacion,
    private String nombreAgrupacion;
    private int saldoTotal;
    private List<String> idMiembros;

    public Agrupacion(String nombreAgrupacion ){
        this.idAgrupacion = UUID.randomUUID().toString();
        this.nombreAgrupacion = nombreAgrupacion;
        this.saldoTotal = 0;
        this.idMiembros = new ArrayList<>();
    }

    public void agregarMiembro(String idUsuario) {
        boolean miembroExiste = true;
        for (String recorrerID : this.idMiembros) {
            if (recorrerID.equals(idUsuario)) {
                miembroExiste = false;
                break;
            }
        }
        if (miembroExiste) {
            this.idMiembros.add(idUsuario);

        }
    }
}
