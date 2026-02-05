package services;

import model.*;
import interfaces.ATMRepository;
import interfaces.UserRepository;
import java.util.*;

public class JsonStorage implements ATMRepository, UserRepository {
    private Map<String, User> memoryUsers = new HashMap<>();
    private ATMState memoryATM;

    public JsonStorage() {
        // Init with 5 of each note as requested
        memoryATM = new ATMState(5, 5, 5, 5, 5, 100, 100, "2.0.0");
        memoryUsers.put("1001", new User("1001", "1234", 500.0));
        memoryUsers.put("1002", new User("1002", "5678", 1000.0));
    }

    @Override public ATMState loadATM() { return memoryATM; }
    @Override public void saveATM(ATMState state) { this.memoryATM = state; }
    @Override public User authenticate(String acc, String pin) {
        User u = memoryUsers.get(acc);
        return (u != null && u.getPin().equals(pin)) ? u : null;
    }
    @Override public void updateUser(User user) { memoryUsers.put(user.getAccount(), user); }
    @Override public void transfer(User from, String to, double amt) {
        User target = memoryUsers.get(to);
        if (target == null) throw new IllegalArgumentException("Recipient not found.");
        from.transfer(amt, to);
        target.receive(amt, from.getAccount());
    }
}