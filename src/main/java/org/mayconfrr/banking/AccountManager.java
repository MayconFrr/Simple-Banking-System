package org.mayconfrr.banking;

import java.sql.*;

public class AccountManager {
    private final String databaseURL;
    private Account logged = null;

    public AccountManager(String databaseURL) throws SQLException {
        this.databaseURL = databaseURL;

        try (Connection connection = DriverManager.getConnection(databaseURL);
             Statement statement = connection.createStatement()) {

            String createTableStatement = "CREATE TABLE IF NOT EXISTS card ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "number TEXT,"
                + "pin TEXT,"
                + "balance INTEGER DEFAULT 0"
                + ");";

            statement.execute(createTableStatement);
        }
    }

    boolean containsCardNumber(String cardNumber) throws SQLException {
        String cardNumberQuery = "SELECT number FROM card WHERE number = ?";

        try (Connection connection = DriverManager.getConnection(databaseURL);
             PreparedStatement preparedStatement = connection.prepareStatement(cardNumberQuery)) {

            preparedStatement.setString(1, cardNumber);
            ResultSet numbers = preparedStatement.executeQuery();
            return numbers.next();
        }
    }

    Account createAccount() throws SQLException {
        String cardNumber;
        do {
            cardNumber = CardUtils.generateCardNumber();
        } while (containsCardNumber(cardNumber));
        String pin = CardUtils.generatePIN();

        String insertAccount = "INSERT INTO card (number, pin) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(databaseURL);
             PreparedStatement statement = connection.prepareStatement(insertAccount)) {

            statement.setString(1, cardNumber);
            statement.setString(2, pin);
            statement.executeUpdate();
        }

        return new Account(cardNumber, pin);
    }

    boolean logIn(String cardNumber, String pin) throws SQLException {
        String cardNumberQuery = "SELECT number, pin, balance FROM card WHERE number = ? AND pin = ?";

        try (Connection connection = DriverManager.getConnection(databaseURL);
             PreparedStatement statement = connection.prepareStatement(cardNumberQuery)) {

            statement.setString(1, cardNumber);
            statement.setString(2, pin);
            ResultSet cardNumbers = statement.executeQuery();

            if (cardNumbers.next()) {
                logged = new Account(cardNumbers.getString(1), cardNumbers.getString(2), cardNumbers.getInt(3));
                return true;
            }
        }
        return false;
    }

    int getLoggedAccountBalance() {
        return logged.getBalance();
    }

    void addIncomeToLoggedAccount(int income) throws SQLException {
        String balanceUpdate = "UPDATE card SET balance = balance + ? WHERE number = ?";

        try (Connection connection = DriverManager.getConnection(databaseURL);
             PreparedStatement statement = connection.prepareStatement(balanceUpdate)) {

            statement.setInt(1, income);
            statement.setString(2, logged.getCardNumber());
            statement.executeUpdate();

            logged.addBalance(income);
        }
    }

    void transferFromLoggedAccountTo(String cardNumber, int amount) throws SQLException {
        String fromUpdate = "UPDATE card SET balance = balance - ? WHERE number = ?";
        String toUpdate = "UPDATE card SET balance = balance + ? WHERE number = ?";

        try (Connection connection = DriverManager.getConnection(databaseURL);
             PreparedStatement fromStatement = connection.prepareStatement(fromUpdate);
             PreparedStatement toStatement = connection.prepareStatement(toUpdate)) {

            connection.setAutoCommit(false);
            {
                fromStatement.setInt(1, amount);
                fromStatement.setString(2, logged.getCardNumber());
                fromStatement.executeUpdate();

                toStatement.setInt(1, amount);
                toStatement.setString(2, cardNumber);
                toStatement.executeUpdate();
            }
            connection.setAutoCommit(true);
            logged.removeBalance(amount);
        }
    }

    void deleteLoggedAccount() throws SQLException {
        String accountDelete = "DELETE FROM card WHERE number = ?";

        try (Connection connection = DriverManager.getConnection(databaseURL);
             PreparedStatement statement = connection.prepareStatement(accountDelete)) {

            statement.setString(1, logged.getCardNumber());
            statement.executeUpdate();

            logOut();
        }
    }

    void logOut() {
        logged = null;
    }
}
