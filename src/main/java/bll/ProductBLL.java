package bll;

import dao.ProductDAO;
import model.Product;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 *Handles product related operations
 * intermediary class between data access and presentation
 */
public class ProductBLL {

    private ProductDAO productDAO;

    public ProductBLL() {
        productDAO = new ProductDAO();
    }

    /**
     * finds a product with the given id
     * @param id the identifier in the database
     * @return the Product object with that identifier
     * @throws NoSuchElementException when the id is not found
     */
    public Product findProductById(int id) {
        Product product = productDAO.findById(id);
        if (product == null) {
            throw new NoSuchElementException("The product with id " + id + " not found!");
        }
        return product;
    }


    /**
     * creates a list with all the products in the database
     * @return a list with all the items
     */
    public List<Product> findAllProducts() {
        return productDAO.findAll();
    }


    /**
     * validates the data and inserts a new product in the database
     * @param product the product that will be inserted
     * @return the created product object
     * @throws NoSuchElementException if the name is null,  or the quantity or price is negative
     */
    public Product insertProduct(Product product) throws Exception {
        if (product.getPrice() < 0 || product.getQuantity() < 0) {
            throw new Exception("Price and quantity must be positive values!");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new Exception("Product name cannot be empty!");
        }
        return productDAO.insert(product);
    }

    /**
     * updates a product in the databse
     * @param product the product to be updated
     * @return the updated product object
     * @throws NoSuchElementException if the name is null, or the price or quantity is negative
     */
    public Product updateProduct(Product product) throws Exception {
        if (product.getPrice() < 0 || product.getQuantity() < 0) {
            throw new Exception("Price and quantity must be positive values!");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new Exception("Product name cannot be empty!");
        }
        return productDAO.update(product);
    }

    /**
     * deletes a product from the database
     * @param id the id of the product to be deleted
     */
    public void deleteProduct(int id) {
        productDAO.delete(id);
    }

    /**
     * uses lambda expressions to find all the proucts that are still in stock
     * @return a list of the items with a quantity > 0
     */

    public List<Product> findAvailableProducts(){
        List<Product> allProducts = productDAO.findAll();

        return allProducts.stream()
                .filter(product -> product.getQuantity() > 0)
                .collect(Collectors.toList());
    }

}
