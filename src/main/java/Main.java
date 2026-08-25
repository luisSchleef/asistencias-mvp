package org.asistencias;

public class Main {
    public static void main(String[] args) {
        try {
            com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme.setup();
        } catch (Exception ex) {
            System.err.println("Error al inicializar FlatLaf: " + ex.getMessage());
        }

        java.awt.EventQueue.invokeLater(() -> {
            new org.asistencias.ui.frmLogin().setVisible(true);
        });
    }
}
