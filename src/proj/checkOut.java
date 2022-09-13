package proj;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class checkOut extends JFrame {
    private JPanel panel1;
    private JTable table1;
    private JTextField textField1;
    private JButton GetBillButton;
    private JTextField textField2;
    private JButton CHECKOUTButton;
    private JTextField textField3;
    int balance;
    public checkOut() throws SQLException {
        setContentPane(panel1);
        setDefaultCloseOperation(checkOut.DISPOSE_ON_CLOSE);
        pack();
        setSize(1400,800);
        setVisible(true);
        createTable();
        GetBillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idd = textField3.getText();
                String vid = textField1.getText();
                int bill = -1;
                try {
                    bill = getBill(idd, vid);
                    if (bill==-1)
                        JOptionPane.showMessageDialog(null,"Invalid id");
                    else{
                        balance = bill;
                        textField2.setText(Integer.toString(bill));
                    }
                } catch (SQLException throwables) {
                    JOptionPane.showMessageDialog(null,"SOME ERROR OCCURRED, PLEASE TRY AGAIN LATER");
                    throwables.printStackTrace();
                }
            }
        });
        CHECKOUTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String curr_date = dtf.format(now);
                String user = textField3.getText();
                String vehicle = textField1.getText();
                Connection connection= null;
                try {
                    connection = proj.connection.getConnection();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                Statement statement= null;
                try {
                    statement = connection.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                String sql = "select * from rented where user = '"+ user +"' and vehicle = '" + vehicle + "';";
                System.out.println(sql);
                try {
                    ResultSet resultSet = statement.executeQuery(sql);
                    String userName = null, vid = null, bookDate = null;
                    int quantity = 0;
                    while(resultSet.next())
                    {
                        userName = resultSet.getString("user");
                        vid = resultSet.getString("vehicle");
                        bookDate = resultSet.getDate("date").toString() + " " + resultSet.getTime("date").toString();
                        quantity = resultSet.getInt("quantity");
                    }
                    String sql1 = "update vehicles set quantity = quantity + " + quantity + " where id = '" + vid + "';";
                    statement.executeUpdate(sql1);
                    deleteFromDatabase(userName, vid);
                    String sql3 = "insert into history values('"+userName+"' , "+ vid +" ,'" + bookDate +"','"+ curr_date +"',"+ balance +")";
                    statement.executeUpdate(sql3);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                JOptionPane.showMessageDialog(null,"Check-Out Successful Bill received!");
                dispose();
            }
        });
    }

    public int dateDifference(String start, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date d1 = null, d2 = null;
        try {
            d1 = sdf.parse(start);
            d2 = sdf.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long difference = d2.getTime() - d1.getTime();
        long difference_In_Hours = (difference / (1000 * 60 * 60)) % 24;
        return (int) Math.floor(difference_In_Hours);
    }

    public int getAmount(int a, int b, int c) {
        return a * b * c;
    }

    public void createTable()
    {
        table1.setModel(new DefaultTableModel(
                null,new String[]{"CUSTOMER USERNAME", "VEHICLE ID", "NAME", "PRICE/HOUR", "QUANTITY", "BOOKING DATE", "AMOUNT"}
        ));
    }

    public int getBill(String id, String vid) throws SQLException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String curr_date = dtf.format(now);
        DefaultTableModel model=(DefaultTableModel)table1.getModel();
        Connection connection= proj.connection.getConnection();
        Statement statement=connection.createStatement();
        String sql = "select * from rented where user = '" + id + "';";
        ResultSet resultSet = statement.executeQuery(sql);
        Statement statement1 =connection.createStatement();
        String sql1 = "select * from vehicles;";
        ResultSet resultSet1 = statement1.executeQuery(sql1);
        int sum = 0;
        while (resultSet.next()) {
            if(resultSet.getString("vehicle").equals(vid)) {
                while(resultSet1.next()) {
                    if(resultSet1.getString("id").equals(resultSet.getString("vehicle"))) {
                        int amt = getAmount(resultSet1.getInt("price"), resultSet.getInt("quantity"), dateDifference(resultSet.getDate("date").toString() + " " + resultSet.getTime("date").toString(), curr_date));
                        sum += amt;
                        model.addRow(new String[]{resultSet.getString("user"), resultSet1.getString("id"),resultSet1.getString("name"),Integer.toString(resultSet1.getInt("price")), Integer.toString(resultSet.getInt("quantity")), resultSet.getDate("date").toString(), Integer.toString(amt)});
                    }
                }
            }
        }
        return sum;
    }
    public void deleteFromDatabase(String user, String vehicle) throws SQLException {
        Connection connection= proj.connection.getConnection();
        Statement statement=connection.createStatement();
        String sql2 = "delete from rented where user = '" + user + "' and vehicle = '" + vehicle + "';";
        statement.executeUpdate(sql2);
    }
}
