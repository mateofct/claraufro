package modelo;
import java.util.UUID;

public class Agrupacion {
    private String idAgrupacion,
    private String nombreAgrupacion;
    private int saldoTotal;

    public Agrupacion(String nombreAgrupacion ){
        this.idAgrupacion = UUID.randomUUID().toString();
        this.nombreAgrupacion = nombreAgrupacion;
        this.saldoTotal = 0;
    }
}
