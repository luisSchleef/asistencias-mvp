package org.asistencias.ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.util.regex.Pattern;

public final class filtroBusqueda {

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
}
