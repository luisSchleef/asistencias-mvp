package ui;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import controller.crud;
import model.user;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class frmMenu extends JFrame {

    public frmMenu(user usuario) {
        boolean esAdmin = "ADMIN".equals(usuario.rol());
        setTitle("Control de Asistencia");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, esAdmin ? 320 : 240);
        setLocationRelativeTo(null);


        FlatSVGIcon arrowUp = new FlatSVGIcon("icons/arrow-up.svg", 24, 24);
        FlatSVGIcon arrrowDown = new FlatSVGIcon("icons/arrow-down.svg", 24, 24);
        FlatSVGIcon arrowLeft = new FlatSVGIcon("icons/arrow-left.svg", 24, 24);
        FlatSVGIcon users = new FlatSVGIcon("icons/users.svg", 24, 24);
        FlatSVGIcon reports = new FlatSVGIcon("icons/reports.svg", 24, 24);


        JButton btnEntrada = new JButton("Marcar Entrada", arrowUp);
        JButton btnSalida = new JButton("Marcar Salida", arrrowDown);
        JButton btnCerrar = new JButton("Cerrar Sesión", arrowLeft);
        btnCerrar.setBackground(Color.decode("#D10000"));
        btnCerrar.setForeground(Color.WHITE);

        crud crud = new crud();

        JLabel lblBienvenida = new JLabel("Hola, " + usuario.nombre(), SwingConstants.CENTER);
        lblBienvenida.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        add(lblBienvenida, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        panel.add(btnEntrada);
        panel.add(btnSalida);

        if (esAdmin) {
            JButton btnAtrasos = new JButton("Reporte de Atrasos", reports);
            JButton btnSalidas = new JButton("Reporte Salidas Anticipadas", reports);
            JButton btnInasistencias = new JButton("Reporte de Inasistencias", reports);
            JButton btnUsuarios = new JButton("Gestionar Usuarios", users);
            btnAtrasos.addActionListener(e -> abrirReporte("Reporte de Entradas Atrasadas (post 9:30)",
                    new String[]{"ID", "Nombre", "Fecha", "Hora Entrada"}, crud::reporteAtrasos));
            btnSalidas.addActionListener(e -> abrirReporte("Reporte de Salidas Anticipadas (antes de 17:30)",
                    new String[]{"ID", "Nombre", "Fecha", "Hora Salida"}, crud::reporteSalidasAnticipadas));
            btnInasistencias.addActionListener(e -> abrirReporte("Reporte de Inasistencias (lun-vie)",
                    new String[]{"ID", "Nombre", "Fecha Ausencia"}, crud::reporteInasistencias));
            btnUsuarios.addActionListener(e -> new frmUser(usuario).setVisible(true));
            panel.add(btnAtrasos);
            panel.add(btnSalidas);
            panel.add(btnInasistencias);
            panel.add(btnUsuarios);
            aplicarCursorMano(btnAtrasos, btnInasistencias, btnUsuarios, btnSalidas);
        }
        panel.add(btnCerrar);
        add(panel);

        aplicarCursorMano(btnEntrada, btnSalida,btnCerrar);

        btnEntrada.addActionListener(e -> marcar(crud, usuario, "ENTRADA", btnEntrada));
        btnSalida.addActionListener(e -> marcar(crud, usuario, "SALIDA", btnSalida));
        btnCerrar.addActionListener(e -> {
            new frmLogin().setVisible(true);
            dispose();
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

    private void abrirReporte(String titulo, String[] columnas, java.util.concurrent.Callable<List<String[]>> carga) {
        try {
            new frmReporte(titulo, columnas, carga.call()).setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void marcar(crud crud, user usuario, String tipo, JButton boton) {
        try {
            crud.registrar(usuario.id(), tipo);
            JOptionPane.showMessageDialog(this, tipo + " registrada correctamente");
            boton.setEnabled(false);
            Timer timer = new Timer(30_000, e -> boton.setEnabled(true));
            timer.setRepeats(false);
            timer.start();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
