public class Main {
    public static void main(String[] args) {
        try {
            com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme.setup();
        } catch (Exception ex) {
            System.err.println("Error al inicializar FlatLaf: " + ex.getMessage());
        }

        java.awt.EventQueue.invokeLater(() -> {
            new ui.frmLogin().setVisible(true);
        });
    }
}
