package store.model.order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.dto.request.OrderItemInputDto;
import store.exception.ExceptionMessage;
import store.exception.ExceptionUtils;
import store.model.product.Product;
import store.model.product.ProductManager;
import store.model.product.Stock;

public class Order {
    private final ProductManager productManager;
    private final List<OrderItem> orderItems = new ArrayList<>();
    private final LocalDate orderDate;

    public Order(ProductManager productManager, List<OrderItemInputDto> orderItemInputsDto,
                 LocalDate orderDate) {
        this.productManager = productManager;
        this.orderDate = orderDate;
        List<OrderItemInputDto> mergeOrderItemsInput = mergeDuplicateProducts(orderItemInputsDto);
        for (OrderItemInputDto orderItemInputDto : mergeOrderItemsInput) {
            createOrderItem(orderItemInputDto.productName(), orderItemInputDto.orderQuantity());
        }
    }

    public List<OrderItem> getOrderItems() {
        return List.copyOf(orderItems);
    }

    public List<OrderItem> findOrderItemByProductName(String productName) {
        return orderItems.stream()
                .filter(orderItem -> orderItem.getProductName().equals(productName))
                .toList();
    }

    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
    }

    private List<OrderItemInputDto> mergeDuplicateProducts(List<OrderItemInputDto> orderItemInputsDto) {
        Map<String, Integer> mergedProducts = new HashMap<>();
        for (OrderItemInputDto item : orderItemInputsDto) {
            mergedProducts.merge(item.productName(), item.orderQuantity(), Integer::sum);
        }
        return mergedProducts.entrySet().stream()
                .map(entry -> new OrderItemInputDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private void createOrderItem(String productName, int orderQuantity) {
        validateProductExistsInStore(productName);
        validateOrderQuantity(productName, orderQuantity);
        List<Stock> productStocks = productManager.findStocksByProductName(productName);
        int remainingQuantity = orderQuantity;
        for (Stock stock : productStocks) {
            if (remainingQuantity == 0) {
                return;
            }
            remainingQuantity = createOrderAndUpdateStock(stock, remainingQuantity);
        }
    }

    private int createOrderAndUpdateStock(Stock productStock, int remainingQuantity) {
        if (productStock.getQuantity() <= 0) {
            return remainingQuantity;
        }
        return calculateQuantityAndcreateOrderItem(productStock, remainingQuantity);
    }

    private int calculateQuantityAndcreateOrderItem(Stock productStock, int remainingQuantity) {
        if (remainingQuantity <= productStock.getQuantity()) {
            orderItems.add(new OrderItem(productStock.getProduct(), remainingQuantity));
            productStock.reduceQuantity(remainingQuantity);
            return 0;
        }
        orderItems.add(new OrderItem(productStock.getProduct(), productStock.getQuantity()));
        remainingQuantity -= productStock.getQuantity();
        productStock.reduceQuantity(productStock.getQuantity());
        return remainingQuantity;
    }

    private void validateOrderQuantity(String productName, int orderQuantity) {
        int productTotalQuantity = productManager.getProductTotalQuantity(productName);
        if (productTotalQuantity < orderQuantity) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_ORDER_ITEM_QUANTITY);
        }
    }

    private void validateProductExistsInStore(String productName) {
        List<Product> matchingProductsInStore = productManager.findMatchingProducts(productName);
        if (matchingProductsInStore == null || matchingProductsInStore.isEmpty()) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_ORDER_PRODUCT);
        }
    }
}

