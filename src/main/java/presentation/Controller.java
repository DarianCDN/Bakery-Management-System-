package presentation;

import bll.ClientBLL;
import bll.OrderBLL;
import bll.OrderItemBLL;
import bll.ProductBLL;
import dao.BillDAO;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The controller of the application's presentation layer
 * Acts as a bridge between the interface and the buisness logic layer.
 * Initializes action listeners and handels flow of data when changes occur
 */
public class Controller {
    private View view;
    private ClientBLL clientBLL;
    private ProductBLL productBLL;
    private OrderBLL orderBLL;
    private OrderItemBLL orderItemBLL;
    private BillDAO billDAO;
    private List<OrderItem> shoppingCart;

    /**
     * Initializes the controller and binds it to the View.
     * Sets the initial state of the UI tables
     * @param view the user interface window to be controlled
     */
    public Controller(View view) {
        this.view = view;
        this.clientBLL = new ClientBLL();
        this.productBLL = new ProductBLL();
        this.orderBLL = new OrderBLL();
        this.orderItemBLL = new OrderItemBLL();
        this.billDAO = new BillDAO();
        this.shoppingCart = new ArrayList<>();

        initListeners();

        refreshClientTable();
        refreshProductTable();
        refreshDropdowns();
        refreshOrderTable();
    }

