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

    // todo: 리팩토링
    public void printReceipt(ReceiptResultDto receiptResult) {
        int totalWidth = 36; // 기준이 되는 전체 폭

        System.out.println("==============W 편의점================");
        System.out.printf("%-20s %6s %10s%n", "상품명", "수량", "금액");

        receiptResult.orderItems().forEach(orderItem ->
                System.out.printf("%-20s %6d %10s%n",
                        orderItem.productName(),
                        orderItem.quantity(),
                        String.format("%,d", orderItem.totalPrice())) // 금액 포맷팅
        );

        if (!receiptResult.promotionBenefits().isEmpty()) {
            System.out.println("============= 증정 ===============");
            receiptResult.promotionBenefits().forEach(promotionBenefit ->
                    System.out.printf("%-20s %6d%n",
                            promotionBenefit.productName(),
                            promotionBenefit.promotionBenefitQuantity())
            );
        }

        System.out.println("====================================");
        System.out.printf("%-20s %6s %10s%n", "총구매액", "", String.format("%,d", receiptResult.totalPurchaseAmount()));
        System.out.printf("%-20s %6s -%10s%n", "행사할인", "",
                String.format("%,d", receiptResult.totalPromotionDiscount()));
        System.out.printf("%-20s %6s -%10.0f%n", "멤버십할인", "", receiptResult.membershipDiscount());
        System.out.printf("%-20s %6s %10s%n", "내실돈", "", String.format("%,d", receiptResult.finalAmount()));
    }

}

