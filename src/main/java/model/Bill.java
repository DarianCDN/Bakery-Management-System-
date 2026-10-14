package model;

import java.util.Date;

/**
 * immutable receipt generated when completing an order
 * it is read-only to ensure immutability
 * @param id the unique identifier in the database
 * @param orderId the id of the order
 * @param clientId the id of the client
 * @param totalPrice the final price of the order
 * @param date the timestamp of when the order was completed
 * @param address the address of the client
 */
public record Bill(int id, int orderId, int clientId, double totalPrice, Date date, String address)
{

}