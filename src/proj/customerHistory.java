package proj;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class customerHistory extends JFrame{
    private JTable table1;
    private JPanel panel1;
    private JLabel sumLabel;
    public customerHistory() throws SQLException {
        setContentPane(panel1);
        setDefaultCloseOperation(customerHistory.DISPOSE_ON_CLOSE);
        pack();
        setSize(1400, 800);
        setVisible(true);
        createTable();
        takeFromDatabase();
    }
   public void createTable() {
        table1.setModel(new DefaultTableModel(
                null, new String[]{"ID", "NAME", "CAPACITY", "PRICE", "BOOKING DATE", "QUANTITY"}
        ));
    }
    public void takeFromDatabase() throws SQLException {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        Connection connection = proj.connection.getConnection();
        Statement statement = connection.createStatement();
        String sql = "select * from vehicles";
        ResultSet vehicles = statement.executeQuery(sql);
        String sqlcmd = "select * from rented";
        Statement statement1 = connection.createStatement();
        ResultSet history = statement1.executeQuery(sqlcmd);
        int sum=0;
        while (history.next()) {
            while(vehicles.next()) {
                if(vehicles.getString("id").equals(history.getString("vehicle"))) {
                    model.addRow(new String[]{vehicles.getString("id"), vehicles.getString("name"), Integer.toString(vehicles.getInt("capacity")), Integer.toString(vehicles.getInt("price")), history.getDate("date").toString(), Integer.toString(history.getInt("quantity"))});
                    sum += vehicles.getInt("price") * history.getInt("quantity");
                    break;
                }
            }

        }
        sumLabel.setText("Total Cost: Rs "+ Integer.toString(sum) +" only");
    }
}
