package proj;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

public class UpdateVehicles extends JFrame implements CommonMethods{
    private JPanel panel1;
    private JTable table1;
    private JTextField textField1;
    private JButton deleteButton;
    private JTextField textField2;
    private JTextField textField5;
    private JComboBox comboBox1;
    private JSpinner spinner1;
    private JComboBox comboBox2;
    private JButton insertButton;
    private JButton editPriceButton;
    private JButton editAvailablityButton;
    private JTextField textField6;
    private JTextField textField7;
    private JTextField textField3;
    private JSpinner spinner2;
    private JTextField textField4;

    public UpdateVehicles() throws SQLException {
        super.setContentPane(panel1);
        super.setDefaultCloseOperation(UpdateVehicles.DISPOSE_ON_CLOSE);
        super.pack();
        super.setVisible(true);
        createTable();
        takeFromDatabase();
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 String id = textField2.getText();
                 String name = textField4.getText();
                 int capacity = Integer.parseInt((String) comboBox1.getSelectedItem());
                 int price = Integer.parseInt(textField5.getText());
                 int available = (Integer)spinner1.getValue();
                try {
                    addToDatabase(id,name,capacity,price,available);
                    JOptionPane.showMessageDialog(null,"Vehicle added successfully");

                } catch (SQLException throwables) {
                    JOptionPane.showMessageDialog(null,"Please recheck the data.");
                    throwables.printStackTrace();
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = textField1.getText();
                try {
                    deleteFromDatabase(id);
                    JOptionPane.showMessageDialog(null,"Vehicles Removed!");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        editPriceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = textField7.getText();
                int price = Integer.parseInt(textField3.getText());
                try {
                    updatePrice(id, price);
                    JOptionPane.showMessageDialog(null,"price updated");
                } catch (SQLException throwables) {
                    JOptionPane.showMessageDialog(null,"Cannot update the price. Some error occurred");
                    throwables.printStackTrace();
                }
            }
        });
        editAvailablityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = textField6.getText();
                int available = (Integer)spinner2.getValue();
                try {
                    updateAvailablity(id,available);
                    JOptionPane.showMessageDialog(null,"updated availability");
                } catch (SQLException throwables) {
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
    public void addToDatabase(String id,String name,int capacity,int price,int availability) throws SQLException {
        String sql = "insert into vehicles values('"+name+"','"+capacity+"','"+availability+"','"+id+"','"+price+"')";
        //System.out.println(sql);
        Connection connection= proj.connection.getConnection();
        Statement statement=connection.createStatement();
        statement.executeUpdate(sql);
        DefaultTableModel model=(DefaultTableModel)table1.getModel();
        model.addRow(new String[]{id,name,Integer.toString(capacity),Integer.toString(price),Integer.toString(availability)});
    }
    public void deleteFromDatabase(String id) throws SQLException {
        Connection connection= proj.connection.getConnection();
        Statement statement=connection.createStatement();
        String sql = "delete from vehicles where id=" + id;
        statement.executeUpdate(sql);
        DefaultTableModel model = (DefaultTableModel)table1.getModel();
        for(int i=0;i<model.getRowCount();i++) {
            String ak= (String) model.getValueAt(i,0);
            if(ak.equals(id)){
                model.removeRow(i);
                break;
            }
        }
    }
    private  void updatePrice(String id,int price) throws SQLException
    {
        Connection connection= proj.connection.getConnection();
        Statement statement=connection.createStatement();
        String sql = "update vehicles set price= "+price+" where id= " + id ;
        statement.executeUpdate(sql);
        DefaultTableModel model=(DefaultTableModel)table1.getModel();
        for(int i=0;i<model.getRowCount();i++) {
            String ak= (String) model.getValueAt(i,0);
            if(ak.equals(id)){
               model.setValueAt(Integer.toString(price),i,3);
                break;
            }
        }
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