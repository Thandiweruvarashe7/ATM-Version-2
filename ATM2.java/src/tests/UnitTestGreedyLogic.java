package tests;

import model.ATMState;
import java.util.Map;

public class UnitTestGreedyLogic {
    public static void main(String[] args) {
        System.out.println("=== Unit Test: Greedy Note Allocation ===");

        // Setup: 10 notes of each (100, 50, 20, 10, 5), 100 paper, 100 ink
        ATMState atm = new ATMState(10, 10, 10, 10, 10, 100, 100, "2.0.0");

        // Action: Request 180 Euro
        Map<Integer, Integer> allocation = atm.simulateAllocationForAmount(180);

        // Check if null
        if (allocation == null) {
            System.out.println("FAILED: Allocation is null!");
            return;
        }

        // Print what we got for the presentation
        System.out.println("Requested: 180 Euro");
        System.out.println("ATM dispensed: " + allocation);

        // Validation logic
        boolean passed = true;
        if (allocation.get(100) != 1) passed = false;
        if (allocation.get(50) != 1) passed = false;
        if (allocation.get(20) != 1) passed = false;
        if (allocation.get(10) != 1) passed = false;

        if (passed) {
            System.out.println("UnitTestGreedyLogic PASSED");
        } else {
            System.out.println("UnitTestGreedyLogic FAILED: Incorrect note count.");
        }
    }
}
