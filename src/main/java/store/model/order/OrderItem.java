package store.model.order;

import java.util.Optional;
import store.model.product.Product;
import store.model.product.PromotionType;

public class OrderItem implements Comparable<OrderItem> {
    private final Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    @Override
    public int compareTo(OrderItem o) {
        if (this.product.getPromotionType() == null && o.product.getPromotionType() != null) {
            return 1;
        }
        if (this.product.getPromotionType() != null && o.product.getPromotionType() == null) {
            return -1;
        }
        return 0;
    }

    public int getQuantity() {
        return quantity;
    }

    public Optional<PromotionType> getPromotionType() {
        return product.getPromotionType();
    }

    public String getProductName() {
        return product.getName();
    }
}
