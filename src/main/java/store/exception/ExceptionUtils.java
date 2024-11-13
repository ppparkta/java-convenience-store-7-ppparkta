package store.exception;

public class ExceptionUtils {
    public static void throwIllegalArgumentException(ExceptionMessage exceptionMessage) {
        throw new IllegalArgumentException(exceptionMessage.getMessage());
    }
}
