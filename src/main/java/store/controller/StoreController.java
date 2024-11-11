package store.controller;

import java.util.NoSuchElementException;
import store.exception.ExceptionMessage;
import store.model.product.ProductManager;

public class StoreController {
    public void run() {
        try {
            ProductController productController = new ProductController();
            ProductManager productManager = productController.initialize();

            new OrderController(productManager).processOrder();
        } catch (NoSuchElementException e) {
            System.out.println(ExceptionMessage.NO_SUCH_ERROR.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
