package org.mayconfrr.banking;

import java.sql.SQLException;
import java.util.Scanner;

public class SystemManager {
    private final Scanner scanner = new Scanner(System.in);
    private final AccountManager accountManager;

    public SystemManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public void createAccount() {
        try {
            Account account = accountManager.createAccount();

            System.out.println();
            System.out.println("Your card has been created");
            System.out.println("Your card number:");
            System.out.println(account.getCardNumber());
            System.out.println("Your card PIN:");
            System.out.println(account.getPin());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean logIntoAccount() {
        try {
            System.out.println();

            System.out.println("Enter your card number:");
            String cardNumber = scanner.nextLine().trim();

            System.out.println("Enter your PIN:");
            String pin = scanner.nextLine().trim();

            if (accountManager.logIn(cardNumber, pin)) {
                System.out.println("You have successfully logged in!");
                return true;
            } else {
                System.out.println("Wrong card number or PIN!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void balance() {
        System.out.println();
        System.out.println("Balance: " + accountManager.getLoggedAccountBalance());
    }

    public void addIncome() {
        System.out.println();
        System.out.println("Enter income:");

        System.out.print("> ");
        int income = Integer.parseInt(scanner.nextLine().trim());

        try {
            accountManager.addIncomeToLoggedAccount(income);
            System.out.println("Income was added!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void doTransfer() {
        System.out.println("Enter card number:");
        String cardNumber = scanner.nextLine().trim();

        if (cardNumber.length() != 16 || CardUtils.getChecksum(cardNumber.substring(0, 15)) != cardNumber.charAt(15)) {
            System.out.println("Probably you made mistake in the card number.");
            System.out.println("Please try again!");
            return;
        }

        try {
            if (accountManager.containsCardNumber(cardNumber)) {
                System.out.println("Enter how much money you want to transfer:");
                int amount = Integer.parseInt(scanner.nextLine().trim());

                if (amount < 0) {
                    System.out.println("Stop right there you criminal scum");
                    return;
                }

                if (accountManager.getLoggedAccountBalance() < amount) {
                    System.out.println("Not enough money!");
                    return;
                }

                accountManager.transferFromLoggedAccountTo(cardNumber, amount);
                System.out.println("Success!");
            } else {
                System.out.println("Such a card does not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeAccount() {
        try {
            accountManager.deleteLoggedAccount();
            System.out.println("The account has been closed!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void logOut() {
        System.out.println();
        System.out.println("You have successfully logged out!");
        accountManager.logOut();
    }

    public void exit() {
        System.out.println();
        System.out.println("Bye!");
        accountManager.logOut();
        System.exit(0);
    }
}
