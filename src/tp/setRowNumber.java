package tp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class setRowNumber {
    private JTextField nRowsTextField;
    private JButton verButton;
    private JPanel panel;

    public setRowNumber() {

        System.out.println("Number of rows");

        JFrame frame = new JFrame("Numero de linhas");
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

        verButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try {

                    int number = Integer.parseInt(nRowsTextField.getText());
                    new Log(number);
                    frame.dispose();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Deve introduzir um numero inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
    }

}
