package store.parser;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.constant.FileConfig;
import store.dto.PromotionTypeInputDto;
import store.exception.ExceptionMessage;
import store.exception.ExceptionUtils;

public class PromotionParser {
    private static final String PROMOTION_FILE_HEADER = "name,buy,get,start_date,end_date";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static List<PromotionTypeInputDto> parsePromotions(List<String> inputPromotions) {
        checkNullException(inputPromotions);
        String header = inputPromotions.get(FileConfig.FILE_HEADER.getValue());
        checkPromotionHeader(header);

        return inputPromotions.stream().skip(1)
                .map(PromotionParser::parsePromotionBody)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private static void checkNullException(List<String> inputPromotions) {
        if (inputPromotions.isEmpty()) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.EMPTY_FILE_ERROR);
        }
    }

    private static void checkPromotionHeader(String header) {
        if (!header.equals(PROMOTION_FILE_HEADER)) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.FILE_FORMAT_ERROR);
        }
    }

    private static Optional<PromotionTypeInputDto> parsePromotionBody(String line) {
        String[] promotionField = line.split(",");
        if (!validateSplitBody(promotionField)) {
            return Optional.empty();
        }
        return parseField(promotionField);
    }

    private static Optional<PromotionTypeInputDto> parseField(String[] promotionField) {
        try {
            String name = promotionField[0];
            int buy = Integer.parseInt(promotionField[1]);
            int get = Integer.parseInt(promotionField[2]);
            LocalDate startDate = LocalDate.parse(promotionField[3], DATE_FORMATTER);
            LocalDate endDate = LocalDate.parse(promotionField[4], DATE_FORMATTER);
            return Optional.of(new PromotionTypeInputDto(name, buy, get, startDate, endDate));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static boolean validateSplitBody(String[] promotionField) {
        return promotionField.length == FileConfig.PROMOTION_HEADER_SIZE.getValue();
    }
}

