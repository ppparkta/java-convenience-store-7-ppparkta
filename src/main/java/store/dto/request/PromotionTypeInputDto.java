package store.dto.request;

import java.time.LocalDate;

public record PromotionTypeInputDto(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {
}
