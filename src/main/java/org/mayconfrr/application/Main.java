package org.mayconfrr.application;

import org.mayconfrr.banking.AccountManager;
import org.mayconfrr.banking.SystemManager;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        String filepath = ArgumentParser.parseArgument(args, "-fileName");
        String url = "jdbc:sqlite:" + filepath;

        try {
            SystemManager systemManager = new SystemManager(new AccountManager(url));

            TerminalMenu accountMenu = new TerminalMenu();
            accountMenu.addMenuItem("1", "1. Balance", systemManager::balance);
            accountMenu.addMenuItem("2", "2. Add income", systemManager::addIncome);
            accountMenu.addMenuItem("3", "3. Do transfer", systemManager::doTransfer);
            accountMenu.addMenuItem("4", "4. Close account", systemManager::closeAccount);
            accountMenu.addMenuItem("5", "5. Log out", systemManager::logOut);
            accountMenu.addMenuItem("0", "0. Exit", systemManager::exit);
            accountMenu.setExitCondition(s -> "4".equals(s) || "5".equals(s));

            TerminalMenu mainMenu = new TerminalMenu();
            mainMenu.addMenuItem("1", "1. Create an account", systemManager::createAccount);
            mainMenu.addMenuItem("2", "2. Log into account", () -> {
                if (systemManager.logIntoAccount()) {
                    accountMenu.execute();
                }
            });
            mainMenu.addMenuItem("0", "0. Exit", systemManager::exit);

            mainMenu.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}