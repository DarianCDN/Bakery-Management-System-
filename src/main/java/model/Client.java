package model;

/**
 * A Client entity in the application
 * It maps directly to the table with the same name in the database
 * Used to transfer data between layers
 */

public class Client {

    int id;
    String name;
    String address;
    String email;

    /**
     * default constructor needed for the DAO layer
     */
    public Client(){}

    public String getEmail(){
        return email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    /**
     * constructs a Client object
     * @param id the unique identifier in the database
     * @param name the name of the client
     * @param address the address of the client
     * @param email the email address of the client
     */
    public Client(int id, String name, String address, String email) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
