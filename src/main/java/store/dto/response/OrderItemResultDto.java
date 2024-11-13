package store.dto.response;

public record OrderItemResultDto(String productName, int quantity, long totalPrice) {
}
