package tp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class Log {

    private JPanel panel;
    private JPanel buttonPanel;
    private JPanel isolationLevelChange;
    private JComboBox transactionIsolationLevel;
    private JLabel labelTimer;

    private JScrollPane scrollPane = null;
    private JTable table = null;

    java.util.Timer timer = null;

    int rows;

    int insolationLevel = 1;
    Connection conn;
    String app = "Log";

    java.util.Timer lTimer = null;
    int timeToUpdate = 1000; // 1 segundo
    int timeToUpdateScale = 100;
    int currentUpdateLabel = timeToUpdate;
    private void updateLabelTimer() {
        labelTimer.setText("Update em: " + ((double) currentUpdateLabel / 1000));
        currentUpdateLabel -= timeToUpdateScale;
    }

    public Log(int rows) {
        this.rows = rows;

        System.out.println(app);

        conn = Main.getNewConnection();

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>( Main.isolationLevels );
        transactionIsolationLevel.setModel( model );
        insolationLevel = transactionIsolationLevel.getSelectedIndex();
        Main.setIsolationLevel(conn, insolationLevel);
        System.out.println("New isolation level for " + app +  ": " + Main.isolationLevels[insolationLevel]);

        transactionIsolationLevel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                insolationLevel = transactionIsolationLevel.getSelectedIndex();
                Main.setIsolationLevel(conn, insolationLevel);
                System.out.println("New isolation level for " + app +  ": " + Main.isolationLevels[insolationLevel]);

            }
        });

        JFrame frame = new JFrame(app);
        frame.setContentPane(panel);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                if (timer != null)
                    timer.cancel();
                if (lTimer != null)
                    lTimer.cancel();
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
        }, 0, timeToUpdate); // 1 SECONDS

        lTimer = new Timer();
        lTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateLabelTimer();
            }
        }, 0, timeToUpdateScale);

    }

    public void update() {

        Statement stmt = null;
        ResultSet rs = null;

        try {

            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT TOP " + rows + " * FROM LogOperations WHERE EventType = 'I' OR EventType = 'U' OR EventType = 'D' ORDER BY DCriacao DESC;");

            DefaultTableModel dtm = buildTableModel(rs);
            if (table == null) {
                table = new JTable(dtm);
            } else {
                ((DefaultTableModel) table.getModel()).setRowCount(0);
                table.setModel(dtm);
                dtm.fireTableDataChanged();
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

        currentUpdateLabel = timeToUpdate;

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
