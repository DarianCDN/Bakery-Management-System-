package model;

/**
 * An OrderItem entity in the application
 * It maps directly to the table with the same name in the database
 * Used to transfer data between layers
 */

public class OrderItem {
    private int id;
    private int orderId;
    private int productId;
    private int quantity;
    /**
     * default constructor needed for the DAO layer
     */
    public OrderItem() {
    }

    /**
     *
     * @param id the identifier in the database
     * @param orderId the id of the order it belongs to
     * @param productId the id of the product
     * @param quantity the quantity ordered
     */
    public OrderItem(int id, int orderId, int productId, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}