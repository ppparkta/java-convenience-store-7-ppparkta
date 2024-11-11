package store.view;

import java.util.List;
import java.util.Optional;
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

    public void printReceipt() {
    }
}

