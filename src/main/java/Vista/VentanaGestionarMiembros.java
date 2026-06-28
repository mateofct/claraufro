package Vista;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

import Controlador.ControladorAgrupacion;
import Controlador.ControladorUsuario;
import Modelo.Agrupacion;
import Modelo.Usuario;

public class VentanaGestionarMiembros extends JFrame {
    private ControladorAgrupacion controladorAgrupacion;
    private ControladorUsuario controladorUsuario;

    private JComboBox<Agrupacion> selectorAgrupacion;
    private JTextField buscarUsuario;
    private DefaultListModel<Usuario> modeloCandidatos;
    private JList<Usuario> listaCandidatos;
    private Usuario usuarioSeleccionado;

    private DefaultListModel<Usuario> modeloMiembros;
    private JList<Usuario> listaMiembros;

    public VentanaGestionarMiembros(ControladorAgrupacion controladorAgrupacion, ControladorUsuario controladorUsuario) {
        this.controladorAgrupacion = controladorAgrupacion;
        this.controladorUsuario = controladorUsuario;

        setTitle("Gestionar miembros de agrupación");
        setSize(520, 600);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(102, 133, 183));

        JLabel etiquetaAgrupacion = new JLabel("Agrupación:");
        etiquetaAgrupacion.setBounds(20, 15, 200, 20);
        add(etiquetaAgrupacion);

        selectorAgrupacion = new JComboBox<>();
        for (Agrupacion a : controladorAgrupacion.listarAgrupaciones()) {
            selectorAgrupacion.addItem(a);
        }
        selectorAgrupacion.setBounds(20, 38, 470, 30);
        selectorAgrupacion.addActionListener(e -> {
            usuarioSeleccionado = null;
            buscarUsuario.setText("");
            actualizarListaMiembros();
            actualizarListaCandidatos("");
        });
        add(selectorAgrupacion);

        JLabel etiquetaBuscar = new JLabel("Buscar usuario por nombre o matrícula:");
        etiquetaBuscar.setBounds(20, 80, 300, 20);
        add(etiquetaBuscar);

        buscarUsuario = new JTextField();
        buscarUsuario.setBounds(20, 102, 470, 30);
        add(buscarUsuario);

        modeloCandidatos = new DefaultListModel<>();
        listaCandidatos = new JList<>(modeloCandidatos);
        JScrollPane scrollCandidatos = new JScrollPane(listaCandidatos);
        scrollCandidatos.setBounds(20, 138, 470, 110);
        add(scrollCandidatos);

        buscarUsuario.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { onTexto(); }
            public void removeUpdate(DocumentEvent e) { onTexto(); }
            public void changedUpdate(DocumentEvent e) { onTexto(); }
            private void onTexto() {
                usuarioSeleccionado = null;
                actualizarListaCandidatos(buscarUsuario.getText());
            }
        });

        listaCandidatos.addListSelectionListener(e -> {
            usuarioSeleccionado = listaCandidatos.getSelectedValue();
        });

        JButton botonAgregar = new JButton("Agregar a la agrupación");
        botonAgregar.setBounds(20, 256, 470, 35);
        botonAgregar.addActionListener(e -> agregarSeleccionado());
        add(botonAgregar);

        JLabel etiquetaMiembros = new JLabel("Miembros actuales:");
        etiquetaMiembros.setBounds(20, 300, 300, 20);
        add(etiquetaMiembros);

        modeloMiembros = new DefaultListModel<>();
        listaMiembros = new JList<>(modeloMiembros);
        JScrollPane scrollMiembros = new JScrollPane(listaMiembros);
        scrollMiembros.setBounds(20, 322, 470, 110);
        add(scrollMiembros);

        JButton botonQuitar = new JButton("Quitar de la agrupación");
        botonQuitar.setBounds(20, 440, 470, 35);
        botonQuitar.addActionListener(e -> quitarSeleccionado());
        add(botonQuitar);

        actualizarListaMiembros();
        actualizarListaCandidatos("");
        setVisible(true);

        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.setBounds(20, 480, 470, 35);
        botonCancelar.addActionListener(e -> {dispose();});
        add(botonCancelar);
        setVisible(true);
    }

    private void actualizarListaCandidatos(String filtro) {
        modeloCandidatos.clear();
        Agrupacion agrupacionActual = (Agrupacion) selectorAgrupacion.getSelectedItem();
        if (agrupacionActual == null) return;

        String filtroNormalizado = filtro.trim().toLowerCase();

        for (Usuario u : controladorUsuario.listarUsuarios()) {
            boolean yaEsMiembro = u.getIdAgrupacion().equals(agrupacionActual.getIdAgrupacion());
            if (yaEsMiembro) continue;

            if (filtroNormalizado.isEmpty()
                    || u.getNombre().toLowerCase().contains(filtroNormalizado)
                    || u.getMatricula().toLowerCase().contains(filtroNormalizado)) {
                modeloCandidatos.addElement(u);
            }
        }
    }

    private void actualizarListaMiembros() {
        modeloMiembros.clear();
        Agrupacion agrupacionActual = (Agrupacion) selectorAgrupacion.getSelectedItem();
        if (agrupacionActual == null) return;

        List<Usuario> miembros = controladorUsuario.listarUsuariosPorAgrupacion(agrupacionActual.getIdAgrupacion());
        for (Usuario u : miembros) {
            modeloMiembros.addElement(u);
        }
    }

    private void agregarSeleccionado() {
        if (usuarioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un usuario de la lista (haciendo clic) antes de agregarlo.", "Selección requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Agrupacion agrupacionActual = (Agrupacion) selectorAgrupacion.getSelectedItem();
        try {
            controladorAgrupacion.agregarMiembro(agrupacionActual.getIdAgrupacion(), usuarioSeleccionado);
            usuarioSeleccionado = null;
            buscarUsuario.setText("");
            actualizarListaMiembros();
            actualizarListaCandidatos("");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void quitarSeleccionado() {
        Usuario miembroSeleccionado = listaMiembros.getSelectedValue();
        if (miembroSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un miembro de la lista para quitarlo.", "Selección requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Agrupacion agrupacionActual = (Agrupacion) selectorAgrupacion.getSelectedItem();
        try {
            controladorAgrupacion.quitarMiembro(agrupacionActual.getIdAgrupacion(), miembroSeleccionado);
            actualizarListaMiembros();
            actualizarListaCandidatos(buscarUsuario.getText());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}