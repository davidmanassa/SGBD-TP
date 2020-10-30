package tp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Vector;

public class Log {

    private JPanel panel;

    public Log() {

        System.out.println("Log");

        JFrame frame = new JFrame("Log");
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
            rs = stmt.executeQuery("SELECT TOP 10 * FROM LogOperations WHERE EventType = 'I' OR EventType = 'U' OR EventType = 'D';");

            JTable table = new JTable(buildTableModel(rs));

            JScrollPane jsp = new JScrollPane(table);

            panel.add(jsp);

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
