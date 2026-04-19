package modelo;
import java.util.UUID;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Movimiento {
    private String idAgrupacion;
    private String idMovimiento;
    private TipoMovimiento tipoMovimiento;
    private int monto;
    private String  fechaMovimiento;
    private String descripcionMovimiento;
    private String rutaComprobante;
    private String idUsuarioQueRegistra;


    public Movimiento(String idAgrupacion, TipoMovimiento tipoMovimiento, int monto, String descripcionMovimiento, String rutaComprobante, String idUsuarioQueRegistra ) {
        this.idMovimiento = UUID.randomUUID().toString();
        this.idAgrupacion = idAgrupacion;
        this.tipoMovimiento = tipoMovimiento;
        this.monto = monto;
        this.descripcionMovimiento = descripcionMovimiento;
        this.rutaComprobante = rutaComprobante;
        this.idUsuarioQueRegistra = idUsuarioQueRegistra;
        this.fechaMovimiento = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

    }

    public String getIdMovimiento() {
        return idMovimiento;
    }
    public String getIdAgrupacion() {
        return idAgrupacion;
    }
    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }
    public int getMonto() {
        return monto;
    }
    public String getFechaMovimiento() {
        return fechaMovimiento;
    }
    public String getDescripcionMovimiento() {
        return descripcionMovimiento;
    }
    public String getRutaComprobante() {
        return rutaComprobante;
    }
    public String getIdUsuarioQueRegistra() {
        return idUsuarioQueRegistra;
    }



    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }
    public void setMonto(int monto) {
        this.monto = monto;
    }
    public void setDescripcionMovimiento(String descripcionMovimiento) {
        this.descripcionMovimiento = descripcionMovimiento;
    }
    public void setRutaComprobante(String rutaComprobante) {
        this.rutaComprobante = rutaComprobante;
    }

}
