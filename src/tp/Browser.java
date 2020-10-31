package tp;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;

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
                frame.dispose();
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

        java.util.Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }, 0, 10000); // 5 SECONDS

    }

    public void update() {

        Statement stmt = null;
        ResultSet rs = null;

        try {

            stmt = Main.connection.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Encomenda;");

            DefaultTableModel dtm = new DefaultTableModel(0,3);
            encTable.setModel(dtm);

            JTableHeader th = encTable.getTableHeader();
            th.getColumnModel().getColumn(0).setHeaderValue("ID");
            th.getColumnModel().getColumn(1).setHeaderValue("Nome");
            th.getColumnModel().getColumn(2).setHeaderValue("Morada");
            th.repaint();

            while (rs.next()) {

                int encId = rs.getInt("EncID");
                String Nome = rs.getString("Nome");
                String Morada = rs.getString("Morada");

                dtm.addRow(new Object[]{encId, Nome, Morada});

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        encTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {

                int toTry;
                try {
                    toTry = Integer.parseInt(encTable.getValueAt(encTable.getSelectedRow(), 0).toString());
                } catch (IndexOutOfBoundsException ex) {
                    toTry = lastSelected;
                }
                lastSelected = toTry;

                try {

                    int encId = toTry;

                    Statement stmt1 = Main.connection.createStatement();
                    ResultSet rs1 = stmt1.executeQuery("SELECT * FROM EncLinha WHERE EncID=" + encId + ";");

                    DefaultTableModel dtm = new DefaultTableModel(0,3);
                    productTable.setModel(dtm);

                    JTableHeader th = productTable.getTableHeader();
                    th.getColumnModel().getColumn(0).setHeaderValue("Designação");
                    th.getColumnModel().getColumn(1).setHeaderValue("Preço");
                    th.getColumnModel().getColumn(2).setHeaderValue("Quantidade");
                    th.repaint();

                    while (rs1.next()) {

                        String designacao = rs1.getString("Designacao");
                        BigDecimal preco = rs1.getBigDecimal("Preco");
                        BigDecimal qtd = rs1.getBigDecimal("Qtd");

                        dtm.addRow(new Object[]{designacao, preco, qtd});

                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
