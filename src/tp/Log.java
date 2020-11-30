package tp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class Log {

    private JPanel panel;
    private JPanel buttonPanel;

    private JScrollPane scrollPane = null;
    private JTable table = null;

    java.util.Timer timer = null;

    public Log() {

        System.out.println("Log");

        JFrame frame = new JFrame("Log");
        frame.setContentPane(panel);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                if (timer != null)
                    timer.cancel();
                new Menu();
            }
        });
        frame.pack();
        frame.setVisible(true);

        JButton updateButton = new JButton("Atualizar");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                update();

            }
        });
        buttonPanel.add(updateButton);

        update();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }, 0, 1000); // 1 SECONDS

    }

    public void update() {

        Statement stmt = null;
        ResultSet rs = null;

        try {

            stmt = Main.connection.createStatement();
            rs = stmt.executeQuery("SELECT TOP 50 * FROM LogOperations WHERE EventType = 'I' OR EventType = 'U' OR EventType = 'D' ORDER BY DCriacao DESC;");

            if (table == null) {
                table = new JTable(buildTableModel(rs));
            } else {
                ((DefaultTableModel) table.getModel()).setRowCount(0);
                table.setModel(buildTableModel(rs));
            }
            table.repaint();

            if (scrollPane == null) {
                scrollPane = new JScrollPane(table);
                scrollPane.repaint();

                panel.add(scrollPane);
            }

        } catch (SQLException ex) {
            // ex.printStackTrace();
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
