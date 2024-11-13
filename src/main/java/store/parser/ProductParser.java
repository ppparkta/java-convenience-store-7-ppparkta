package store.parser;

import java.util.List;
import java.util.Optional;
import store.constant.FileConfig;
import store.dto.request.ProductInputDto;
import store.exception.ExceptionMessage;
import store.exception.ExceptionUtils;

public class ProductParser {
    private static final String PRODUCT_FILE_HEADER = "name,price,quantity,promotion";

    public static List<ProductInputDto> parseProducts(List<String> inputProducts) {
        checkNullException(inputProducts);
        String header = inputProducts.get(FileConfig.FILE_HEADER.getValue());
        checkProductHeader(header);

        return inputProducts.stream().skip(1)
                .map(ProductParser::parseProductBody)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private static void checkNullException(List<String> inputProducts) {
        if (inputProducts.isEmpty()) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.EMPTY_FILE_ERROR);
        }
    }

    private static void checkProductHeader(String header) {
        if (!header.equals(PRODUCT_FILE_HEADER)) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.FILE_FORMAT_ERROR);
        }
    }

    private static Optional<ProductInputDto> parseProductBody(String line) {
        String[] productFields = line.split(",");
        if (!validateSplitBody(productFields)) {
            return Optional.empty();
        }
        return parseFields(productFields);
    }

    private static Optional<ProductInputDto> parseFields(String[] productFields) {
        try {
            String name = productFields[0];
            int price = Integer.parseInt(productFields[1]);
            int quantity = Integer.parseInt(productFields[2]);
            String promotion = getPromotionName(productFields[3]);
            return Optional.of(new ProductInputDto(name, price, quantity, promotion));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private static String getPromotionName(String promotionField) {
        if (promotionField.equals("null")) {
            return "";
        }
        return promotionField;
    }

    private static boolean validateSplitBody(String[] productFields) {
        return productFields.length == FileConfig.PRODUCT_HEADER_SIZE.getValue();
    }
}