package Persistence.SQL;

import java.sql.*;

/**
 * The SQLConnector class will abstract the specifics of the connection to a MySQL database.
 * <p>
 * This class follows the Singleton design pattern to facilitate outside access while maintaining
 * a single instance, as having multiple connectors to a database is generally discouraged.
 * <p>
 * Be aware that this class presents a simplified approach. Configuration parameters SHOULD NOT be
 * hardcoded and the use of Statements COULD be replaced by PreparedStatements to avoid SQL Injection.
 *
 * @author Àlex Ferre, Aroa García, Marti Rebollo, Sandra Corral y Sami Amin
 * @version 1.0
 */
public final class SQLConnector {

    // The static attribute to implement the singleton design pattern.
    private static SQLConnector instance = null;
    // Attributes to connect to the database.
    private final String username;
    private final String password;
    private final String url;
    private Connection conn;

    /**
     * Constructor to start the database call with the data obtained from the confi.json file.
     *
     * @param username User Name
     * @param password Password
     * @param ip       IP
     * @param port     Port
     * @param database Database
     */
    private SQLConnector(String username, String password, String ip, int port, String database) {
        this.username = username;
        this.password = password;
        this.url = "jdbc:mysql://" + ip + ":" + port + "/" + database;
    }

    /**
     * Static method that returns the shared instance managed by the singleton.
     *
     * @return The shared SQLConnector instance.
     */
    public static SQLConnector getInstance() {
        ConnectorJson connectorJson = new ConnectorJson();
        String[] dataJson = connectorJson.readPlayer();
        String username = dataJson[0];
        String password = dataJson[1];
        String ip = dataJson[2];
        int port = Integer.parseInt(dataJson[3]);
        String databas = dataJson[4];

        if (instance == null) {
            // NOT a good practice to hardcode connection data! Be aware of this for your project delivery ;)
            instance = new SQLConnector(username, password, ip, port, databas);
            instance.connect();
        }
        return instance;
    }

    /**
     * Method that starts the inner connection to the database. Ideally, users would disconnect after
     * using the shared instance.
     */
    private void connect() {
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.err.println("Couldn't connect to --> " + url + " (" + e.getMessage() + ")");
        }
    }

    /**
     * Method that executes an insertion query to the connected database.
     *
     * @param query String representation of the query to execute.
     */
    public void insertQuery(String query) {
        try {
            Statement s = conn.createStatement();
            s.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println(query);
            System.err.println("Problem when inserting --> " + e.getSQLState() + " (" + e.getMessage() + ")");
        }
    }

    /**
     * Method that executes an update query to the connected database.
     *
     * @param query String representation of the query to execute.
     */
    public void updateQuery(String query) {
        try {
            Statement s = conn.createStatement();
            s.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println(query);
            System.err.println("Problema when updating --> " + e.getSQLState() + " (" + e.getMessage() + ")");
        }
    }

    /**
     * Method that executes a deletion query to the connected database.
     *
     * @param query String representation of the query to execute.
     */
    public void deleteQuery(String query) {
        try {
            Statement s = conn.createStatement();
            s.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println(query);
            System.err.println("Problem when deleting --> " + e.getSQLState() + " (" + e.getMessage() + ")");
        }
    }

    /**
     * Method that executes a selection query to the connected database.
     *
     * @param query String representation of the query to execute.
     * @return The results of the selection.
     */
    public ResultSet selectQuery(String query) {
        ResultSet rs = null;
        try {
            Statement s = conn.createStatement();
            rs = s.executeQuery(query);
        } catch (SQLException e) {
            System.err.println(query);
            System.err.println("Problem when selecting data --> " + e.getSQLState() + " (" + e.getMessage() + ")");
        }
        return rs;
    }

    /**
     * Method that closes the inner connection to the database. Ideally, users would disconnect after
     * using the shared instance.
     */
    public void disconnect() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.err.println("Problem when closing the connection --> " + e.getSQLState() + " (" + e.getMessage() + ")");
        }
    }
}
