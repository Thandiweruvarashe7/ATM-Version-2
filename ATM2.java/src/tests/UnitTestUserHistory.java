package tests;

import model.User;

public class UnitTestUserHistory {
    public static void main(String[] args) {
        System.out.println("=== Unit Test: User History Recording ===");

        // Setup: Fresh user
        User user = new User("HistoryTest", "0000", 100.0);

        // Action: Deposit money
        user.deposit(50.0);

        // Assert: Check balance and history list
        assert user.getBalance() == 150.0 : "Balance math failed";
        assert user.getHistory().size() == 1 : "History list size should be 1";
        assert user.getHistory().get(0).contains("Deposited €50.0") : "Transaction record is missing or incorrect";

        System.out.println(" UnitTestUserHistory PASSED");
    }
}


