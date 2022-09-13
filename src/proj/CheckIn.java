package proj;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CheckIn extends JFrame {
    private JButton CHECKINButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JTextField textField4;
    private JPanel panel1;
    public CheckIn() throws SQLException {
        setContentPane(panel1);
        setDefaultCloseOperation(CheckIn.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
        CHECKINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name=textField1.getText();
                int age=Integer.parseInt(textField2.getText());
                String address=textField3.getText();
                String hotel=(String) comboBox1.getSelectedItem();
                String roomType=(String) comboBox2.getSelectedItem();
                String phoneNumber=textField4.getText();

                try {
                    if(!checkAvailable(hotel, roomType))
                        JOptionPane.showMessageDialog(null,"Room Not Available Select Other!");
                    else{
                        addCustomer(name,age,address,hotel,roomType,phoneNumber);
                        dispose();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    private boolean checkAvailable(String hotel,String roomType) throws SQLException {
        Connection connection= proj.connection.getConnection();
        Statement statement=connection.createStatement();
        String sql = "select sum(available) as sum from rooms where type='" + roomType + "' and hotel='"+hotel+"'";
        ResultSet resultSet = statement.executeQuery(sql);

        if (resultSet.next()) {
            System.out.println(resultSet.getInt("sum"));
            if(resultSet.getInt("sum")>0)
                return true;
            else
                return false;
       }
        else
            return false;
   }
   private  void addCustomer(String name,int age,String address,String hotel,String roomType,String phoneNumber) throws SQLException {
       Connection connection= proj.connection.getConnection();
       Statement statement=connection.createStatement();
       String sql = "insert into checkin(age,address,hotel,roomtype,date,number,name) values('"+age+"','"+address+"','"+hotel+"','"+roomType+"',"+"curdate()"+",'"+phoneNumber+"','"+name+"')";
       statement.executeUpdate(sql);
       JOptionPane.showMessageDialog(null,"customer checked in successfully");
       String sql1="update rooms set available=available-1 where type='" + roomType + "' and hotel='"+hotel+"'";
       statement.executeUpdate(sql1);
   }
}
