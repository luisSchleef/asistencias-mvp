package org.asistencias.ui;

import org.asistencias.controller.crud;
import org.asistencias.model.user;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class frmUser extends JFrame {

    private final crud crud = new crud();
    private final user admin;
    private final DefaultTableModel modelo;
    private final JTable tabla;

    public frmUser(user admin) {
        this.admin = admin;
        setTitle("Gestión de Usuarios");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(640, 420);
        setLocationRelativeTo(null);

        modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Correo", "Rol"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton btnCrear = new JButton("Crear");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JTextField txtBuscar = new JTextField();
        txtBuscar.putClientProperty("JTextField.placeholderText", "Buscar...");


        JPanel botones = new JPanel();
        JPanel buscar = new JPanel(new GridLayout(1, 4, 6, 0));
        buscar.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        botones.add(btnCrear);
        botones.add(btnModificar);
        botones.add(btnEliminar);
        buscar.add(new JLabel(""));
        buscar.add(txtBuscar);
        buscar.add(new JLabel(""));


        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(buscar, BorderLayout.NORTH);
        add(botones, BorderLayout.SOUTH);

        btnCrear.addActionListener(e -> crear());
        btnModificar.addActionListener(e -> modificar());
        btnEliminar.addActionListener(e -> eliminar());

        cargar();
    }

    private void cargar() {
        modelo.setRowCount(0);
        try {
            List<String[]> filas = crud.listarUsuarios();
            for (String[] fila : filas) modelo.addRow(fila);
        } catch (SQLException ex) {
            error("Error al cargar usuarios", ex);
        }
    }

    private void buscar() {

    }

    private void dialogoUsuario(Integer filaSeleccionada) {
        boolean editar = filaSeleccionada != null;
        String nombreIni = "", correoIni = "", rolIni = "EMPLEADO";
        int id = 0;
        if (editar) {
            if (filaSeleccionada < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un usuario", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            id = Integer.parseInt((String) modelo.getValueAt(filaSeleccionada, 0));
            nombreIni = (String) modelo.getValueAt(filaSeleccionada, 1);
            correoIni = (String) modelo.getValueAt(filaSeleccionada, 2);
            rolIni = (String) modelo.getValueAt(filaSeleccionada, 3);
        }

        JTextField txtNombre = new JTextField(nombreIni, 15);
        JTextField txtCorreo = new JTextField(correoIni, 15);
        JPasswordField txtPass = new JPasswordField(15);
        JPasswordField txtPass2 = new JPasswordField(15);
        JComboBox<String> cmbRol = new JComboBox<>(new String[]{"EMPLEADO", "ADMIN"});
        cmbRol.setSelectedItem(rolIni);
        if (JOptionPane.showConfirmDialog(this, formulario(txtNombre, txtCorreo, txtPass, txtPass2, cmbRol),
                editar ? "Modificar Usuario (contraseña vacía = sin cambio)" : "Crear Usuario",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;

        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String pass = new String(txtPass.getPassword());
        if (nombre.isEmpty() || correo.isEmpty() || (!editar && pass.isEmpty())) {
            JOptionPane.showMessageDialog(this, "Nombre, correo" + (editar ? "" : " y contraseña") + " son obligatorios",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!pass.equals(new String(txtPass2.getPassword()))) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            if (editar) {
                crud.actualizarUsuario(id, nombre, correo, pass, (String) cmbRol.getSelectedItem());
            } else {
                crud.crearUsuario(nombre, correo, pass, (String) cmbRol.getSelectedItem());
            }
            JOptionPane.showMessageDialog(this, "Usuario " + (editar ? "modificado" : "creado") + " correctamente");
            cargar();
        } catch (SQLException ex) {
            error("Error al " + (editar ? "modificar" : "crear") + " usuario", ex);
        }
    }

    private void crear() {
        dialogoUsuario(null);
    }

    private void modificar() {
        dialogoUsuario(tabla.getSelectedRow());
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = Integer.parseInt((String) modelo.getValueAt(fila, 0));
        if (id == admin.getId()) {
            JOptionPane.showMessageDialog(this, "No puede eliminar su propio usuario",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(this,
                "¿Eliminar a " + modelo.getValueAt(fila, 1) + "? También se eliminarán sus registros de asistencia.",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) return;
        try {
            crud.eliminarUsuario(id);
            JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente");
            cargar();
        } catch (SQLException ex) {
            error("Error al eliminar usuario", ex);
        }
    }

    private static JPanel formulario(JTextField nombre, JTextField correo, JPasswordField pass,
                                     JPasswordField pass2, JComboBox<String> rol) {
        JPanel p = new JPanel(new GridLayout(0, 2, 8, 8));
        p.add(new JLabel("Nombre:"));
        p.add(nombre);
        p.add(new JLabel("Correo:"));
        p.add(correo);
        p.add(new JLabel("Contraseña:"));
        p.add(pass);
        p.add(new JLabel("Confirmar contraseña:"));
        p.add(pass2);
        p.add(new JLabel("Rol:"));
        p.add(rol);
        return p;
    }

    private void error(String mensaje, SQLException ex) {
        JOptionPane.showMessageDialog(this, mensaje + ": " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
