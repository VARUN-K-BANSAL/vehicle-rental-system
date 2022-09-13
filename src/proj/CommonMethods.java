package proj;

import java.sql.SQLException;

public interface CommonMethods {
     void createTable();
     void takeFromDatabase() throws SQLException;
     void deleteFromDatabase(String id) throws SQLException;
}