    private void initListeners() {
        view.getBtnAddClient().addActionListener(e -> {
            try {
                String name = view.getClientName();
                String address = view.getClientAddress();
                String email = view.getClientEmail();
                Client client = new Client(0, name, address, email);
                clientBLL.insertClient(client);
                refreshClientTable();
                refreshDropdowns();
                view.setClientLog("SUCCESS: Added client " + name);
            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        });

        view.getBtnEditClient().addActionListener(e -> {
            try {
                int id = Integer.parseInt(view.getClientId());
                String name = view.getClientName();
                String address = view.getClientAddress();
                String email = view.getClientEmail();
                Client client = new Client(id, name, address, email);
                clientBLL.updateClient(client);
                refreshClientTable();
                refreshDropdowns();
                view.setClientLog("SUCCESS: Updated client ID " + id);
            } catch (NumberFormatException ex) {
                view.showError("Invalid Client ID format!");
            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        });

        view.getBtnDeleteClient().addActionListener(e -> {
            try {
                int id = Integer.parseInt(view.getClientId());
                clientBLL.deleteClient(id);
                refreshClientTable();
                refreshDropdowns();
                view.setClientLog("SUCCESS: Deleted client ID " + id);
            } catch (NumberFormatException ex) {
                view.showError("Invalid Client ID format!");
            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        });

        view.getBtnSearchClient().addActionListener(e -> {
            try {
                int id = Integer.parseInt(view.getClientId());
                Client c = clientBLL.findClientById(id);
                String msg = "ID: " + c.getId() +
                        "\nName: " + c.getName() +
                        "\nAddress: " + c.getAddress() +
                        "\nEmail: " + c.getEmail();
                JOptionPane.showMessageDialog(view, msg, "Client Found", JOptionPane.INFORMATION_MESSAGE);
                view.setClientLog("SEARCH: Found client " + c.getName());
            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        });

        view.getBtnAddProduct().addActionListener(e -> {
            try {
                String name = view.getProductName();
                int quantity = Integer.parseInt(view.getProductQuantity());
                double price = Double.parseDouble(view.getProductPrice());
                Product product = new Product(0, name, quantity, price);
                productBLL.insertProduct(product);
                refreshProductTable();
                refreshDropdowns();
                view.setProductLog("SUCCESS: Added product " + name);
            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        });

        view.getBtnEditProduct().addActionListener(e -> {
            try {
                int id = Integer.parseInt(view.getProductId());
                String name = view.getProductName();
                int quantity = Integer.parseInt(view.getProductQuantity());
                double price = Double.parseDouble(view.getProductPrice());
                Product product = new Product(id, name, quantity, price);
                productBLL.updateProduct(product);
                refreshProductTable();
                refreshDropdowns();
                view.setProductLog("SUCCESS: Updated product ID " + id);
            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        });

        view.getBtnDeleteProduct().addActionListener(e -> {
            try {
                int id = Integer.parseInt(view.getProductId());
                productBLL.deleteProduct(id);
                refreshProductTable();
                refreshDropdowns();
                view.setProductLog("SUCCESS: Deleted product ID " + id);
            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        });

        view.getBtnSearchProduct().addActionListener(e -> {
            try {
                int id = Integer.parseInt(view.getProductId());
                Product p = productBLL.findProductById(id);
                String msg = "ID: " + p.getId() +
                        "\nName: " + p.getName() +
                        "\nStock: " + p.getQuantity() +
                        "\nPrice: " + p.getPrice();
                JOptionPane.showMessageDialog(view, msg, "Product Found", JOptionPane.INFORMATION_MESSAGE);
                view.setProductLog("SEARCH: Found product " + p.getName());
            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        });

        view.getBtnAddToCart().addActionListener(e -> {
            try {
                String selectedProduct = view.getOrderProduct();
                if (selectedProduct == null) throw new Exception("No product selected.");

                int productId = Integer.parseInt(selectedProduct.split(" - ")[0]);
                String productName = selectedProduct.split(" - ")[1].split(" ")[0];
                int quantity = Integer.parseInt(view.getOrderQuantity());

                OrderItem item = new OrderItem(0, 0, productId, quantity);
                shoppingCart.add(item);
                view.appendCart("- " + productName + " (x" + quantity + ")");
                view.setOrderLog("Added " + quantity + " " + productName + "(s) to cart.");
            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        });

        view.getBtnClearCart().addActionListener(e -> {
            shoppingCart.clear();
            view.clearCartArea();
            view.setOrderLog("Cart has been cleared.");
        });

        view.getBtnPlaceOrder().addActionListener(e -> {
            try {
                String selectedClient = view.getOrderClient();
                if(selectedClient == null) throw new Exception("Please select a client.");

                int clientId = Integer.parseInt(selectedClient.split(" - ")[0]);

                Bill bill = orderBLL.placeOrder(clientId, shoppingCart);

                view.setOrderLog("ORDER SUCCESS: Bill #" + bill.id() + " | Total: " + bill.totalPrice());
                shoppingCart.clear();
                view.clearCartArea();

                refreshProductTable();
                refreshDropdowns();
                refreshOrderTable();
            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        });

        view.getBtnSearchOrder().addActionListener(e -> {
            try {
                int id = Integer.parseInt(view.getOrderId());

                Order o = orderBLL.findOrderById(id);
                Client c = clientBLL.findClientById(o.getClientId());

                StringBuilder info = new StringBuilder();
                info.append("Order ID: ").append(o.getId()).append("\n");
                info.append("Placed by: ").append(c.getName()).append("\n");
                info.append("----------------------------------\n");
                info.append("Products in this Order:\n");

                List<OrderItem> items = orderItemBLL.findItemsByOrderId(o.getId());

                if (items.isEmpty()) {
                    info.append("No products found.\n");
                } else {
                    for (OrderItem item : items) {
                        Product p = productBLL.findProductById(item.getProductId());
                        info.append("- ").append(p.getName())
                                .append(" (").append(p.getPrice()).append(") x ")
                                .append(item.getQuantity()).append("\n");
                    }
                }

                JOptionPane.showMessageDialog(view, info.toString(), "Order Details", JOptionPane.INFORMATION_MESSAGE);
                view.setOrderLog("SEARCH: Found Order ID " + id);

            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        });
    }

    private void refreshClientTable() {
        List<Client> clients = clientBLL.findAllClients();
        view.setClientTable(createTableFromObjects(clients));
    }

    private void refreshProductTable() {
        List<Product> products = productBLL.findAllProducts();
        view.setProductTable(createTableFromObjects(products));
    }

    private void refreshOrderTable() {
        List<Bill> bills = billDAO.findAll();
        view.setOrderTable(createTableFromObjects(bills));
    }

    private void refreshDropdowns() {
        List<Client> clients = clientBLL.findAllClients();
        String[] clientItems = clients.stream()
                .map(c -> c.getId() + " - " + c.getName())
                .toArray(String[]::new);
        view.setClientDropdown(clientItems);

        List<Product> products = productBLL.findAvailableProducts();
        String[] productItems = products.stream()
                .map(p -> p.getId() + " - " + p.getName() + " (" + p.getPrice() + ")")
                .toArray(String[]::new);
        view.setProductDropdown(productItems);
    }

    private <T> JTable createTableFromObjects(List<T> objects) {
        if (objects == null || objects.isEmpty()) {
            return new JTable(new DefaultTableModel());
        }

        Class<?> type = objects.get(0).getClass();
        Field[] fields = type.getDeclaredFields();
        String[] columnNames = Arrays.stream(fields).map(Field::getName).toArray(String[]::new);

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (T obj : objects) {
            Object[] row = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                try {
                    fields[i].setAccessible(true);
                    row[i] = fields[i].get(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            model.addRow(row);
        }
        return new JTable(model);
    }
}