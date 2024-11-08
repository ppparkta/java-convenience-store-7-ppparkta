package store.exception;

import store.constant.ExceptionMessage;

public class ExceptionUtils {
    public static void throwIllegalArgumentException(ExceptionMessage exceptionMessage) {
        throw new IllegalArgumentException(exceptionMessage.getMessage());
    }
}
