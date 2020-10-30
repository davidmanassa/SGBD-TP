package tp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class Browser {
    private JTable encTable;
    private JPanel panel;
    private JButton atualizarButton;

    public Browser() {

        System.out.println("Browser");

        JFrame frame = new JFrame("Browser");
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
            rs = stmt.executeQuery("SELECT * FROM Encomenda;");

            int rowI = 0;
            while (rs.next()) {

                int encId = rs.getInt("EncID");
                String Nome = rs.getString("Nome");
                String Morada = rs.getString("Morada");

                rs.last();
                int rowCount = rs.getRow();
                rs.beforeFirst();

                MyDefaultTableModel dtm = new MyDefaultTableModel(rowCount,4);
                encTable.setModel(dtm);

                JTableHeader th = encTable.getTableHeader();
                th.getColumnModel().getColumn(0).setHeaderValue("ID");
                th.getColumnModel().getColumn(1).setHeaderValue("Nome");
                th.getColumnModel().getColumn(2).setHeaderValue("Morada");
                th.getColumnModel().getColumn(3).setHeaderValue("Produtos");
                th.repaint();

                stmt = Main.connection.createStatement();
                rs = stmt.executeQuery("SELECT * FROM EncLinha WHERE EncID="+encId+";");


                JTable linhasTable = new JTable(buildTableModel(rs));

                dtm.setValueAt(encId, rowI, 0);
                dtm.setValueAt(Nome, rowI, 1);
                dtm.setValueAt(Morada, rowI, 2);
                dtm.setValueAt(new JScrollPane(linhasTable), rowI, 3);

                rowI += 1;
            }

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
