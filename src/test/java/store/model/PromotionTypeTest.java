package store.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.exception.ExceptionMessage;

public class PromotionTypeTest {
    @Nested
    @DisplayName("프로모션 타입 생성 유효성 검사")
    class PromotionTypeValidate {
        @DisplayName("프로모션 이름이 빈문자열이면 예외가 발생한다.")
        @Test
        void 프로모션이름_빈문자열_예외() {
            assertThatThrownBy(() -> {
                new PromotionType("", 1, 1, LocalDate.now(), LocalDate.now());
            })
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ExceptionMessage.INVALID_EMPTY_INPUT.getMessage());
        }

        @DisplayName("프로모션 구매수량이 1 미만이면 예외가 발생한다.")
        @Test
        void 프로모션_구매수량_1미만_예외() {
            assertThatThrownBy(() -> {
                new PromotionType("1+1", 0, 1, LocalDate.now(), LocalDate.now());
            })
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ExceptionMessage.INVALID_MIN_QUANTITY.getMessage());
        }

        @DisplayName("프로모션 혜택이 1 미만이면 예외가 발생한다.")
        @Test
        void 프로모션_혜택_1미만_예외() {
            assertThatThrownBy(() -> {
                new PromotionType("1+1", 1, 0, LocalDate.now(), LocalDate.now());
            })
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ExceptionMessage.INVALID_MIN_QUANTITY.getMessage());
        }

        @DisplayName("startDate가 endDate보다 뒤면 예외가 발생한다.")
        @Test
        void 시작일이_종료일보다_뒤면_예외() {
            assertThatThrownBy(() -> {
                new PromotionType("1+1", 1, 1, LocalDate.now(), LocalDate.now().minusDays(1));
            })
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ExceptionMessage.INVALID_PROMOTION_START_DATE.getMessage());
        }

        @DisplayName("endDate가 현재 날짜 이전이면 예외가 발생한다.")
        @Test
        void 지난날짜_생성_예외() {
            assertThatThrownBy(() -> {
                new PromotionType("1+1", 1, 1, LocalDate.now().minusDays(2), LocalDate.now().minusDays(1));
            })
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ExceptionMessage.INVALID_PROMOTION_END_DATE.getMessage());
        }
    }
}
