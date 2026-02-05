package model;

import java.util.List;
import java.util.ArrayList;

public class User {
    private String account; // Encapsulation
    private String pin;
    private double balance;
    private List<String> history;

    // Constructor overloading (Polymorphism)
    public User(String account, String pin, double balance) {
        this.account = account;
        this.pin = pin;
        this.balance = balance;
        this.history = new ArrayList<>();
    }

    public User(String account, String pin, double balance, List<String> history) {
        this.account = account;
        this.pin = pin;
        this.balance = balance;
        this.history = history != null ? history : new ArrayList<>();
    }

    // Encapsulation: getters
    public String getAccount() { return account; }
    public String getPin() { return pin; }
    public double getBalance() { return balance; }

    // Business logic encapsulated
    public void withdraw(double amount) {
        balance -= amount; // Logic
        history.add("Withdrew €" + amount); // Encapsulation: update history internally
    }

    public void deposit(double amount) {
        balance += amount;
        history.add("Deposited €" + amount);
    }

    public void transfer(double amount, String toAccount) {
        balance -= amount;
        history.add("Transferred €" + amount + " to " + toAccount);
    }

    public void receive(double amount, String fromAccount) {
        balance += amount;
        history.add("Received €" + amount + " from " + fromAccount);
    }

    public List<String> getHistory() { return history; } // Encapsulation
}

