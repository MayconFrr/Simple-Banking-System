package org.mayconfrr.application;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;

public class TerminalMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final Map<String, String> titleMap = new LinkedHashMap<>();
    private final Map<String, Runnable> actionMap = new HashMap<>();

    private Predicate<String> exitCondition = s -> false;
    private Runnable defaultAction = () -> {
    };

    public void addMenuItem(String key, String title, Runnable action) {
        titleMap.put(key, title);
        actionMap.put(key, action);
    }

    public void setExitCondition(Predicate<String> exitCondition) {
        this.exitCondition = exitCondition;
    }

    public void setDefaultAction(Runnable defaultAction) {
        this.defaultAction = defaultAction;
    }

    public void execute() {
        String option;
        do {
            System.out.println();
            titleMap.values()
                .forEach(System.out::println);

            System.out.print("> ");
            option = scanner.nextLine().trim();

            Runnable action = actionMap.getOrDefault(option, defaultAction);
            action.run();
        } while (!exitCondition.test(option));
    }
}


