package database;

import shelter.database.util.DatabaseConnection;

import java.sql.SQLException;

public class TestUtils {
//    private final DatabaseConnection databaseConnection;


    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        testCreation();
    }

    private static void testCreation() throws ClassNotFoundException, SQLException {
        DatabaseConnection.connection("src/test/resources/test.db");
        DatabaseConnection.createNewDatabase();
        DatabaseConnection.createSchema();
    }

}
