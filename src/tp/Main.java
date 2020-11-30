package tp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    static Connection connection;
    static int menusOpened = 0;

    static int transactionIsolationLevel = 0;

    static String hostname;
    static String database;
    static String username;
    static String password;

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

    public static void main(String[] args) {

        new App();


    }

}
