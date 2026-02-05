package tests;

import model.User;
import services.JsonStorage;

public class IntegrationTestTransfer {
    public static void main(String[] args) {
        System.out.println("=== Integration Test: Account Transfer ===");

        JsonStorage storage = new JsonStorage();
        
        // Setup: Get two existing accounts
        User sender = storage.authenticate("1002", "5678"); // 1000.0
        User receiver = storage.authenticate("1001", "1234"); // 500.0

        // Execution: Perform transfer
        storage.transfer(sender, "1001", 200.0);

        // Verification: Check memory and storage state
        assert sender.getBalance() == 800.0 : "Sender balance incorrect after transfer";
        assert storage.authenticate("1001", "1234").getBalance() == 700.0 : "Receiver balance not updated in storage";

        System.out.println("IntegrationTestTransfer PASSED");
    }
}


