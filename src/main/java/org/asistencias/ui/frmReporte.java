package org.asistencias.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class frmReporte extends JFrame {

    public frmReporte(String titulo, String[] columnas, List<String[]> filas) {
        setTitle(titulo);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        for (String[] fila : filas) modelo.addRow(fila);

        add(new JScrollPane(new JTable(modelo)), BorderLayout.CENTER);
    }
}
