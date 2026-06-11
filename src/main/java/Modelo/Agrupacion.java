package Modelo;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

public class Agrupacion {
    private String idAgrupacion;
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
                System.out.println("El usuario ya es miembro de la agrupación");
                break;
            }
        }
        if (miembroExiste) {
            this.idMiembros.add(idUsuario);
        }
    }

    public void quitarMiembro(String idMiembro){
        if (this.idMiembros.contains(idMiembro)) {
            this.idMiembros.remove(idMiembro);
            System.out.println("El usuario ha sido removido de la agrupación");
        } else {
            System.out.println("El usuario no es miembro de la agrupación");
        }
    }

    public String getIdAgrupacion() {
        return this.idAgrupacion;
    }

    public String getNombreAgrupacion() {
        return this.nombreAgrupacion;
    }

    public int getSaldoTotal(){
        return this.saldoTotal;
    }

    public List<String> getIdMiembros(){
        return this.idMiembros;
    }

    public void setNombreAgrupacion(String nombreAgrupacion){
        this.nombreAgrupacion = nombreAgrupacion;
    }

    public void setSaldoTotal(int saldoTotal) {
        this.saldoTotal = saldoTotal;
    }
}