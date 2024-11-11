package store.view;

import java.util.List;
import java.util.Optional;
import store.dto.response.ReceiptResultDto;
import store.model.product.PromotionType;
import store.model.product.Stock;

public class OutputView {
    public void printWelcomeMessage() {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.");
        System.out.println();
    }

    public void printStocks(List<Stock> stocks) {
        for (Stock stock : stocks) {
            printStock(stock);
        }
        System.out.println();
    }

    private void printStock(Stock stock) {
        String productName = stock.getProduct().getName();
        long price = stock.getProduct().getPrice();
        int quantity = stock.getQuantity();
        Optional<PromotionType> promotionType = stock.getProduct().getPromotionType();

        printStockFormat(productName, price, quantity, promotionType);
    }

    private static void printStockFormat(String productName, long price, int quantity,
                                         Optional<PromotionType> promotionType) {
        String promotion = promotionType.map(PromotionType::getName).orElse("");

        if (quantity == 0) {
            System.out.println(
                    String.format("- %s %,d원 재고 없음 %s", productName, price, promotion));
        } else {
            System.out.println(
                    String.format("- %s %,d원 %,d개 %s", productName, price, quantity, promotion));
        }
    }

    public void printReceipt(ReceiptResultDto receiptResult) {
        printHeader();
        printOrderItems(receiptResult);
        printPromotionBenefits(receiptResult);
        printFooter(receiptResult);
    }

    private void printHeader() {
        System.out.println("==============W 편의점================");
        System.out.printf("%s%n", formatString("상품명", 20) + formatString("수량", 6) + formatString("금액", 10));
    }

    private void printOrderItems(ReceiptResultDto receiptResult) {
        receiptResult.orderItems().forEach(orderItem ->
                System.out.printf("%s%n", formatString(orderItem.productName(), 20)
                        + formatString(String.valueOf(orderItem.quantity()), 6)
                        + formatString(String.format("%,d", orderItem.totalPrice()), 10))
        );
    }

    private void printPromotionBenefits(ReceiptResultDto receiptResult) {
        if (!receiptResult.promotionBenefits().isEmpty()) {
            System.out.println("=============증      정===============");
            receiptResult.promotionBenefits().forEach(promotionBenefit ->
                    System.out.printf("%s%n", formatString(promotionBenefit.productName(), 20)
                            + formatString(String.valueOf(promotionBenefit.promotionBenefitQuantity()), 6))
            );
        }
    }

    private void printFooter(ReceiptResultDto receiptResult) {
        System.out.println("====================================");
        printTotalAmount(receiptResult);
        printDiscounts(receiptResult);
        printFinalAmount(receiptResult);
    }

    private void printTotalAmount(ReceiptResultDto receiptResult) {
        System.out.printf("%s%n", formatString("총구매액", 20) + formatString("", 6)
                + formatString(String.format("%,d", receiptResult.totalPurchaseAmount()), 10));
    }

    private void printDiscounts(ReceiptResultDto receiptResult) {
        System.out.printf("%s%n", formatString("행사할인", 20) + formatString("", 6)
                + formatString("-" + String.format("%,d", receiptResult.totalPromotionDiscount()), 10));
        System.out.printf("%s%n", formatString("멤버십할인", 20) + formatString("", 6)
                + formatString("-" + String.format("%.0f", receiptResult.membershipDiscount()), 10));
    }

    private void printFinalAmount(ReceiptResultDto receiptResult) {
        System.out.printf("%s%n", formatString("내실돈", 20) + formatString("", 6)
                + formatString(String.format("%,d", receiptResult.finalAmount()), 10));
    }

    private static String formatString(String input, int length) {
        if (input.length() > length) {
            return input.substring(0, length);
        }
        return String.format("%-" + length + "s", input);
    }
}

