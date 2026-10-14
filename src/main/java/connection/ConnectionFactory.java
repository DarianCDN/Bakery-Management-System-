package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Technical University of Cluj-Napoca, Romania Distributed Systems
 *          Research Laboratory
 * Source: http://dsrl.coned.utcluj.ro/
 * @since  Apr 03, 2017
 */
public class ConnectionFactory {

	private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
	private static final String DRIVER = "org.postgresql.Driver";
    private static final String DBURL = "jdbc:postgresql://localhost:5432/assignment3";
    private static final String USER = "postgres";
	private static final String PASS = "dary112233";

	private static ConnectionFactory singleInstance = new ConnectionFactory();

    /**
     * Private constructor to prevent external instantiations
     * Loads the PostgreSQL and JDBC driver
     */

	private ConnectionFactory() {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

    /**
     * Creates a new connections
     * @return the Conncetion object
     */
	private Connection createConnection() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(DBURL, USER, PASS);
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, "An error occured while trying to connect to the database");
			e.printStackTrace();
		}
		return connection;
	}

    /**
     * retrieves the connection
     * @return the conection object
     */
	public static Connection getConnection() {
		return singleInstance.createConnection();
	}


    /**
     * Closes the database connection
     * @param connection the connection to be closed
     */
	public static void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.log(Level.WARNING, "An error occured while trying to close the connection");
			}
		}
	}

    /**
     * closes an sql statement
     * @param statement the statement to be closed
     */
	public static void close(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				LOGGER.log(Level.WARNING, "An error occured while trying to close the statement");
			}
		}
	}


    /**
     * closes a database return set
     * @param resultSet the result set to be closed
     */
	public static void close(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				LOGGER.log(Level.WARNING, "An error occured while trying to close the ResultSet");
			}
		}
	}
}
