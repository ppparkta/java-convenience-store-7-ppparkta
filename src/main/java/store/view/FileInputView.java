package store.view;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import store.exception.ExceptionMessage;
import store.exception.ExceptionUtils;

public class FileInputView {
    public List<String> getInput(String filename) {
        List<String> inputLines = new ArrayList<>();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);
        validateInputStream(inputStream);
        try (Scanner scanner = new Scanner(inputStream)) {
            readLine(inputLines, scanner);
        } catch (Exception e) {
            System.err.println(ExceptionMessage.FILE_READ_ERROR.getMessage());
        }
        return inputLines;
    }

    private static void validateInputStream(InputStream inputStream) {
        if (inputStream == null) {
            ExceptionUtils.throwIllegalArgumentException(ExceptionMessage.FOUND_NOT_FILE);
        }
    }

    private void readLine(List<String> inputLines, Scanner scanner) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().replace(" ", "");
            inputLines.add(line);
        }
    }
}
