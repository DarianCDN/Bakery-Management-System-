package presentation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * The main GUI built in Java Swing
 * Builds the tables, tabs, input fields and buttons
 */

public class View extends JFrame {

    private JTabbedPane tabbedPane;

    private final Color titleColor = new Color(32, 105, 82);
    private final Color resultsBackgroundColor = new Color(156, 197, 161);
    private final Color backgroundColor = new Color(220, 225, 222);
    private final Color btnColor = new Color(219, 234, 221);

    private JTextField clientIdField, clientNameField, clientAddressField, clientEmailField;
    private JButton btnAddClient, btnEditClient, btnDeleteClient, btnSearchClient;
    private JTable clientTable;
    private JTextArea clientLog;

    private JTextField productIdField, productNameField, productQuantityField, productPriceField;
    private JButton btnAddProduct, btnEditProduct, btnDeleteProduct, btnSearchProduct;
    private JTable productTable;
    private JTextArea productLog;

    private JTextField orderQuantityField, orderIdField;
    private JComboBox<String> orderClientCombo, orderProductCombo;
    private JButton btnAddToCart, btnPlaceOrder, btnSearchOrder, btnClearCart;    private JTable orderTable;
    private JTextArea orderLog;
    private JTextArea cartArea;


    public View() {
        setTitle("Dacia");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        getContentPane().setBackground(backgroundColor);

        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(btnColor);

        tabbedPane.addTab("Clients", createClientPanel());
        tabbedPane.addTab("Products", createProductPanel());
        tabbedPane.addTab("Orders", createOrderPanel());

        add(tabbedPane);
    }

    private JPanel createClientPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(backgroundColor);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topContainer = createInputContainer();
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBackground(titleColor);

        clientIdField = new JTextField();
        clientNameField = new JTextField();
        clientAddressField = new JTextField();
        clientEmailField = new JTextField();

