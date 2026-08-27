package ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.regex.Pattern;

public final class utils {

    public static void escuchar(JTextField txt, TableRowSorter<?> sorter) {
        txt.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            public void changedUpdate(DocumentEvent e) { filtrar(); }
            private void filtrar() {
                String texto = txt.getText().trim();
                sorter.setRowFilter(texto.isEmpty() ? null
                        : RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
            }
        });
    }

    public static void aplicarCursorMano(JButton... botones) {
        Cursor mano = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        for (JButton btn : botones) {
            if (btn != null) {
                btn.setCursor(mano);
            }
        }
    }
}
