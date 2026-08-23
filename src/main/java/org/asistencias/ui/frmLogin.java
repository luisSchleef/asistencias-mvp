package org.asistencias.ui;

import org.asistencias.controller.login;
import org.asistencias.model.user;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class frmLogin extends JFrame {

    public frmLogin() {
        setTitle("Control de Asistencia - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(350, 180);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField txtCorreo = new JTextField();
        JPasswordField txtPass = new JPasswordField();
        JButton btnIngresar = new JButton("Ingresar");

        panel.add(new JLabel("Correo:"));
        panel.add(txtCorreo);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtPass);
        panel.add(new JLabel());
        panel.add(btnIngresar);
        add(panel);

        login login = new login();
        getRootPane().setDefaultButton(btnIngresar);
        btnIngresar.addActionListener(e -> {
            try {
                user u = login.autenticar(txtCorreo.getText().trim(), new String(txtPass.getPassword()));
                if (u != null) {
                    new frmMenu(u).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
