package store.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProductTest {
    @Nested
    @DisplayName("상품 생성 유효성검사")
    class validate {
        private PromotionType promotionType;

        @BeforeEach
        void beforeInit() {
            this.promotionType = new PromotionType("1+1", 1, 1, "2024-11-08", "2024-12-04");
        }

        @DisplayName("상품명이 빈 문자열일 때 예외가 발생한다.")
        @Test
        void 상품명_빈문자열_예외() {
            assertThatThrownBy(() -> {
                new Product("", 1000, this.promotionType);
            })
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 빈 문자열일 수 없습니다.");
        }

        @DisplayName("가격이 음수이면 예외가 발생한다.")
        @Test
        void 가격_음수_예외() {
            assertThatThrownBy(() -> {
                new Product("아이스크림", -1, this.promotionType);
            })
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 빈 문자열일 수 없습니다.");
        }

        @DisplayName("가격이 int 범위를 초과하면 예외가 발생한다.")
        @Test
        void 가격_크면_예외() {
            assertThatThrownBy(() -> {
                new Product("아이스크림", 2200_000_000, this.promotionType);
            })
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 빈 문자열일 수 없습니다.");
        }

        @DisplayName("존재하지 않는 프로모션이라면 예외가 발생한다.")
        @Test
        void 존재하는_프로모션_타입이_아니라면_예외() {
        }
    }


}