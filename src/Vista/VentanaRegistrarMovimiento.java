package Vista;
import javax.swing.*;
import java.awt.*;
import Modelo.Usuario;
import Modelo.TipoMovimiento;
import Controlador.ControladorFinanzas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaRegistrarMovimiento extends JFrame{
    private ControladorFinanzas controladorFinanzas;
    private Usuario usuario;
    private boolean Ingreso;
    private String rutaComprobante;
    private JTextField EscribirMonto;
    private JTextField EscribirDescripcion;

    public VentanaRegistrarMovimiento(ControladorFinanzas controladorFinanzas, Usuario usuario, boolean Ingreso, String rutaComprobante){
        this.controladorFinanzas = controladorFinanzas;
        this.usuario = usuario;
        this.Ingreso = Ingreso;
        this.rutaComprobante = rutaComprobante;

        if (Ingreso) {
            setTitle("Registrar Ingreso");
        } else {
            setTitle("Registrar Egreso");
        }

        setSize(400, 380);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 240, 240));

        Color colorTipo;
        if (Ingreso) {
            colorTipo = new Color(29, 158, 117);
        } else {
            colorTipo = new Color(200, 50, 50);
        }

        String textoTitulo;
        if (Ingreso) {
            textoTitulo = "Registrar Ingreso";
        } else {
            textoTitulo = "Registrar Egreso";
        }
        JLabel titulo = new JLabel(textoTitulo);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(20, 10, 300, 30);
        add(titulo);

        int saldoActual = controladorFinanzas.calcularSaldo(usuario.getIdAgrupacion());
        JLabel etiquetaSaldo = new JLabel("Saldo actual: $" + saldoActual);
        etiquetaSaldo.setFont(new Font("Arial", Font.PLAIN, 12));
        etiquetaSaldo.setForeground(Color.WHITE);
        etiquetaSaldo.setBounds(20, 42, 300, 20);
        add(etiquetaSaldo);

        JLabel etiquetaMonto = new JLabel("Monto $:");
        etiquetaMonto.setFont(new Font("Arial", Font.BOLD, 13));
        etiquetaMonto.setBounds(20, 88, 340, 20);
        add(etiquetaMonto);

        EscribirMonto = new JTextField();
        EscribirMonto.setFont(new Font("Arial", Font.PLAIN, 13));
        EscribirMonto.setBounds(20, 110, 340, 35);
        add(EscribirMonto);

        JLabel etiquetaDescripcion = new JLabel("Descripción del movimiento:");
        etiquetaDescripcion.setFont(new Font("Arial", Font.BOLD, 13));
        etiquetaDescripcion.setBounds(20, 158, 340, 20);
        add(etiquetaDescripcion);

        EscribirDescripcion = new JTextField();
        EscribirDescripcion.setFont(new Font("Arial", Font.PLAIN, 13));
        EscribirDescripcion.setBounds(20, 180, 340, 35);
        add(EscribirDescripcion);

        JLabel RegistroUsuario = new JLabel("Registrado por: " + usuario.getNombre());
        RegistroUsuario.setFont(new Font("Arial", Font.ITALIC, 11));
        RegistroUsuario.setForeground(Color.GRAY);
        RegistroUsuario.setBounds(20, 246, 340, 20);
        add(RegistroUsuario);

        String textoBoton;
        if (Ingreso) {
            textoBoton = "Guardar ingreso";
        } else {
            textoBoton = "Guardar egreso";
        }

        JButton botonGuardar = new JButton(textoBoton);
        botonGuardar.setFont(new Font("Arial", Font.BOLD, 13));
        botonGuardar.setBackground(colorTipo);
        botonGuardar.setForeground(Color.WHITE);
        botonGuardar.setBounds(20, 295, 160, 38);

        botonGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarMovimiento();
            }
        });
        add(botonGuardar);

        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.setFont(new Font("Arial", Font.BOLD, 13));
        botonCancelar.setBackground(Color.WHITE);
        botonCancelar.setBounds(200, 295, 160, 38);

        botonCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        add(botonCancelar);
        setVisible(true);


    }

    private void guardarMovimiento() {
        String montoTexto   = EscribirMonto.getText().trim();
        String descripcion  = EscribirDescripcion.getText().trim();
        int monto;

        try {
            monto = Integer.parseInt(montoTexto);
        } catch (NumberFormatException error) {
            JOptionPane.showMessageDialog(this, "El monto debe ser un número entero");
            return;
        }

        if (monto <= 0) {
            JOptionPane.showMessageDialog(this, "El monto no puede ser negativo o cero");
            return;
        }

        TipoMovimiento tipo;
        if (Ingreso) {
            tipo = TipoMovimiento.INGRESO;
        } else {
            tipo = TipoMovimiento.EGRESO;
        }

        controladorFinanzas.registrarMovimiento(usuario.getIdAgrupacion(), tipo, monto, descripcion, rutaComprobante, usuario.getIdUsuario());
        JOptionPane.showMessageDialog(this, "Movimiento registrado correctamente");
        dispose();
    }


}
