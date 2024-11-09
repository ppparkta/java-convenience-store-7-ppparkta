package store.model;

import java.util.ArrayList;
import java.util.List;
import store.constant.ExceptionMessage;
import store.dto.PromotionTypeInputDto;
import store.exception.ExceptionUtils;

public class PromotionTypeManager {
    private final List<PromotionType> promotionTypes = new ArrayList<>();

    public PromotionTypeManager(List<PromotionTypeInputDto> promotionTypesInputDto) {
        for (PromotionTypeInputDto promotionTypeDto : promotionTypesInputDto) {
            validate(promotionTypeDto);
            promotionTypes.add(new PromotionType(
                    promotionTypeDto.name(),
                    promotionTypeDto.buy(),
                    promotionTypeDto.get(),
                    promotionTypeDto.startDate(),
                    promotionTypeDto.endDate()));
        }
    }

    public boolean findPromotionType(String promotionName) {
        return promotionTypes.stream().anyMatch(promotionType ->
                promotionType.isNameEqual(promotionName)
        );
    }

    private void validate(PromotionTypeInputDto promotionTypeInputDto) {
        boolean isDuplicate = promotionTypes.stream().anyMatch(promotionType ->
                promotionType.getName().equals(promotionTypeInputDto.name())
        );
        if (isDuplicate) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.DUPLICATE_PROMOTION_NAME_ERROR);
        }
    }
}
