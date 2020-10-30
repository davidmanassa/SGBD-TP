package tp;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Browser {
    private JTable encTable;
    private JPanel panel;
    private JButton atualizarButton;
    private JTable productTable;

    int lastSelected = -1;

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

        update();

        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });
    }

    public void update() {

        Statement stmt = null;
        ResultSet rs = null;

        try {

            stmt = Main.connection.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Encomenda;");
            int rowCount = getRowCount(rs);
            stmt = Main.connection.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Encomenda;");

            MyDefaultTableModel dtm = new MyDefaultTableModel(rowCount,3);
            encTable.setModel(dtm);

            JTableHeader th = encTable.getTableHeader();
            th.getColumnModel().getColumn(0).setHeaderValue("ID");
            th.getColumnModel().getColumn(1).setHeaderValue("Nome");
            th.getColumnModel().getColumn(2).setHeaderValue("Morada");
            th.repaint();

            int rowI = 0;
            while (rs.next()) {

                int encId = rs.getInt("EncID");
                String Nome = rs.getString("Nome");
                String Morada = rs.getString("Morada");

                dtm.setValueAt(encId, rowI, 0);
                dtm.setValueAt(Nome, rowI, 1);
                dtm.setValueAt(Morada, rowI, 2);

                rowI += 1;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        encTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {

                int toTry = encTable.getSelectedRow();
                if (toTry == -1)
                    toTry = lastSelected;
                lastSelected = toTry;

                try {

                    int encId = Integer.parseInt(encTable.getValueAt(toTry, 0).toString());

                    System.out.println("Selected: " + encId);

                    Statement stmt1 = Main.connection.createStatement();
                    ResultSet rs1 = stmt1.executeQuery("SELECT * FROM EncLinha WHERE EncID=" + encId + ";");
                    int rowCount = getRowCount(rs1);
                    rs1 = stmt1.executeQuery("SELECT * FROM EncLinha WHERE EncID=" + encId + ";");

                    MyDefaultTableModel dtm = new MyDefaultTableModel(rowCount,3);
                    productTable.setModel(dtm);

                    JTableHeader th = productTable.getTableHeader();
                    th.getColumnModel().getColumn(0).setHeaderValue("Designação");
                    th.getColumnModel().getColumn(1).setHeaderValue("Preço");
                    th.getColumnModel().getColumn(2).setHeaderValue("Quantidade");
                    th.repaint();

                    int rowI = 0;
                    while (rs1.next()) {

                        String designacao = rs1.getString("Designacao");
                        BigDecimal preco = rs1.getBigDecimal("Preco");
                        BigDecimal qtd = rs1.getBigDecimal("Qtd");

                        dtm.setValueAt(designacao, rowI, 0);
                        dtm.setValueAt(preco, rowI, 1);
                        dtm.setValueAt(qtd, rowI, 2);

                        rowI += 1;

                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public int getRowCount(ResultSet rs) {
        int i = 0;
        try {
            while (rs.next()) {
                i++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return i;
    }

}
