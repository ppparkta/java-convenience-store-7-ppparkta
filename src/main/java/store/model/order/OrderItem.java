package store.model.order;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderItem orderItem = (OrderItem) o;
        return quantity == orderItem.quantity && Objects.equals(product, orderItem.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, quantity);
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public Optional<PromotionType> getPromotionType() {
        return product.getPromotionType();
    }

    public String getProductName() {
        return product.getName();
    }
}
