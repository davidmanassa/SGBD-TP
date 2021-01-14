package tp;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class EditEncomenda {

    private JPanel panel;
    private JLabel clientName;
    private JTextField moradaTextField;
    private JLabel address;
    private JTable productTable;
    private JButton guardarButton;
    private JComboBox transactionIsolationLevel;

    private HashMap<String, Integer> initialQuantities;
    String initialAddress;

    int insolationLevel = 1;
    Connection conn;
    String app = "Editar encomenda";

    public EditEncomenda(int id) {

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
        frame.pack();
        frame.setVisible(true);

        Statement stmt = null;
        ResultSet rs = null;

        // ----------------- REGISTO DE ENTRADA
        try {
            conn.setAutoCommit(true);
        } catch (Exception ex) {}

        LocalDateTime datetime = LocalDateTime.now();
        DateTimeFormatter dateFormatted = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String ref = "G1_" + dateFormatted.format(datetime);

        try {

            stmt = conn.createStatement();
            stmt.executeUpdate("Insert Into LogOperations (EventType, Objecto, Valor, Referencia) " +
                    "Values ('O', '"+ id +"', SYSDATETIME(), '" + ref + "')");


        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // ----------------- REGISTO DE FECHO
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {

                close(id, ref);
                frame.dispose();
                new Menu();

            }
        });

        // ----------------- LEITURA DE DADOS
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Encomenda WHERE EncID="+id+";");
            while (rs.next()) {

                String Nome = rs.getString("Nome");
                String Morada = rs.getString("Morada");

                clientName.setText(Nome);
                moradaTextField.setText(Morada);

                initialAddress = Morada;

            }

            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM EncLinha WHERE EncID="+id+";");

            MyDefaultTableModel dtm = new MyDefaultTableModel(0, 2);
            productTable.setModel(dtm);

            JTableHeader th = productTable.getTableHeader();
            th.getColumnModel().getColumn(0).setHeaderValue("Produto");
            th.getColumnModel().getColumn(1).setHeaderValue("Quantidade");
            th.repaint();

            initialQuantities = new HashMap<>();
            int rowNumber = 0;
            while (rs.next()) {

                String produto = rs.getString("Designacao");
                int quantidade = rs.getInt("Qtd");

                dtm.addRow(new Object[]{produto, quantidade});
                dtm.setCellEditable(rowNumber, 0, false);
                dtm.setCellEditable(rowNumber, 1, true);
                // dtm.fireTableCellUpdated(rowNumber, 1);

                initialQuantities.put(produto, quantidade);

                rowNumber += 1;
            }


        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                PreparedStatement stmt = null;
                ResultSet rs = null;
                try {

                    conn.setAutoCommit(false);

                    if (!initialAddress.equals(moradaTextField.getText())) {

                        String stmtText = "UPDATE Encomenda SET Morada = '" + moradaTextField.getText() + "' WHERE EncID = " + id + ";";
                        stmt = conn.prepareStatement(stmtText);
                        stmt.executeUpdate();

                        System.out.println("Updating address to: " + moradaTextField.getText());
                    }

                    for (int i = 0; i < initialQuantities.size(); i++) {
                        int newQuantity = Integer.parseInt(productTable.getValueAt(i, 1).toString());
                        String product = productTable.getValueAt(i, 0).toString();

                        if (initialQuantities.get(product) != newQuantity) {

                            System.out.println("initial quantity of " + product + ": " + initialQuantities.get(product) + " updated: " + newQuantity);

                            String stmtText = "UPDATE EncLinha SET Qtd = '" + newQuantity + "' WHERE EncID = " + id + " AND Designacao = '" + product + "';";

                            stmt = conn.prepareStatement(stmtText);
                            stmt.executeUpdate();

                        }
                    }

                    int confirmation = JOptionPane.showConfirmDialog(null, "Pretende guardar estas alterações?", "Confirmação", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    // 0=yes, 1=no, 2=cancel
                    if (confirmation == 0) {
                        conn.commit();
                    } else {
                        conn.rollback();
                    }

                    conn.setAutoCommit(true);

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                try {
                    conn.setAutoCommit(true);
                } catch (Exception ex) {}


                close(id, ref);
                new Menu();
                frame.dispose();

            }
        });
    }

    public void close(int id, String ref) {
        Statement stmt = null;

        try {

            stmt = conn.createStatement();
            stmt.executeUpdate("Insert Into LogOperations (EventType, Objecto, Valor, Referencia) " +
                    " Values ('O', '"+ id +"', SYSDATETIME(), '" + ref + "')");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }


}
