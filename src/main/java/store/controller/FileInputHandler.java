package store.controller;

import java.util.List;
import store.dto.request.ProductInputDto;
import store.dto.request.PromotionTypeInputDto;
import store.parser.ProductParser;
import store.parser.PromotionParser;
import store.view.FileInputView;

public class FileInputHandler {
    private static final String PROMOTION_FILE_NAME = "promotions.md";
    private static final String PRODUCT_FILE_NAME = "products.md";
    private final FileInputView fileInputView;

    public FileInputHandler() {
        this.fileInputView = new FileInputView();
    }

    public List<PromotionTypeInputDto> getPromotions() {
        List<String> inputPromotions = fileInputView.getInput(PROMOTION_FILE_NAME);
        return PromotionParser.parsePromotions(inputPromotions);
    }

    public List<ProductInputDto> getProducts() {
        List<String> inputProducts = fileInputView.getInput(PRODUCT_FILE_NAME);
        return ProductParser.parseProducts(inputProducts);
    }
}
