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

    private static Runnable startEdit = new Runnable() {
        public void run() {
            new Edit();
        }
    };

    private static Runnable startBrowser = new Runnable() {
        public void run() {
            new Browser();
        }
    };

    private static Runnable startLog = new Runnable() {
        public void run() {
            new setRowNumber();
        }
    };

    private static Runnable startLogTempo = new Runnable() {
        public void run() {
            new TimeLog();
        }
    };

    public Menu() {

        if (Main.menusOpened == 0) {

            Main.menusOpened = 1;

            System.out.println("Menu");

            JFrame frame = new JFrame("Menu");
            frame.setContentPane(panel);
            // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    Main.menusOpened = 0;
                    frame.dispose();
                }
            });
            frame.pack();
            frame.setVisible(true);

            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    new Thread(startEdit).start();

                }
            });

            browserButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    new Thread(startBrowser).start();

                }
            });

            logTempoButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    new Thread(startLogTempo).start();

                }
            });

            logButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    new Thread(startLog).start();

                }
            });
        }
    }
}
