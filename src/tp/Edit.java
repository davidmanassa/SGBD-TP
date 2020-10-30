package tp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Edit {
    private JTextField idEncomenda;
    private JButton editarButton;
    private JButton cancelButton;
    private JPanel panel;

    public Edit() {

        System.out.println("Edit");

        JFrame frame = new JFrame("Indique o ID da encomenda");
        frame.setContentPane(panel);
        // frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                frame.setVisible(false);
                new Menu();

            }
        });

        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                frame.setVisible(false);
                new EditEncomenda(Integer.parseInt(idEncomenda.getText()));

            }
        });

    }
}
