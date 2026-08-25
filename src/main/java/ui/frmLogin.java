package ui;

import controller.login;
import model.user;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class frmLogin extends JFrame {

    public frmLogin() {
        setTitle("Control de Asistencia - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(350, 180);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        FlatSVGIcon arrow = new FlatSVGIcon("icons/login-arrow.svg", 24,24);
        FlatSVGIcon person = new FlatSVGIcon("icons/login.svg", 100,100);

        JPanel panelLeft = new JPanel(new GridBagLayout());
        JPanel panelRigt = new JPanel(new GridLayout(3, 1, 8, 8));
        panelRigt.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField txtCorreo = new JTextField();
        txtCorreo.putClientProperty("JTextField.placeholderText", "Correo");
        JPasswordField txtPass = new JPasswordField();
        JButton btnIngresar = new JButton(arrow);
        txtPass.putClientProperty("JTextField.placeholderText", "Contraseña");

        panelLeft.add(new JLabel(person));

        panelRigt.add(txtCorreo);
        panelRigt.add(txtPass);
        panelRigt.add(btnIngresar);

        aplicarCursorMano(btnIngresar);

        add(panelLeft);
        add(panelRigt);

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
    public void aplicarCursorMano(JButton... botones) {
        Cursor mano = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        for (JButton btn : botones) {
            if (btn != null) {
                btn.setCursor(mano);
            }
        }
    }
}
