package store.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.dto.ProductInputDto;
import store.model.validator.ProductManagerValidator;

public class ProductManager {
    private final PromotionTypeManager promotionTypeManager;
    private final List<Stock> stocks = new ArrayList<>();

    public ProductManager(PromotionTypeManager promotionTypeManager) {
        this.promotionTypeManager = promotionTypeManager;
    }

    public void addProductStock(List<ProductInputDto> productsInputDto) {
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

    private List<Product> findMatchingProducts(String productName) {
        return stocks.stream()
                .filter(stock -> stock.isNameEqual(productName))
                .map(Stock::getProduct)
                .toList();
    }

    private void handleExistingProducts(ProductInputDto productInput, List<Product> matchingProducts,
                                        Optional<PromotionType> matchingPromotionType) {
        ProductManagerValidator.validateProductPrice(productInput.price(), matchingProducts);

        if (promotionTypeManager.isPromotionTypeMatched(productInput.promotion(), matchingProducts)) {
            addStockQuantity(productInput);
            return;
        }
        ProductManagerValidator.validateProductVariety(matchingProducts, productInput.promotion());
        createProductAndStock(productInput, matchingPromotionType);
    }

    private void addStockQuantity(ProductInputDto productInput) {
        stocks.stream()
                .filter(stock -> stock.isNameEqual(productInput.name()))
                .forEach(stock -> stock.addQuantity(productInput.quantity()));
    }

    private void createProductAndStock(ProductInputDto productInput, Optional<PromotionType> matchingPromotionType) {
        Product product = new Product(productInput.name(), productInput.price(),
                matchingPromotionType.orElse(null));
        stocks.add(new Stock(product, productInput.quantity()));
    }
}
