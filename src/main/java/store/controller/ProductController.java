package store.controller;

import java.util.List;
import store.dto.ProductInputDto;
import store.dto.PromotionTypeInputDto;
import store.model.PromotionTypeManager;
import store.model.product.ProductManager;

public class ProductController {
    private final FileInputHandler fileInputHandler;

    public ProductController() {
        this.fileInputHandler = new FileInputHandler();
    }

    public ProductManager initialize() {
        List<PromotionTypeInputDto> promotionTypeInputDtos = fileInputHandler.getPromotions();
        PromotionTypeManager promotionTypeManager = new PromotionTypeManager(promotionTypeInputDtos);

        List<ProductInputDto> productInputDtos = fileInputHandler.getProducts();
        return new ProductManager(promotionTypeManager, productInputDtos);
    }
}
