package interfaces;

import model.User;

public interface UserRepository {
    User authenticate(String acc, String pin);
    void updateUser(User user);
    void transfer(User from, String toAccount, double amount);
}