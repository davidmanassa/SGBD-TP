package tp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    private HashMap<String, Integer> initialQuantities;
    String initialAddress;

    public EditEncomenda(int id) {

        System.out.println("Edit encomenda");

        JFrame frame = new JFrame("Editar encomenda");
        frame.setContentPane(panel);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        Statement stmt = null;
        ResultSet rs = null;

        // ----------------- REGISTO DE ENTRADA
        LocalDateTime datetime = LocalDateTime.now();
        DateTimeFormatter dateFormatted = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String ref = "G1_" + dateFormatted.format(datetime);

        try {

            stmt = Main.connection.createStatement();
            stmt.executeUpdate("Insert Into LogOperations (EventType, Objecto, Valor, Referencia) " +
                    "Values ('O', '"+ id +"', '" + System.currentTimeMillis() + "', '" + ref + "')");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // ----------------- REGISTO DE FECHO
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {

                Statement stmt = null;
                ResultSet rs = null;

                try {
                    LocalDateTime datetime1 = LocalDateTime.now();

                    stmt = Main.connection.createStatement();
                    stmt.executeUpdate("Insert Into LogOperations (EventType, Objecto, Valor, Referencia) " +
                            " Values ('O', '"+ id +"', '" + System.currentTimeMillis() + "', '" + ref + "')");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                frame.setVisible(false);
                new Menu();

            }
        });

        // ----------------- LEITURA DE DADOS
        try {
            stmt = Main.connection.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Encomenda WHERE EncID="+id+";");
            while (rs.next()) {

                String Nome = rs.getString("Nome");
                String Morada = rs.getString("Morada");

                clientName.setText(Nome);
                moradaTextField.setText(Morada);

                initialAddress = Morada;

            }

            stmt = Main.connection.createStatement();
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
                Statement stmt = null;
                ResultSet rs = null;
                try {

                    if (!initialAddress.equals(moradaTextField.getText())) {
                        stmt = Main.connection.createStatement();
                        stmt.executeUpdate("UPDATE Encomenda SET Morada = '" + moradaTextField.getText() + "' WHERE EncID = " + id + ";");
                        System.out.println("Updating address to: " + moradaTextField.getText());
                    }

                    for (int i = 0; i < initialQuantities.size(); i++) {
                        int newQuantity = Integer.parseInt(productTable.getValueAt(i, 1).toString());
                        String product = productTable.getValueAt(i, 0).toString();

                        if (initialQuantities.get(product) != newQuantity) {

                            System.out.println("initial quantity of " + product + ": " + initialQuantities.get(product) + " updated: " + newQuantity);

                            stmt = Main.connection.createStatement();
                            stmt.executeUpdate("UPDATE EncLinha SET Qtd = '" + newQuantity + "' WHERE EncID = " + id + " AND Designacao = '" + product + "';");
                        }
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                new Menu();
                frame.setVisible(false);

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
