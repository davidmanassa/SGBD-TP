package tp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    private JButton cancelButton;
    private JButton okButton;
    private JTextField hostName;
    private JTextField databaseName;
    private JTextField username;
    private JPanel mainPanel;
    private JPasswordField password;

    public App() {

        System.out.println("App");

        JFrame frame = new JFrame("Login...");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String connectionUrl =
                        "jdbc:sqlserver://" +hostName.getText()+ ":1433;"
                                + "database=" + databaseName.getText() + ";"
                                + "user=" + username.getText() + ";"
                                + "password=" + password.getText() + ";"
                                + "encrypt=true;"
                                + "trustServerCertificate=true;"
                                + "loginTimeout=30;";

                Main.hostname = hostName.getText();
                Main.database = databaseName.getText();
                Main.username = username.getText();
                Main.password = password.getText();

                try {

                    Connection connection = DriverManager.getConnection(connectionUrl);

                    Main.connection = connection;

                    System.out.println("Connection successful!");

                    //frame.setVisible(false);
                    //new IsolationLevel();
                    new Menu();

                    frame.dispose();

                }
                // Handle any errors that may have occurred.
                catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

}
