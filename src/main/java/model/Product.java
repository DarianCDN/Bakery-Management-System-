package model;
/**
 * A Client entity in the application
 * It maps directly to the table with the same name in the database
 * Used to transfer data between layers
 */
public class Product {
    int id;
    String name;
    int quantity;
    double price;
    /**
     * default constructor needed for the DAO layer
     */
    public Product(){}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    /**
     * creates a Product entity
     * @param id the unique identifier in the database
     * @param name the name of the product
     * @param quantity the number of units in stock
     * @param price the price of one unit
     */
    public Product(int id, String name, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
