package tp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Menu {

    private JButton editButton;
    private JButton browserButton;
    private JButton logTempoButton;
    private JButton logButton;
    private JPanel panel;

    public Menu() {

        if (Main.menusOpened == 0) {

            Main.menusOpened = 1;

            System.out.println("Menu");

            JFrame frame = new JFrame("Menu");
            frame.setContentPane(panel);
            // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    frame.dispose();
                    Main.menusOpened = 0;
                }
            });
            frame.pack();
            frame.setVisible(true);

            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    new Edit();

                }
            });

            browserButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    new Browser();

                }
            });

            logTempoButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    new TimeLog();

                }
            });

            logButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    new setRowNumber();

                }
            });
        }
    }
}
