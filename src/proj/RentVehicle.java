package proj;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class RentVehicle extends JFrame{
    private JPanel panel1;
    private JTable table1;
    private JTextField textField1;
    private JButton deleteButton;
    private JTextField textField2;
    private JTextField textField5;
    private JComboBox comboBox1;
    private JSpinner spinner1;
    private JComboBox comboBox2;
    private JButton rentButton;
    private JButton editPriceButton;
    private JButton editAvailablityButton;
    private JTextField textField6;
    private JTextField textField7;
    private JTextField textField3;
    private JSpinner spinner2;
    private JTextField textField4;

    public RentVehicle() throws SQLException {
        super.setContentPane(panel1);
        super.setDefaultCloseOperation(UpdateVehicles.DISPOSE_ON_CLOSE);
        super.pack();
        super.setVisible(true);
        createTable();
        takeFromDatabase();
        rentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 String id = textField2.getText();
                 int quantity = (Integer)spinner1.getValue();
                try {
                    if(rentVehicle(id, quantity)) {
                        JOptionPane.showMessageDialog(null,"Vehicle rented successfully");
                    } else {
                        JOptionPane.showMessageDialog(null,"Sorry, we don't have this much of vehicles.");
                    }
                } catch (SQLException throwables) {
                    JOptionPane.showMessageDialog(null,"Please recheck the data.");
                    throwables.printStackTrace();
                }
            }
        });
    }

    public void createTable()
    {
        table1.setModel(new DefaultTableModel(
                null,new String[]{"VEHICLE ID","NAME","CAPACITY","PRICE/HOUR","AVAILABLE"}
        ));
    }

   public void takeFromDatabase() throws SQLException {
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        DefaultTableModel model=(DefaultTableModel)table1.getModel();
        Connection connection= proj.connection.getConnection();
        Statement statement=connection.createStatement();
        String sql = "select * from vehicles";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
           vehicles.add(new Vehicle(resultSet.getString("id"),resultSet.getString("name"),resultSet.getInt("capacity"),resultSet.getInt("price"),resultSet.getInt("quantity")));
        }
       for (Vehicle vehicle : vehicles) {
           model.addRow(new String[]{vehicle.getId(), vehicle.getName(), Integer.toString(vehicle.getCapacity()), Integer.toString(vehicle.getPrice()), Integer.toString(vehicle.getAvailable())});
       }
    }

    public boolean rentVehicle(String id, int quantity) throws SQLException {
        String sql = "SELECT * FROM vehicles where id = "+id+";";
        //System.out.println(sql);
        Connection connection= proj.connection.getConnection();
        Statement statement=connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
           if(resultSet.getInt("quantity") >= quantity) {
               updateAvailablity(id, resultSet.getInt("quantity") - quantity);
               updateRented(id, Variables.userName, quantity);
               return true;
           }
        }
        return false;
    }

    private void updateRented(String id, String userName, int quantity) throws SQLException {
        Connection connection= proj.connection.getConnection();
        Statement statement=connection.createStatement();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        String sql = "INSERT INTO rented VALUES ('"+userName + "', '" + id + "', '" + quantity + "', '"+ date + "');";
        statement.executeUpdate(sql);
    }

    private void updateAvailablity(String id,int available)throws SQLException
    {
        Connection connection= proj.connection.getConnection();
        Statement statement=connection.createStatement();
        String sql = "update vehicles set quantity= "+available+" where id= " + id ;
        statement.executeUpdate(sql);
        DefaultTableModel model=(DefaultTableModel)table1.getModel();
        for(int i=0;i<model.getRowCount();i++) {
            String ak= (String) model.getValueAt(i,0);
            if(ak.equals(id)){
                model.setValueAt(Integer.toString(available),i,4);
                break;
            }
        }
    }
}