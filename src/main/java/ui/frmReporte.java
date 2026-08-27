package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
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

        JTable tabla = new JTable(modelo);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);

        JTextField txtBuscar = new JTextField();
        txtBuscar.putClientProperty("JTextField.placeholderText", "Buscar...");
        utils.escuchar(txtBuscar, sorter);

        JPanel buscar = new JPanel(new GridLayout(1, 4, 6, 0));
        buscar.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        buscar.add(txtBuscar);
        buscar.add(new JLabel(""));
        buscar.add(txtBuscar);
        buscar.add(new JLabel(""));

        add(buscar, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }
}
