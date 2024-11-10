package store.model.order;

import java.util.ArrayList;
import java.util.List;
import store.dto.OrderItemInputDto;
import store.exception.ExceptionMessage;
import store.exception.ExceptionUtils;
import store.model.product.Product;
import store.model.product.ProductManager;

public class Order {
    private final ProductManager productManager;
    private final List<OrderItem> orderItems = new ArrayList<>();

    public Order(ProductManager productManager, List<OrderItemInputDto> orderItemInputsDto) {
        this.productManager = productManager;
        for (OrderItemInputDto orderItemInputDto : orderItemInputsDto) {
            createOrUpdateOrderItem(orderItemInputDto.productName(), orderItemInputDto.orderQuantity());
        }
    }

    private void createOrUpdateOrderItem(String productName, int orderQuantity) {
        validateProductExistsInStore(productName);
        validateOrderQuantity(productName, orderQuantity);
        if (isProductMatchedInOrder(productName)) {
            addOrderItemQuantity(productName, orderQuantity);
            return;
        }
        createOrderItem(productName, orderQuantity);
    }

    private void validateProductExistsInStore(String productName) {
        List<Product> matchingProductsInStore = productManager.findMatchingProducts(productName);
        if (matchingProductsInStore == null || matchingProductsInStore.isEmpty()) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_PRODUCT_NAME);
        }
    }

    private void validateOrderQuantity(String productName, int orderQuantity) {
        int productTotalQuantity = productManager.getProductTotalQuantity(productName);
        if (productTotalQuantity < orderQuantity) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_ORDER_ITEM_QUANTITY);
        }
    }

    private boolean isProductMatchedInOrder(String productName) {
        return orderItems.stream()
                .anyMatch(orderItem -> orderItem.isProductNameEqual(productName));
    }

    private void addOrderItemQuantity(String productName, int orderQuantity) {
        OrderItem foundOrderItem = orderItems.stream()
                .filter(orderItem -> orderItem.isProductNameEqual(productName))
                .sorted()
                .toList()
                .getFirst();
        validateOrderQuantity(productName, foundOrderItem.getQuantity() + orderQuantity);
        foundOrderItem.addQuantity(orderQuantity);
    }

    private void createOrderItem(String productName, int orderQuantity) {
        Product matchingProduct = productManager.getFirstMatchingProduct(productName);
        orderItems.add(new OrderItem(matchingProduct, orderQuantity));
    }
}
