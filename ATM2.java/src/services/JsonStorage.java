package services;

import model.*;
import interfaces.ATMRepository;
import interfaces.UserRepository;
import java.util.*;

// Polymorphism: implements two interfaces
public class JsonStorage implements ATMRepository, UserRepository {
    private Map<String, User> memoryUsers = new HashMap<>();
    private ATMState memoryATM;

    // Constructor: initializes storage
    public JsonStorage() {
        memoryATM = new ATMState(5, 5, 5, 5, 5, 100, 100, "2.0.0");
        memoryUsers.put("1001", new User("1001", "1234", 500.0));
        memoryUsers.put("1002", new User("1002", "5678", 1000.0));
    }
//method from interface or parent class
    @Override public ATMState loadATM() { return memoryATM; } // Encapsulation
    @Override public void saveATM(ATMState state) { this.memoryATM = state; }

    @Override
    public User authenticate(String acc, String pin) { // Encapsulation + Logic
        User u = memoryUsers.get(acc);
        return (u != null && u.getPin().equals(pin)) ? u : null;
    }

    @Override
    public void updateUser(User user) { memoryUsers.put(user.getAccount(), user); } // SRP

    @Override
    public void transfer(User from, String to, double amt) { // Encapsulation + Logic
        User target = memoryUsers.get(to);
        if (target == null) throw new IllegalArgumentException("Recipient not found.");
        from.transfer(amt, to); // User encapsulation
        target.receive(amt, from.getAccount());
    }
}
