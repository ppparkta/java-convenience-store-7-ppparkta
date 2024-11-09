package store.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.constant.ExceptionMessage;

class ProductTest {
    @Nested
    @DisplayName("상품 생성 유효성검사")
    class ProductValidate {
        private PromotionType promotionType;

        @BeforeEach
        void beforeInit() {
            this.promotionType = createPromotionType("1+1", 1, 1);
        }

        @DisplayName("상품명이 빈 문자열일 때 예외가 발생한다.")
        @Test
        void 상품명_빈문자열_예외() {
            assertThatThrownBy(() -> {
                new Product("", 1000, this.promotionType);
            })
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ExceptionMessage.INVALID_EMPTY_INPUT.getMessage());
        }

        @DisplayName("가격이 음수이면 예외가 발생한다.")
        @Test
        void 가격_음수_예외() {
            assertThatThrownBy(() ->
                    new Product("아이스크림", -1, this.promotionType)
            )
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ExceptionMessage.INVALID_MIN_PRICE.getMessage());
        }

        @DisplayName("가격이 int 범위를 초과하면 예외가 발생한다.")
        @Test
        void 가격_크면_예외() {
            assertThatThrownBy(() -> {
                new Product("아이스크림", 2200_000_000L, this.promotionType);
            })
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ExceptionMessage.INVALID_INTEGER_RANGE.getMessage());
        }
    }

    @DisplayName("이름과 가격 중 하나라도 다르면 다른 상품으로 판단한다.")
    @Test
    void 동일한_상품을_찾지못함() {
        // given
        Product product1 = new Product("상품", 1000, null);
        Product product2 = new Product("상품", 2000, null);

        // when
        boolean equalsResult = product1.equals(product2);

        // then
        Assertions.assertThat(equalsResult).isFalse();
    }

    @DisplayName("이름과 가격이 동일하면 같은 상품으로 판단한다.")
    @Test
    void 동일한_상품을_찾음() {
        // given
        Product product1 = new Product("상품", 1000, null);
        Product product2 = new Product("상품", 1000, null);

        // when
        boolean equalsResult = product1.equals(product2);

        // then
        Assertions.assertThat(equalsResult).isTrue();
    }

    PromotionType createPromotionType(String name, int buy, int get) {
        return new PromotionType(name, buy, get,
                LocalDate.of(
                        2024, LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth()
                ),
                LocalDate.of(
                        2024, LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth()
                ));
    }
}