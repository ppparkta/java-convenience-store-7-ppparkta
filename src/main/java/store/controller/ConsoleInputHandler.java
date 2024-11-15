package store.controller;

import java.util.List;
import store.dto.request.OrderItemInputDto;
import store.dto.response.PromotionResultDto;
import store.exception.ExceptionMessage;
import store.exception.ExceptionUtils;
import store.parser.OrderParser;
import store.view.ConsoleInputView;

public class ConsoleInputHandler {
    private final ConsoleInputView inputView;
    private final OrderParser orderParser;

    public ConsoleInputHandler() {
        this.inputView = new ConsoleInputView();
        this.orderParser = new OrderParser();
    }

    public List<OrderItemInputDto> getOrderItems() {
        String orderItemsInput = inputView.getUserInput("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        return orderParser.parseOrderItems(orderItemsInput);
    }

    public String getReceiveMorePromotion(PromotionResultDto promotionResultDto) {
        while (true) {
            try {
                String userInput = inputView.getUserInput(
                        getReceiveMorePromoionPrompt(promotionResultDto));
                validYOrN(userInput);
                return userInput;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static String getReceiveMorePromoionPrompt(PromotionResultDto promotionResultDto) {
        return String.format("\n현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)",
                promotionResultDto.productName(),
                promotionResultDto.getAdditionalReceivable());
    }

    public String getNoPromotion(PromotionResultDto promotionResultDto) {
        while (true) {
            try {
                String userInput = inputView.getUserInput(
                        getNoPromotionPrompt(promotionResultDto));
                validYOrN(userInput);
                return userInput;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static String getNoPromotionPrompt(PromotionResultDto promotionResultDto) {
        return String.format("\n현재 %s %s개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)",
                promotionResultDto.productName(),
                promotionResultDto.remainingQuantity());
    }

    public String getUserInputYesOrNo(String prompt) {
        while (true) {
            try {
                String userInput = inputView.getUserInput(prompt);
                validYOrN(userInput);
                return userInput;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void validYOrN(String userInput) {
        if (!userInput.equals("Y") && !userInput.equals("N")) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_ORDER_FORMAT);
        }
    }
}
