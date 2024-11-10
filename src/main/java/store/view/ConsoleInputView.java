package store.view;

import camp.nextstep.edu.missionutils.Console;

public class ConsoleInputView {
    public String getUserInput(String placeholder) {
        System.out.println(placeholder);
        return Console.readLine().replace(" ", "");
    }
}
