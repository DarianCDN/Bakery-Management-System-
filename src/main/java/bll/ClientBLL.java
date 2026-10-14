package bll;

import bll.validators.EmailValidator;
import bll.validators.Validator;
import dao.ClientDAO;
import model.Client;
import model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * Manages the client related operations.
 *Acts as an intermediary class to connect the presentation layer and the data access
 */


public class ClientBLL
{
    private ClientDAO clientDAO;
    private EmailValidator emailValidator;


    /**
     * Constructor
     * initializes the Data Access object and the email validator
     */
    public ClientBLL(){

        clientDAO = new ClientDAO();
        emailValidator = new EmailValidator();

    }

    /**
     * Searches a client in the database by their unique id
     *
     * @param id The database unique identifier
     * @throws NoSuchElementException if no client is found with the given id
     * @return The client  with that id
     */
    public Client findClientById(int id) {
        Client client = clientDAO.findById(id);
        if (client == null) {
            throw new NoSuchElementException("The client with id " + id + " not found!");
        }
        return client;
    }

    /**
     *retireves all clients from the database
     *
     * @return A list containing all Client objects in the database
     */
    public List<Client> findAllClients() {
        return clientDAO.findAll();
    }

    /**
     * Validates the information of a client and then inserts it in the database
     * @param client the Client object added to the database
     * @return The Client object created
     * @throws Exception if the client name is empty or the email is not valid
     */
    public Client insertClient(Client client) throws Exception {
        if (client.getName() == null || client.getName().trim().isEmpty()) {
            throw new Exception("Client name cannot be empty!");
        }

        emailValidator.validate(client);

        return clientDAO.insert(client);
    }

    /**
     * Validates and updates the field of a given client
     * @param client the Client to be updated
     * @return the updated Client object
     * @throws Exception if the name is empty or the email is invalid
     */
    public Client updateClient(Client client) throws Exception {
        if (client.getName() == null || client.getName().trim().isEmpty()) {
            throw new Exception("Client name cannot be empty!");
        }

        emailValidator.validate(client);


        return clientDAO.update(client);
    }

    /**
     * Deletes the client with a given id from the database
     * @param id the id of the client that will be deleted
     */
    public void deleteClient(int id) {
        clientDAO.delete(id);
    }
}
