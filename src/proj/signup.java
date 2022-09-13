package proj;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class signup extends JFrame {
    private JPanel panel1;
    private JTextField textField1;
    private JTextField textField3;
    private JTextField textField5;
    private JButton ENTER;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JTextField textField2;
    public static int i=0;
    public signup()  {
        setSize(1000,1000);
        setContentPane(panel1);
        setDefaultCloseOperation(signup.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
        ENTER.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = textField1.getText();
                String name = textField3.getText();
                String employee = textField2.getText().toUpperCase(Locale.ROOT);
                String isEmployee = "false";
                String password = passwordField1.getText();
                String number = textField5.getText();
                System.out.println(employee);
                if("YES".equals(employee)) {
                    isEmployee = "true";
                    System.out.println(isEmployee);
                }
                if (username == "" || name == "" || password == "" || number == "") {
                    JOptionPane.showMessageDialog(null, "inputs can't be blank");
                } else {
                    Boolean checkUseridPresent = false;
                    try {
                        checkUseridPresent = checkUserid(username);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    if (checkUseridPresent == true) {
                        JOptionPane.showMessageDialog(null, "userName exist choose other!");
                    } else {
                        try {
                            System.out.println(isEmployee);
                            addUser(username, password, name, number, isEmployee);
                            JOptionPane.showMessageDialog(null, "userName created successfully");
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                }
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new login().setSize(800,400);
            }
        });
    }
    public  void addUser(String userId,String password,String name,String number, String isEmployee) throws SQLException {
        String sql = "insert into userlogin values('" + userId + "','" + password + "','" + number + "','" + name + "','" + isEmployee + "')";
        System.out.println(sql);
        Connection connection = proj.connection.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }
    public  boolean checkUserid(String userId) throws SQLException {
        Connection connection= proj.connection.getConnection();
        Statement statement=connection.createStatement();
        String sql = "select * from userlogin where username='" + userId + "'";
        ResultSet resultSet = statement.executeQuery(sql);
        if (!resultSet.next()) {
            return false;
        }
        else
            return true;
    }
}
