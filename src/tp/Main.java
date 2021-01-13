package tp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    static Connection connection;

    static int menusOpened = 0;

    static String hostname;
    static String database;
    static String username;
    static String password;

    static String[] isolationLevels = {"READ UNCOMMITTED", "READ COMMITTED", "REPEATABLE READ", "SERIALIZABLE"};

    public static Connection getNewConnection() {

        String connectionUrl = "jdbc:sqlserver://" +hostname+ ":1433;"
                        + "database=" + database + ";"
                        + "user=" + username + ";"
                        + "password=" + password + ";"
                        + "encrypt=true;"
                        + "trustServerCertificate=true;"
                        + "loginTimeout=30;";


        Connection con = null;

        try {

            con = DriverManager.getConnection(connectionUrl);

            System.out.println("Connection successful!");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return con;

    }

    public static void setIsolationLevel(Connection connection, int isolation) {

        try {

            Statement stmt = connection.createStatement();

            if (isolation == 0) {
                stmt.executeUpdate("SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;");
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            } else if (isolation == 1) {
                stmt.executeUpdate("SET TRANSACTION ISOLATION LEVEL READ COMMITTED;");
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            } else if (isolation == 2) {
                stmt.executeUpdate("SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;");
                connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            } else if (isolation == 3) {
                stmt.executeUpdate("SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;");
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) {

        new App();

    }

}
