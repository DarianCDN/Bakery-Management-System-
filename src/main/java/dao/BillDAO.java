package dao;

import connection.ConnectionFactory;
import model.Bill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Manages Bill entities.
 * Extends AbstarctDAO but overides some of the CRUD operations to mantain immutability
 */
public class BillDAO extends AbstractDAO<Bill> {

    /**
     * constructs a BillDAO instance
     */
    public BillDAO() {
        super();
    }

    /**
     * inserts a new bill in the database log
     * @param bill the bill to be inserted
     * @return a bill object with the id from the database
     */
    @Override
    public Bill insert(Bill bill) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "INSERT INTO bill (order_id, client_id, total_price, date, address) VALUES (?, ?, ?, ?, ?)";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, bill.orderId());
            statement.setInt(2, bill.clientId());
            statement.setDouble(3, bill.totalPrice());
            statement.setTimestamp(4, new java.sql.Timestamp(bill.date().getTime()));
            statement.setString(5, bill.address());

            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int insertedId = rs.getInt(1);
                return new Bill(insertedId, bill.orderId(), bill.clientId(), bill.totalPrice(), bill.date(), bill.address());
            }

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "BillDAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return bill;
    }

    /**
     * retrieves all the bills generated
     * @return a list with all the Bill items
     */
    @Override
    public List<Bill> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Bill> bills = new ArrayList<>();
        String query = "SELECT * FROM bill";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                bills.add(new Bill(
                        resultSet.getInt("id"),
                        resultSet.getInt("order_id"),
                        resultSet.getInt("client_id"),
                        resultSet.getDouble("total_price"),
                        resultSet.getTimestamp("date"),
                        resultSet.getString("address")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "BillDAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return bills;
    }

    /**
     * Overrides the default update to prevent it
     * @param bill the Bill attempting to be updated
     * @return it never reaches this point
     * @throws UnsupportedOperationException Always throws this to prevent updating a bill
     */
    @Override
    public Bill update(Bill bill) {
        throw new UnsupportedOperationException("Bills cannot be updated!");
    }

    /**
     * Overrides the default method to prevent it
     * @param id the identifier of the object to be deleted
     * @throws UnsupportedOperationException always throws this to prevent deletion
     */
    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Bills cannot be deleted!");
    }
}