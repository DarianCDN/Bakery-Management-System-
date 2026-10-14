package model;

import java.util.Date;

/**
 * An Order entity in the application
 * It maps directly to the table with the same name in the database
 * Used to transfer data between layers
 */
public class Order{
    int id;
    int clientId;
    Date date;

    /**
     * default constructor needed for the DAO layer
     */
    public Order(){}

    public int getId() {
        return id;
    }

    public int getClientId() {
        return clientId;
    }

    public Date getDate() {
        return date;
    }

    /**
     * Constructs an Order object
     * @param id the unique identifier in the database
     * @param clientId the id of the client who placed the order
     * @param date the date when the order was placed
     */
    public Order(int id, int clientId, Date date) {
        this.id = id;
        this.clientId = clientId;
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
