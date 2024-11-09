package store.model;

import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.constant.ExceptionMessage;
import store.dto.PromotionTypeInputDto;

public class PromotionTypeManagerTest {
    @DisplayName("프로모션타입 추가 시 이름이 중복되면 예외가 발생한다.")
    @Test
    void 프로모션이름이_중복이면_예외() {
        String name = "새프로모션";
        List<PromotionTypeInputDto> promotionTypesInputDto = List.of(
                new PromotionTypeInputDto(name, 1, 1, LocalDate.now(), LocalDate.now()),
                new PromotionTypeInputDto(name, 1, 1, LocalDate.now(), LocalDate.now()));
        Assertions.assertThatThrownBy(() -> {
                    new PromotionTypeManager(promotionTypesInputDto);
                })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.DUPLICATE_PROMOTION_NAME_ERROR.getMessage());
    }
}
