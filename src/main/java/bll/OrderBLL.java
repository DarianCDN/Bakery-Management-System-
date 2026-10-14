package bll;

import dao.BillDAO;
import dao.ClientDAO;
import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.ProductDAO;
import model.Bill;
import model.Client;
import model.Order;
import model.OrderItem;
import model.Product;

import java.util.Date;
import java.util.List;

/**
 * Manages order related operations
 * Acts as an intermediary between the presentation layer and the data access
 */

public class OrderBLL {

    private ProductDAO productDAO;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private BillDAO billDAO;
    private ClientDAO clientDAO;

    /**
     * Constructs a new order
     */
    public OrderBLL(){
        productDAO = new ProductDAO();
        orderDAO = new OrderDAO();
        orderItemDAO = new OrderItemDAO();
        billDAO = new BillDAO();
        clientDAO = new ClientDAO();
    }

    /**
     * Creates a new order and an entry as an immutable Bill
     * @param clientId the client id that places the order
     * @param cart The list of products that the client buys
     * @return The Bill object
     * @throws Exception when the cart is empty, the client is not found or the stock of the product is not large enough
     */
    public Bill placeOrder(int clientId, List<OrderItem> cart) throws Exception {
        if(cart == null || cart.isEmpty()) {
            throw new Exception("The cart is empty!");
        }

        Client client = clientDAO.findById(clientId);
        if(client == null){
            throw new Exception("Client not found!");
        }

        double totalPrice = 0;
        Date currentDate = new Date();
        Order newOrder = new Order(0, clientId, currentDate);
        Order insertedOrder = orderDAO.insert(newOrder);

        for (OrderItem item : cart) {
            Product product = productDAO.findById(item.getProductId());
            if(product == null) {
                throw new Exception("Product ID " + item.getProductId() + " not found!");
            }
            if(item.getQuantity() > product.getQuantity()){
                throw new Exception ("Not enough stock for " + product.getName() + "!");
            }

            int newQuantity = product.getQuantity() - item.getQuantity();
            Product updatedProduct = new Product(product.getId(), product.getName(), newQuantity, product.getPrice());
            productDAO.update(updatedProduct);

            item.setOrderId(insertedOrder.getId());
            orderItemDAO.insert(item);

            totalPrice += (item.getQuantity() * product.getPrice());
        }

        Bill newBill = new Bill(0, insertedOrder.getId(), clientId, totalPrice, currentDate, client.getAddress());
        billDAO.insert(newBill);

        return newBill;
    }


    /**
     * Finds an order in the database with a given id
     * @param id theidentifier in the database
     * @return the Order object with the given id
     * @throws java.util.NoSuchElementException if the id is not found
     */
    public Order findOrderById(int id) {
        Order order = orderDAO.findById(id);
        if (order == null) {
            throw new java.util.NoSuchElementException("Order with id " + id + " was not found!");
        }
        return order;
    }
}