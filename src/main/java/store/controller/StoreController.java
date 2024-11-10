package store.controller;

import store.model.product.ProductManager;

public class StoreController {
    public void run() {
        try {
            ProductController productController = new ProductController();
            ProductManager productManager = productController.initialize();

            OrderController orderController = new OrderController(productManager);
            orderController.processOrder();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
