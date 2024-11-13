package store.model.product;

import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.dto.request.ProductInputDto;
import store.dto.request.PromotionTypeInputDto;
import store.exception.ExceptionMessage;

public class ProductManagerTest {
    private PromotionTypeManager promotionTypeManager;

    @BeforeEach
    void setUp() {
        promotionTypeManager = new PromotionTypeManager(
                List.of(
                        new PromotionTypeInputDto("1+1", 1, 1, LocalDate.now(), LocalDate.now().plusDays(10)),
                        new PromotionTypeInputDto("2+1", 2, 1, LocalDate.now(), LocalDate.now().plusDays(10))));
    }

    @DisplayName("다른 상품 두개를 넣었을 때 재고가 올바르게 추가된다.")
    @Test
    void 다른_상품_두개_재고_추가() {
        // given
        ProductInputDto productInput1 = new ProductInputDto("Product1", 100, 10, "1+1");
        ProductInputDto productInput2 = new ProductInputDto("Product2", 200, 5, "");
        List<ProductInputDto> productInputs = List.of(productInput1, productInput2);

        //when
        ProductManager productManager = new ProductManager(promotionTypeManager, productInputs);

        // then
        Assertions.assertThat(productManager.getStocks()).hasSize(3);
        Assertions.assertThat(productManager.getStocks().get(0).getProduct().getName()).isEqualTo("Product1");
        Assertions.assertThat(productManager.getStocks().get(0).getQuantity()).isEqualTo(10);
        Assertions.assertThat(productManager.getStocks().get(1).getProduct().getName()).isEqualTo("Product2");
        Assertions.assertThat(productManager.getStocks().get(1).getQuantity()).isEqualTo(5);
    }

    @DisplayName("같은 상품을 여러번 등록 시 수량만 추가된다.")
    @Test
    void 같은_상품_여러번_등록시_수량만_추가() {
        // given
        ProductInputDto productInput1 = new ProductInputDto("Product1", 100, 10, "1+1");
        ProductInputDto productInput2 = new ProductInputDto("Product1", 100, 5, "1+1");
        List<ProductInputDto> productInputs = List.of(productInput1, productInput2);

        // when
        ProductManager productManager = new ProductManager(promotionTypeManager, productInputs);

        // then
        Assertions.assertThat(productManager.getStocks()).hasSize(2);
        Assertions.assertThat(productManager.getStocks().get(0).getProduct().getName()).isEqualTo("Product1");
        Assertions.assertThat(productManager.getStocks().get(0).getQuantity()).isEqualTo(15);
    }

    @DisplayName("같은 상품이더라도 프로모션이 다르면 재고에 추가된다.")
    @Test
    void 같은_상품_다른_프로모션_등록시_재고_추가() {
        // given
        ProductInputDto productInput1 = new ProductInputDto("Product1", 100, 10, "1+1");
        ProductInputDto productInput2 = new ProductInputDto("Product1", 100, 5, "");
        List<ProductInputDto> productInputs = List.of(productInput1, productInput2);

        // when
        ProductManager productManager = new ProductManager(promotionTypeManager, productInputs);

        // then
        Assertions.assertThat(productManager.getStocks()).hasSize(2);
        Assertions.assertThat(productManager.getStocks().get(0).getProduct().getName()).isEqualTo("Product1");
        Assertions.assertThat(productManager.getStocks().get(0).getProduct().getPromotionType().get().getName())
                .isEqualTo("1+1");
        Assertions.assertThat(productManager.getStocks().get(0).getQuantity()).isEqualTo(10);
        Assertions.assertThat(productManager.getStocks().get(1).getProduct().getName()).isEqualTo("Product1");
        Assertions.assertThat(productManager.getStocks().get(1).getProduct().getPromotionType().isPresent()).isFalse();
        Assertions.assertThat(productManager.getStocks().get(1).getQuantity()).isEqualTo(5);
    }

    @DisplayName("같은 상품을 서로 다른 가격으로 등록할 때 예외가 발생한다.")
    @Test
    void 같은_상품_다른_프로모션_두개_재고_추가() {
        ProductInputDto productInput1 = new ProductInputDto("Product1", 100, 10, "1+1");
        ProductInputDto productInput2 = new ProductInputDto("Product1", 200, 5, "");
        List<ProductInputDto> productInputs = List.of(productInput1, productInput2);

        Assertions.assertThatThrownBy(() -> {
                    new ProductManager(promotionTypeManager, productInputs);
                })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.INVALID_PRODUCT_PRICE.getMessage());
    }

    @DisplayName("같은 상품에 다른 프로모션을 여러 개 등록할 때 예외가 발생한다.")
    @Test
    void 다른_프로모션_두개_재고_추가() {
        ProductInputDto productInput1 = new ProductInputDto("Product1", 100, 10, "1+1");
        ProductInputDto productInput2 = new ProductInputDto("Product1", 100, 5, "2+1");
        List<ProductInputDto> productInputs = List.of(productInput1, productInput2);

        Assertions.assertThatThrownBy(() -> {
                    new ProductManager(promotionTypeManager, productInputs);
                })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.MAX_PROMOTION_TYPES_PER_PRODUCT_EXCEEDED.getMessage());
    }
}
