package store.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.constant.ExceptionMessage;
import store.constant.StoreConfig;
import store.dto.ProductInputDto;
import store.exception.ExceptionUtils;

public class ProductManager {
    private final PromotionTypeManager promotionTypeManager;
    private final List<Stock> stocks = new ArrayList<>();

    public ProductManager(PromotionTypeManager promotionTypeManager) {
        this.promotionTypeManager = promotionTypeManager;
    }

    public void addProductStock(List<ProductInputDto> productsInputDto) {
        for (ProductInputDto productInput : productsInputDto) {

            // todo: 프로모션타입을 불러온다.
            Optional<PromotionType> matchingPromotionType = promotionTypeManager.findMatchingPromotionType(
                    productInput.name());

            // todo: 프로모션타입이 빈 값이 아닐 때 프로모션이 null인지 검사한다 -> null 바로 예외처리
            if (!productInput.promotion().isEmpty() && !matchingPromotionType.isPresent()) {
                ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_PROMOTION_NAME);
            }

            // todo: 이름이 같은 모든 상품을 받아온다
            List<Product> matchingProducts = findMatchingProducts(productInput.name());

            // todo: 중복된 상품명이 있는지 검사한다
            if (matchingProducts.isEmpty()) {
                createProductAndStock(productInput, matchingPromotionType);
                continue;
            }

            // todo: 가격이 같은지 검사한다 -> 가격 다르면 예외 발생
            if (matchingProducts.stream()
                    .anyMatch(matchingProduct -> matchingProduct.getPrice() != productInput.price())) {
                ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.INVALID_PRODUCT_PRICE);
            }

            // todo: 프로모션이 같은지 검사한다 (빈 문자열은 null과 같은 것으로 판단한다)
            // todo: 프로모션이 같다면 재고 수량을 합산하고 다음으로 넘어간다.
            if (matchingProducts.stream()
                    .anyMatch(matchingProduct -> matchingProduct.isSamePromotionType(productInput.promotion()))) {
                stocks.stream()
                        .filter(stock -> stock.isNameEqual(productInput.name()))
                        .forEach(stock -> stock.addQuantity(productInput.quantity()));
                continue;
            }

            // todo: 프로모션이 다르다면 해당 상품의 종류가 2개이상인지 확인한다.
            if (matchingProducts.size() >= StoreConfig.PRODUCT_MAX_COUNT.getValue()) {
                ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.EXCEEDED_MAX_PRODUCT_PROMOTION_LIMIT);
            }

            // todo: 상품 종류가 1개이지만 그 상품이 프로모션이고 현재 등록하려고 하는 상품에도 프로모션이 있다면 예외처리
            if (matchingProducts.size() == StoreConfig.PROMOTION_TYPE_MAX_COUNT.getValue()
                    && !productInput.promotion().isEmpty()) {
                ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.MAX_PROMOTION_TYPES_PER_PRODUCT_EXCEEDED);
            }
            createProductAndStock(productInput, matchingPromotionType);
        }
    }

    private void createProductAndStock(ProductInputDto productInput, Optional<PromotionType> matchingPromotionType) {
        Product product = new Product(productInput.name(), productInput.price(),
                matchingPromotionType.orElse(null));
        stocks.add(new Stock(product, productInput.quantity()));
    }

    private List<Product> findMatchingProducts(String productName) {
        return stocks.stream()
                .filter(stock -> stock.isNameEqual(productName))
                .map(stock -> stock.getProduct())
                .toList();
    }
}
