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


    public Movimiento(String idAgrupacion, String idMovimiento, TipoMovimiento tipoMovimiento, int monto, String descripcionMovimiento, String rutaComprobante, String idUsuarioQueRegistra ) {
        this.idMovimiento = UUID.randomUUID().toString();
        this.idAgrupacion = idAgrupacion;
        this.tipoMovimiento = tipoMovimiento;
        this.monto = monto;
        this.descripcionMovimiento = descripcionMovimiento;
        this.rutaComprobante = rutaComprobante;
        this.idUsuarioQueRegistra = idUsuarioQueRegistra;
        this.fechaMovimiento = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

    }

}
