package tests;

import interfaces.ATMService;
import model.ATMState;
import model.User;
import services.ATMServiceImpl;
import services.JsonStorage;

public class IntegrationTestUserFlow {
    public static void main(String[] args) {
        System.out.println("=== Integration Test: Withdrawal Flow ===");

        JsonStorage storage = new JsonStorage();
        ATMService atmService = new ATMServiceImpl(storage);

        // Load real data from storage
        User user = storage.authenticate("1001", "1234");
        ATMState atm = storage.loadATM();

        double userBalanceBefore = user.getBalance();
        double atmCashBefore = atm.getCashAmount();

        // Execution: Service coordinates between User and ATMState
        boolean success = atmService.withdraw(user, atm, 100);

        assert success : "Withdrawal should have succeeded";
        assert user.getBalance() == userBalanceBefore - 100 : "User balance was not updated in integration";
        assert atm.getCashAmount() == atmCashBefore - 100 : "ATM cash state was not updated in integration";

        System.out.println("IntegrationTestUserFlow PASSED");
    }
}


