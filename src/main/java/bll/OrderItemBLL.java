package bll;

import dao.OrderItemDAO;
import model.OrderItem;
import java.util.List;

/**
 * used to manage the order item related operations
 * intermediary level between the data layer and the presentation layer
 */
public class OrderItemBLL {

    private OrderItemDAO orderItemDAO;

    /**
     * Creates a new order item and initializes the order that contains it
     */
    public OrderItemBLL() {
        this.orderItemDAO = new OrderItemDAO();
    }

    /**
     * finds an order item by its id
     * @param orderId the unique id in the database
     * @return the order item with the selected id
     */
    public List<OrderItem> findItemsByOrderId(int orderId) {
        return orderItemDAO.findItemsByOrderId(orderId);
    }
}