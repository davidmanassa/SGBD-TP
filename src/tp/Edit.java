package tp;

import com.sun.org.apache.bcel.internal.ExceptionConst;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Edit {
    private JTextField idEncomenda;
    private JButton editarButton;
    private JButton cancelButton;
    private JPanel panel;

    Connection conn;

    Statement stmt = null;
    ResultSet rs = null;


    public Edit() {

        conn = Main.getNewConnection();


        System.out.println("Edit");

        JFrame frame = new JFrame("Indique o ID da encomenda");
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

                int id = Integer.parseInt(idEncomenda.getText());

                try {
/**
                    stmt = conn.createStatement();
                    rs = stmt.executeQuery("SELECT COUNT(NumReg) FROM LogOperations WHERE EventType = 'O' AND Objecto = '" + id + "';");

                    rs.next();
                    int rows = rs.getInt(1);

                    if (rows % 2 == 0) { // It's even!!!
**/
                        frame.setVisible(false);
                        new EditEncomenda(id);
/**
                    } else {

                        JOptionPane.showMessageDialog(frame,
                                "Alguem está a editar esta encomenda.",
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);

                    }
**/
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });

    }
}
