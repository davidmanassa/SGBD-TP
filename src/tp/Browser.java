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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;

public class Browser {
    private JTable encTable;
    private JPanel panel;
    private JButton atualizarButton;
    private JTable productTable;
    private JComboBox transactionIsolationLevel;
    private JLabel labelTimer;

    java.util.Timer timer;

    int insolationLevel = 1;
    Connection conn;
    String app = "Browser";

    java.util.Timer lTimer = null;
    int timeToUpdate = 4000; // 1 segundo
    int timeToUpdateScale = 100;
    int currentUpdateLabel = timeToUpdate;
    private void updateLabelTimer() {
        labelTimer.setText("Update em: " + ((double) currentUpdateLabel / 1000));
        currentUpdateLabel -= timeToUpdateScale;
    }

    public Browser() {

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

        update();

        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }, 0, timeToUpdate);

        lTimer = new Timer();
        lTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateLabelTimer();
            }
        }, 0, timeToUpdateScale);

    }

    int lastSelectedID = -1;
    public void update() {

        Statement stmt = null;
        ResultSet rs = null;

        try {

            stmt = conn.createStatement();
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

            dtm.fireTableDataChanged();
            encTable.repaint();

            if (lastSelectedID != -1)
                updateProductTable(lastSelectedID);

        } catch (Exception ex) {
            // ex.printStackTrace();
        }

        encTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {

                int newId = -1;
                try {
                    newId = Integer.parseInt(encTable.getValueAt(encTable.getSelectedRow(), 0).toString());
                } catch (Exception ex) {
                }

                if (newId != -1) {
                    updateProductTable(newId);
                    lastSelectedID = newId;
                } else if (lastSelectedID != -1)
                    updateProductTable(lastSelectedID);

            }
        });

        currentUpdateLabel = timeToUpdate;

    }

    public void updateProductTable(int id) {

        Statement stmt = null;
        ResultSet rs = null;

        try {

            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM EncLinha WHERE EncID=" + id + ";");

            DefaultTableModel dtm = new DefaultTableModel(0, 3);
            productTable.setModel(dtm);

            JTableHeader th = productTable.getTableHeader();
            th.getColumnModel().getColumn(0).setHeaderValue("Designação");
            th.getColumnModel().getColumn(1).setHeaderValue("Preço");
            th.getColumnModel().getColumn(2).setHeaderValue("Quantidade");
            th.repaint();

            while (rs.next()) {

                String designacao = rs.getString("Designacao");
                BigDecimal preco = rs.getBigDecimal("Preco");
                BigDecimal qtd = rs.getBigDecimal("Qtd");

                dtm.addRow(new Object[]{designacao, preco, qtd});

            }

            dtm.fireTableDataChanged();
            productTable.repaint();

        } catch (Exception ex) {
            // ex.printStackTrace();
        }

    }

}