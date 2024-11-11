package store.model.order;

import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.dto.request.OrderItemInputDto;
import store.dto.request.ProductInputDto;
import store.dto.request.PromotionTypeInputDto;
import store.dto.response.PromotionResultDto;
import store.model.product.ProductManager;
import store.model.product.PromotionTypeManager;
import store.service.OrderService;

public class MembershipTest {
    private PromotionTypeManager promotionTypeManager;
    private ProductManager productManager;
    private List<OrderItemInputDto> applicableOrderItems;
    private LocalDate currentDate;

    @BeforeEach
    void 설정() {
        promotionTypeManager = new PromotionTypeManager(
                List.of(
                        new PromotionTypeInputDto("1+1", 1, 1, LocalDate.now(), LocalDate.now().plusDays(10)),
                        new PromotionTypeInputDto("2+1", 2, 1, LocalDate.now(), LocalDate.now().plusDays(10)),
                        new PromotionTypeInputDto("2+3", 2, 3, LocalDate.now(), LocalDate.now().plusDays(10))));

        ProductInputDto productInput1 = new ProductInputDto("콜라", 1000, 10, "2+1");
        ProductInputDto productInput2 = new ProductInputDto("에너지바", 2000, 5, "");
        List<ProductInputDto> productInputs = List.of(productInput1, productInput2);

        productManager = new ProductManager(promotionTypeManager, productInputs);

        applicableOrderItems = List.of(
                new OrderItemInputDto("콜라", 3),
                new OrderItemInputDto("에너지바", 5)
        );

        currentDate = LocalDate.now();
    }

    @DisplayName("프로모션 상품이 있어도 멤버십 할인 금액을 계산한다.")
    @Test
    void 멤버십_할인_금액_계산() {
        Order order = new Order(productManager, applicableOrderItems, currentDate);
        List<PromotionResultDto> promotionResults = new OrderService(productManager).processPromotions(order);

        double membershipDiscount = Membership.calculateMembershipDiscount(order, promotionResults);

        Assertions.assertThat(membershipDiscount).isEqualTo(3000);
    }
}
