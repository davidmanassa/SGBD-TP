package tp;

import javax.swing.*;

public class App {
    private JButton okButton;
    private JButton cancelButton;
    private JTextField hostName;
    private JTextField databaseName;
    private JTextField username;
    private JTextField password;
    private JPanel mainPanel;

    public App() {

        JFrame frame = new JFrame("Login...");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

}
