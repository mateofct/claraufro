package Vista;
import javax.swing.*;
import java.awt.*;
import Modelo.Usuario;
import Modelo.TipoMovimiento;
import Controlador.ControladorFinanzas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class VentanaRegistrarMovimiento extends JFrame{
    private ControladorFinanzas controladorFinanzas;
    private Usuario usuario;
    private boolean Ingreso;
    private JTextField EscribirMonto;
    private JTextField EscribirDescripcion;
    private File archivoComprobante = null;

    public VentanaRegistrarMovimiento(ControladorFinanzas controladorFinanzas, Usuario usuario, boolean Ingreso){
        this.controladorFinanzas = controladorFinanzas;
        this.usuario = usuario;
        this.Ingreso = Ingreso;


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

        JLabel etiquetaComprobante = new JLabel("Comprobante del movimiento:");
        etiquetaComprobante.setFont(new Font("Arial", Font.BOLD, 13));
        etiquetaComprobante.setBounds(20, 228, 340, 20);
        add(etiquetaComprobante);

        JButton botonSubirPDF = new JButton("Seleccionar archivo PDF del comprobante");
        botonSubirPDF.setFont(new Font("Arial", Font.PLAIN, 13));
        botonSubirPDF.setBackground(Color.WHITE);
        botonSubirPDF.setBounds(20, 250, 340, 35);

        botonSubirPDF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser seleccionador = new JFileChooser();
                seleccionador.setDialogTitle("Seleccione el PDF del Comprobante");
                int resultado = seleccionador.showOpenDialog(VentanaRegistrarMovimiento.this);

                if (resultado == JFileChooser.APPROVE_OPTION) {
                    archivoComprobante = seleccionador.getSelectedFile();
                    botonSubirPDF.setText("Archivo subido: " + archivoComprobante.getName());
                    botonSubirPDF.setBackground(new Color(210, 235, 220));
                }
            }
        });
        add(botonSubirPDF);

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
        try {
            String montoTexto   = EscribirMonto.getText().trim();
            String descripcion  = EscribirDescripcion.getText().trim();

            if (montoTexto.isEmpty() || descripcion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar el monto y la descripción.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int monto = Integer.parseInt(montoTexto);
            TipoMovimiento tipo;

            if (Ingreso) {
                tipo = TipoMovimiento.INGRESO;
            } else {
                tipo = TipoMovimiento.EGRESO;
            }

            // CORRECCIÓN LÍNEA 161: Extracción segura de la ruta a String
            String rutaAbsoluta = (archivoComprobante != null) ? archivoComprobante.getAbsolutePath() : "";

            controladorFinanzas.registrarMovimiento(usuario.getIdAgrupacion(), tipo, monto, descripcion, rutaAbsoluta, usuario.getIdUsuario());

            // Si el controlador no interrumpe el flujo, es un éxito real
            JOptionPane.showMessageDialog(this, "Movimiento registrado correctamente");
            dispose();

        } catch (NumberFormatException ex) {
            // Caso límite: el usuario puso letras o símbolos en el monto
            JOptionPane.showMessageDialog(this, "El monto debe ser un número entero válido.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            // Caso límite: Atrapa los fallos de saldo insuficiente o montos negativos de tu Main.Controlador
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error en el registro", JOptionPane.ERROR_MESSAGE);
        }
    }


}
