package proj;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Dashboard_user extends JFrame {
    private JButton RENTVEHICLEButton;
    private JPanel panel1;
    private JButton VEHICLEHISTORYButton;
    private JButton LOGOUTButton;
    public Dashboard_user() {
        setContentPane(panel1);
        setDefaultCloseOperation(Dashboard_user.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        RENTVEHICLEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new RentVehicle().setSize(1500,800);;
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        VEHICLEHISTORYButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new customerHistory().setSize(1200,800);;
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        LOGOUTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"LOGOUT SUCCESSFUL");
                Variables.userName = "";
                setVisible(false);
                new login().setSize(800,400);
            }
        });
    }
}
