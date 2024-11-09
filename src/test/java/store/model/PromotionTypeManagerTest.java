package store.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.exception.ExceptionMessage;
import store.dto.PromotionTypeInputDto;

public class PromotionTypeManagerTest {
    @DisplayName("프로모션타입 추가 시 이름이 중복되면 예외가 발생한다.")
    @Test
    void 프로모션이름이_중복이면_예외() {
        String name = "새프로모션";
        List<PromotionTypeInputDto> promotionTypesInputDto = List.of(
                new PromotionTypeInputDto(name, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1)),
                new PromotionTypeInputDto(name, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1)));
        Assertions.assertThatThrownBy(() -> {
                    new PromotionTypeManager(promotionTypesInputDto);
                })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.DUPLICATE_PROMOTION_NAME_ERROR.getMessage());
    }

    @DisplayName("존재하는 프로모션 이름을 넣으면 프로모션 타입을 반환한다.")
    @Test
    void 프로모션이름으로_프로모션타입_반환() {
        // given
        List<PromotionTypeInputDto> promotionTypesInputDto = List.of(
                new PromotionTypeInputDto("1+1", 1, 1, LocalDate.now(), LocalDate.now().plusDays(1)));
        PromotionTypeManager promotionTypeManager = new PromotionTypeManager(promotionTypesInputDto);

        // when
        Optional<PromotionType> validPromotionType = promotionTypeManager.getValidPromotionType("1+1");

        // then
        Assertions.assertThat(validPromotionType.get().getName()).isEqualTo("1+1");
    }

    @DisplayName("존재하지 않는 프로모션 이름을 넣으면 예외가 발생한다.")
    @Test
    void 없는_프로모션이름으로_프로모션타입_예외() {
        List<PromotionTypeInputDto> promotionTypesInputDto = null;
        PromotionTypeManager promotionTypeManager = new PromotionTypeManager(promotionTypesInputDto);

        Assertions.assertThatThrownBy(() -> {
                    promotionTypeManager.getValidPromotionType("1+1");
                })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.INVALID_PROMOTION_NAME.getMessage());
    }
}
