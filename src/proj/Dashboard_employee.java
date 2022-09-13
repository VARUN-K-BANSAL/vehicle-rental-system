package proj;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Dashboard_employee extends JFrame {
    private JButton ADDREMOVEVEHICLESButton;
    private JPanel panel1;
    private JButton CHECKINButton;
    private JButton SUBMITVEHICLEButton;
    private JButton LOGOUTButton;
    public Dashboard_employee() {
        setContentPane(panel1);
        setDefaultCloseOperation(Dashboard_user.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        ADDREMOVEVEHICLESButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new UpdateVehicles().setSize(1500,800);;
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        SUBMITVEHICLEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new checkOut().setSize(1500,800);;
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
