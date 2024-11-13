package store.model.product;

import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.exception.ExceptionMessage;

public class StockTest {
    @DisplayName("상품이 비었을 때 재고를 생성하면 예외가 발생한다.")
    @Test
    void 상품이없을때_재고생성_예외() {
        Product product = null;
        int quantity = 5;
        Assertions.assertThatThrownBy(() -> new Stock(product, quantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.NULL_VALUE_ERROR.getMessage());
    }

    @DisplayName("상품 수량이 음수일 때 때 재고를 생성하면 예외가 발생한다.")
    @Test
    void 상품수량_음수일때_재고생성_예외() {
        Product product = new Product("상품", 100, new PromotionType("1+1", 1, 1, LocalDate.now(), LocalDate.now()));
        int quantity = -1;
        Assertions.assertThatThrownBy(() -> new Stock(product, quantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.INVALID_PRODUCT_MIN_QUANTITY.getMessage());
    }
}
