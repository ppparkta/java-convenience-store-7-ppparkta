package store.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class PromotionTypeTest {

    @Nested
    @DisplayName("프로모션 타입 생성 유효성 검사")
    class PromotionTypeValidate {
        @DisplayName("")
        @Test
        void 프로모션이름_빈문자열_예외() {
            assertThatThrownBy(() -> {
                new PromotionType("", 1, 1, LocalDate.now(), LocalDate.now())
            })
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 빈 문자열일 수 없습니다.");
        }

        @DisplayName("")
        @Test
        void 프로모션_구매수량_1미만_예외() {
            assertThatThrownBy(() -> {
                new PromotionType("1+1", 0, 1, LocalDate.now(), LocalDate.now())
            })
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 1 이상 입력해주세요.");
        }

        @DisplayName("")
        @Test
        void 프로모션_혜택_1미만_예외() {
            assertThatThrownBy(() -> {
                new PromotionType("1+1", 1, 0, LocalDate.now(), LocalDate.now())
            })
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 1 이상 입력해주세요.");
        }
    }

}
