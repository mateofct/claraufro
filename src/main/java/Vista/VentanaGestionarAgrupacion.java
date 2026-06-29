package Vista;

import Controlador.ControladorAgrupacion;
import Modelo.Agrupacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaGestionarAgrupacion extends JFrame {
    private ControladorAgrupacion controladorAgrupacion;
    private JTable tablaAgrupaciones;
    private DefaultTableModel modeloTabla;

    public VentanaGestionarAgrupacion(ControladorAgrupacion controladorAgrupacion) {
        this.controladorAgrupacion = controladorAgrupacion;
    }
}