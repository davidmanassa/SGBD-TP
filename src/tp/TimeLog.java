package tp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class TimeLog {
    private JPanel panel;

    public TimeLog() {

        System.out.println("Log Tempo");

        JFrame frame = new JFrame("Log Tempo");
        frame.setContentPane(panel);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.setVisible(false);
                new Menu();
            }
        });
        frame.pack();
        frame.setVisible(true);

        Statement stmt = null;
        ResultSet rs = null;

        try {

            stmt = Main.connection.createStatement();
            rs = stmt.executeQuery("SELECT LO1.UserId, LO1.Objecto as EncId, DATEDIFF(SS,LO1.Valor, LO2.Valor) as Tempo " +
                    "FROM LogOperations LO1, LogOperations LO2 " +
                    "WHERE LO1.Referencia = LO2.Referencia and LO1.DCriacao < LO2.DCriacao;");

                    // "and " +
                    // "LO1.Referencia = 'G1-20191001101356321';");

            JTable table = new JTable(buildTableModel(rs));

            panel.add(new JScrollPane(table));

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public static DefaultTableModel buildTableModel(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);

    }

}
