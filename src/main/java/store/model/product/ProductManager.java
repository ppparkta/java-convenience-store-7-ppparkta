package store.model.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import store.dto.request.ProductInputDto;
import store.exception.ExceptionMessage;
import store.exception.ExceptionUtils;

public class ProductManager {
    private final PromotionTypeManager promotionTypeManager;
    private final List<Stock> stocks = new ArrayList<>();

    public ProductManager(PromotionTypeManager promotionTypeManager, List<ProductInputDto> productInputDtos) {
        this.promotionTypeManager = promotionTypeManager;
        addProductStock(productInputDtos);
        addRegularProductsIfOnlyPromotions();
    }

    public List<Stock> getStocks() {
        return List.copyOf(stocks);
    }

    public List<Product> findMatchingProducts(String productName) {
        return stocks.stream()
                .filter(stock -> stock.isNameEqual(productName))
                .map(Stock::getProduct)
                .toList();
    }

    public int getProductTotalQuantity(String productName) {
        return stocks.stream()
                .filter(stock -> stock.isNameEqual(productName))
                .mapToInt(Stock::getQuantity)
                .sum();
    }

    public int getPromotionProductQuantity(String productName) {
        return stocks.stream()
                .filter(stock -> stock.isNameEqual(productName))
                .filter(stock -> stock.isPromotionStock())
                .mapToInt(Stock::getQuantity)
                .sum();
    }

    public List<Stock> findStocksByProductName(String productName) {
        return stocks.stream()
                .filter(stock -> stock.isNameEqual(productName))
                .sorted()
                .toList();
    }

    private void addProductStock(List<ProductInputDto> productsInputDto) {
        if (productsInputDto == null) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.NULL_VALUE_ERROR);
        }
        for (ProductInputDto productInput : productsInputDto) {
            processProductInput(productInput);
        }
    }

    private void processProductInput(ProductInputDto productInput) {
        Optional<PromotionType> matchingPromotionType = promotionTypeManager.getValidPromotionType(
                productInput.promotion());
        List<Product> matchingProducts = findMatchingProducts(productInput.name());

        if (matchingProducts.isEmpty()) {
            createProductAndStock(productInput, matchingPromotionType);
            return;
        }
        handleExistingProducts(productInput, matchingProducts, matchingPromotionType);
    }

    private void handleExistingProducts(ProductInputDto productInput, List<Product> matchingProducts,
                                        Optional<PromotionType> matchingPromotionType) {
        ProductManagerValidator.validateProductPrice(productInput.price(), matchingProducts);

        if (promotionTypeManager.isPromotionTypeMatched(productInput.name(), productInput.promotion(),
                matchingProducts)) {
            addStockQuantity(productInput);
            return;
        }
        ProductManagerValidator.validateProductVariety(matchingProducts, productInput.promotion());
        createProductAndStock(productInput, matchingPromotionType);
    }

    private void addStockQuantity(ProductInputDto productInput) {
        List<Stock> samePromotionStocks = stocks.stream()
                .filter(stock -> stock.getProduct().isSamePromotionType(productInput.name(), productInput.promotion()))
                .toList();
        samePromotionStocks.getFirst().addQuantity(productInput.quantity());
    }

    private void createProductAndStock(ProductInputDto productInput, Optional<PromotionType> matchingPromotionType) {
        Product product = new Product(productInput.name(), productInput.price(),
                matchingPromotionType.orElse(null));
        stocks.add(new Stock(product, productInput.quantity()));
    }

    private void addRegularProductsIfOnlyPromotions() {
        stocks.stream()
                .collect(Collectors.groupingBy(stock -> stock.getProduct().getName()))
                .values()
                .stream()
                .filter(stockList -> hasPromotionOnly(stockList) && !hasRegularProduct(stockList))
                .forEach(stockList -> {
                    Product regularProduct = createRegularProductFromFirstPromotion(stockList);
                    stocks.add(new Stock(regularProduct, 0));
                });
    }

    private boolean hasPromotionOnly(List<Stock> stockList) {
        return stockList.stream().anyMatch(stock -> stock.getProduct().getPromotionType().isPresent());
    }

    private boolean hasRegularProduct(List<Stock> stockList) {
        return stockList.stream().anyMatch(stock -> stock.getProduct().getPromotionType().isEmpty());
    }

    private Product createRegularProductFromFirstPromotion(List<Stock> stockList) {
        return stockList.stream()
                .filter(stock -> stock.getProduct().getPromotionType().isPresent())
                .findFirst()
                .map(stock -> new Product(stock.getProduct().getName(), stock.getProduct().getPrice(), null))
                .orElseThrow(() -> new IllegalStateException(ExceptionMessage.NULL_VALUE_ERROR.getMessage()));
    }
}
