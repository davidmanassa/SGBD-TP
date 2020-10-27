package tp;

import javax.swing.*;

public class App {
    private JButton cancelButton;
    private JButton okButton;
    private JTextField hostName;
    private JTextField databaseName;
    private JTextField username;
    private JPanel mainPanel;
    private JPasswordField password;

    public App() {

        JFrame frame = new JFrame("Login...");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

}
