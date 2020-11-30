package tp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class IsolationLevel {

    private JButton readUncommittedButton;
    private JButton readCommittedButton;
    private JButton repeatableReadButton;
    private JButton serializableButton;
    private JPanel panel;

    public void setIsolationLevel(int isolation) {

        try {

            Statement stmt = Main.connection.createStatement();

            if (isolation == 0) {
                stmt.executeUpdate("SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;");
                Main.connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            } else if (isolation == 1) {
                stmt.executeUpdate("SET TRANSACTION ISOLATION LEVEL READ COMMITTED;");
                Main.connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            } else if (isolation == 2) {
                stmt.executeUpdate("SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;");
                Main.connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            } else if (isolation == 3) {
                stmt.executeUpdate("SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;");
                Main.connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public IsolationLevel() {

        System.out.println("Isolation Level");

        JFrame frame = new JFrame("Set transaction isolation level");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        readUncommittedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setIsolationLevel(0);

                frame.setVisible(false);
                new Menu();

            }
        });

        readCommittedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setIsolationLevel(1);

                frame.setVisible(false);
                new Menu();

            }
        });

        repeatableReadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setIsolationLevel(2);

                frame.setVisible(false);
                new Menu();

            }
        });

        serializableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setIsolationLevel(3);

                frame.setVisible(false);
                new Menu();

            }
        });

    }


}
