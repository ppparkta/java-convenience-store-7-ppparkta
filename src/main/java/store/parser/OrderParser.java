package store.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import store.dto.OrderItemInputDto;
import store.exception.ExceptionMessage;
import store.exception.ExceptionUtils;

public class OrderParser {
    public List<OrderItemInputDto> parseOrderItems(String orderItemsInput) {
        validateBracketStructure(orderItemsInput);
        List<String> splitOrderItems = splitAndParseOrderItems(orderItemsInput);
        List<OrderItemInputDto> orderItems = new ArrayList<>();

        for (String orderItem : splitOrderItems) {
            orderItems.add(parseProduct(orderItem));
        }
        return orderItems;
    }

    private void validateBracketStructure(String orderItemsInput) {
        int bracketCount = 0;
        for (char currentPoint : orderItemsInput.toCharArray()) {
            bracketCount = getBracketCount(bracketCount, currentPoint);
        }
        if (bracketCount != 0) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_ORDER_FORMAT);
        }
    }

    private int getBracketCount(int bracketCount, char currentPoint) {
        if (currentPoint == '[') {
            bracketCount++;
        } else if (currentPoint == ']') {
            bracketCount--;
        }
        if (bracketCount < 0) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_ORDER_FORMAT);
        }
        return bracketCount;
    }

    private List<String> splitAndParseOrderItems(String orderItemsInput) {
        return Stream.of(orderItemsInput.split(","))
                .map(item -> item.replaceAll("\\[|\\]", ""))
                .collect(Collectors.toList());
    }

    private OrderItemInputDto parseProduct(String productFieldInput) {
        try {
            String[] productFields = productFieldInput.split("-");
            validateOrderFormat(productFields);
            String productName = productFields[0];
            int quantity = Integer.parseInt(productFields[1]);
            return new OrderItemInputDto(productName, quantity);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ExceptionMessage.INVALID_ORDER_FORMAT.getMessage());
        }
    }

    private static void validateOrderFormat(String[] parts) {
        if (parts.length != 2) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_ORDER_FORMAT);
        }
    }
}
