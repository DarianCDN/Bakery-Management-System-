package dao;

import connection.ConnectionFactory;
import model.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
/**
 * Data Access Object that handles OrderItem entities
 * Extends AbstractDAO to inherit CRUD functionalities, but also provides methods for listing the order items
 */
public class OrderItemDAO extends AbstractDAO<OrderItem> {
    /**
     * retrieves all the items of an order
     * @param orderId the order id whose order items need to be retrieved
     * @return a list of OrderItem objects belonging to the given order
     */
    public List<OrderItem> findItemsByOrderId(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM orderitem WHERE orderid = ?";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, orderId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                items.add(new OrderItem(
                        resultSet.getInt("id"),
                        resultSet.getInt("orderid"),
                        resultSet.getInt("productid"),
                        resultSet.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "OrderItemDAO:findItemsByOrderId " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return items;
    }
}