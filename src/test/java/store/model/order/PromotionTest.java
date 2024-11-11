package store.model.order;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.dto.request.OrderItemInputDto;
import store.dto.request.ProductInputDto;
import store.dto.request.PromotionTypeInputDto;
import store.model.product.ProductManager;
import store.model.product.PromotionTypeManager;

public class PromotionTest {
    private PromotionTypeManager promotionTypeManager;
    private ProductManager productManager;
    private List<OrderItemInputDto> applicableOrderItems1;
    private List<OrderItemInputDto> applicableOrderItems2;
    private LocalDate currentDate;

    @BeforeEach
    void setUp() {
        promotionTypeManager = new PromotionTypeManager(
                List.of(
                        new PromotionTypeInputDto("1+1", 1, 1, LocalDate.now(), LocalDate.now().plusDays(10)),
                        new PromotionTypeInputDto("2+1", 2, 1, LocalDate.now(), LocalDate.now().plusDays(10))));
        ProductInputDto productInput1 = new ProductInputDto("Product1", 1000, 10, "1+1");
        ProductInputDto productInput2 = new ProductInputDto("Product1", 1000, 5, "");
        ProductInputDto productInput3 = new ProductInputDto("Product2", 2000, 10, "");
        List<ProductInputDto> productInputs = List.of(productInput1, productInput2, productInput3);

        productManager = new ProductManager(promotionTypeManager, productInputs);
        OrderItemInputDto orderItemWithPromotion1 = new OrderItemInputDto(
                productManager.getStocks().get(0).getProduct().getName(),
                10);
        OrderItemInputDto orderItemWithoutPromotion = new OrderItemInputDto(
                productManager.getStocks().get(2).getProduct().getName(), 3);

        applicableOrderItems1 = List.of(orderItemWithPromotion1);
        applicableOrderItems2 = List.of(orderItemWithoutPromotion);
        currentDate = DateTimes.now().toLocalDate();
    }

    @Test
    @DisplayName("프로모션 기간이 유효한 경우 적용된다")
    void 프로모션_유효할_때_적용된다() {
        // given
        Order order = new Order(productManager, applicableOrderItems1, currentDate);

        // when
        Promotion promotion = new Promotion(productManager, order.getOrderItems(), currentDate);

        // then
        Assertions.assertThat(promotion.isCanReceiveMorePromotion()).isFalse();
        Assertions.assertThat(promotion.getTotalBonusQuantity()).isEqualTo(10);
        Assertions.assertThat(promotion.getRemainingQuantity()).isEqualTo(0);
    }

    @Test
    @DisplayName("프로모션 기간이 유효하지 않은 경우 적용되지 않는다")
    void 프로모션_만료시_적용되지_않는다() {
        // given
        LocalDate expiredDate = LocalDate.now().minusDays(10);
        Order order = new Order(productManager, applicableOrderItems1, expiredDate);

        // when
        Promotion promotion = new Promotion(productManager, order.getOrderItems(), expiredDate);

        // then
        Assertions.assertThat(promotion.isCanReceiveMorePromotion()).isFalse();
        Assertions.assertThat(promotion.getTotalBonusQuantity()).isEqualTo(0);
    }

    @Test
    @DisplayName("프로모션 상품의 재고가 부족할 때 더 받을 수 없다")
    void 프로모션_재고부족시_추가혜택_불가() {
        // given
        OrderItemInputDto orderItemInputDto = new OrderItemInputDto(
                productManager.getStocks().get(0).getProduct().getName(),
                11);
        Order order = new Order(productManager, List.of(orderItemInputDto), currentDate);

        // when
        Promotion promotion = new Promotion(productManager, order.getOrderItems(), currentDate);

        // then
        Assertions.assertThat(promotion.isCanReceiveMorePromotion()).isFalse();
        Assertions.assertThat(promotion.getTotalBonusQuantity()).isEqualTo(10);
    }

    @Test
    @DisplayName("적용할 프로모션이 없으면 적용되지 않는다")
    void 프로모션_없는_경우_적용되지_않는다() {
        // given
        Order order = new Order(productManager, applicableOrderItems2, currentDate);

        // when
        Promotion promotion = new Promotion(productManager, order.getOrderItems(), currentDate);

        // then
        Assertions.assertThat(promotion.isCanReceiveMorePromotion()).isFalse();
        Assertions.assertThat(promotion.getTotalBonusQuantity()).isEqualTo(0);
    }
}
