package org.mayconfrr.banking;

class Account {
    private final String cardNumber;
    private final String pin;
    private int balance;

    Account(String cardNumber, String pin, int balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    Account(String cardNumber, String pin) {
        this(cardNumber, pin, 0);
    }

    String getCardNumber() {
        return cardNumber;
    }

    String getPin() {
        return pin;
    }

    void addBalance(int amount) {
        this.balance += amount;
    }

    void removeBalance(int amount) {
        this.balance -= amount;
    }

    int getBalance() {
        return balance;
    }
}