        addLabelAndComponent(inputPanel, "ID (edit/delete/search):", clientIdField);
        addLabelAndComponent(inputPanel, "Name:", clientNameField);
        addLabelAndComponent(inputPanel, "Address:", clientAddressField);
        addLabelAndComponent(inputPanel, "Email:", clientEmailField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(titleColor);
        btnAddClient = createStyledButton("Add Client");
        btnEditClient = createStyledButton("Edit Client");
        btnDeleteClient = createStyledButton("Delete Client");
        btnSearchClient = createStyledButton("Search by ID");

        buttonPanel.add(btnAddClient);
        buttonPanel.add(btnEditClient);
        buttonPanel.add(btnDeleteClient);
        buttonPanel.add(btnSearchClient);

        topContainer.add(inputPanel, BorderLayout.NORTH);
        topContainer.add(buttonPanel, BorderLayout.SOUTH);

        clientTable = new JTable();
        clientTable.setBackground(resultsBackgroundColor);

        clientLog = createLogArea();

        panel.add(topContainer, BorderLayout.NORTH);
        panel.add(new JScrollPane(clientTable), BorderLayout.CENTER);
        panel.add(new JScrollPane(clientLog), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createProductPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(backgroundColor);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topContainer = createInputContainer();
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBackground(titleColor);

        productIdField = new JTextField();
        productNameField = new JTextField();
        productQuantityField = new JTextField();
        productPriceField = new JTextField();

        addLabelAndComponent(inputPanel, "Product ID:", productIdField);
        addLabelAndComponent(inputPanel, "Name:", productNameField);
        addLabelAndComponent(inputPanel, "Quantity:", productQuantityField);
        addLabelAndComponent(inputPanel, "Price:", productPriceField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(titleColor);
        btnAddProduct = createStyledButton("Add Product");
        btnEditProduct = createStyledButton("Edit Product");
        btnDeleteProduct = createStyledButton("Delete Product");
        btnSearchProduct = createStyledButton("Search by ID");

        buttonPanel.add(btnAddProduct);
        buttonPanel.add(btnEditProduct);
        buttonPanel.add(btnDeleteProduct);
        buttonPanel.add(btnSearchProduct);

        topContainer.add(inputPanel, BorderLayout.NORTH);
        topContainer.add(buttonPanel, BorderLayout.SOUTH);

        productTable = new JTable();
        productTable.setBackground(resultsBackgroundColor);

        productLog = createLogArea();

        panel.add(topContainer, BorderLayout.NORTH);
        panel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        panel.add(new JScrollPane(productLog), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(backgroundColor);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topContainer = createInputContainer();
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(titleColor);

        orderIdField = new JTextField();
        orderClientCombo = new JComboBox<>();
        orderProductCombo = new JComboBox<>();
        orderQuantityField = new JTextField();

        addLabelAndComponent(inputPanel, "Order ID (Search):", orderIdField);
        addLabelAndComponent(inputPanel, "Select Client:", orderClientCombo);
        addLabelAndComponent(inputPanel, "Select Product:", orderProductCombo);
        addLabelAndComponent(inputPanel, "Quantity to Add:", orderQuantityField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(titleColor);
        btnAddToCart = createStyledButton("Add Product to Cart");
        btnClearCart = createStyledButton("Clear Cart");
        btnPlaceOrder = createStyledButton("Place Final Order");
        btnSearchOrder = createStyledButton("Search Order");
        buttonPanel.add(btnAddToCart);
        buttonPanel.add(btnClearCart);
        buttonPanel.add(btnPlaceOrder);
        buttonPanel.add(btnSearchOrder);

        topContainer.add(inputPanel, BorderLayout.NORTH);
        topContainer.add(buttonPanel, BorderLayout.SOUTH);

        orderTable = new JTable();
        orderTable.setBackground(resultsBackgroundColor);

        JPanel centerPanel = new JPanel(new BorderLayout());
        cartArea = createLogArea();
        cartArea.setBorder(BorderFactory.createTitledBorder("Current Cart"));
        centerPanel.add(new JScrollPane(orderTable), BorderLayout.CENTER);
        centerPanel.add(new JScrollPane(cartArea), BorderLayout.EAST);

        orderLog = createLogArea();

        panel.add(topContainer, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(new JScrollPane(orderLog), BorderLayout.SOUTH);

        return panel;
    }

    private JTextArea createLogArea() {
        JTextArea log = new JTextArea(3, 50);
        log.setEditable(false);
        log.setBackground(btnColor);
        log.setFont(new Font("Monospaced", Font.PLAIN, 12));
        return log;
    }

    private JPanel createInputContainer() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(titleColor);
        return container;
    }

    private void addLabelAndComponent(JPanel panel, String text, JComponent comp) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        panel.add(label);
        panel.add(comp);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(btnColor);
        return btn;
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public String getClientId() { return clientIdField.getText(); }
    public String getClientName() { return clientNameField.getText(); }
    public String getClientAddress() { return clientAddressField.getText(); }
    public String getClientEmail() { return clientEmailField.getText(); }
    public JButton getBtnAddClient() { return btnAddClient; }
    public JButton getBtnEditClient() { return btnEditClient; }
    public JButton getBtnDeleteClient() { return btnDeleteClient; }
    public JButton getBtnSearchClient() { return btnSearchClient; }
    public void setClientTable(JTable table) { clientTable.setModel(table.getModel()); }
    public void setClientLog(String msg) { clientLog.append(msg + "\n"); }

    public String getProductId() { return productIdField.getText(); }
    public String getProductName() { return productNameField.getText(); }
    public String getProductQuantity() { return productQuantityField.getText(); }
    public String getProductPrice() { return productPriceField.getText(); }
    public JButton getBtnAddProduct() { return btnAddProduct; }
    public JButton getBtnEditProduct() { return btnEditProduct; }
    public JButton getBtnDeleteProduct() { return btnDeleteProduct; }
    public JButton getBtnSearchProduct() { return btnSearchProduct; }
    public void setProductTable(JTable table) { productTable.setModel(table.getModel()); }
    public void setProductLog(String msg) { productLog.append(msg + "\n"); }

    public String getOrderId() { return orderIdField.getText(); }
    public String getOrderClient() { return (String) orderClientCombo.getSelectedItem(); }
    public String getOrderProduct() { return (String) orderProductCombo.getSelectedItem(); }
    public String getOrderQuantity() { return orderQuantityField.getText(); }
    public JButton getBtnAddToCart() { return btnAddToCart; }
    public JButton getBtnPlaceOrder() { return btnPlaceOrder; }
    public JButton getBtnSearchOrder() { return btnSearchOrder; }
    public void setOrderTable(JTable table) { orderTable.setModel(table.getModel()); }
    public void setOrderLog(String msg) { orderLog.append(msg + "\n"); }
    public void appendCart(String msg) { cartArea.append(msg + "\n"); }
    public void clearCartArea() { cartArea.setText(""); }
    public JButton getBtnClearCart() { return btnClearCart; }

    public void setClientDropdown(String[] clients) {
        orderClientCombo.setModel(new DefaultComboBoxModel<>(clients));
    }

    public void setProductDropdown(String[] products) {
        orderProductCombo.setModel(new DefaultComboBoxModel<>(products));
    }
}