package services;

import model.ATMState;
import interfaces.ATMRepository;
import java.util.Scanner;
import java.util.InputMismatchException;

public class TechnicianService {
    private ATMRepository atmRepo; // Composition
    private Scanner sc = new Scanner(System.in);

    public TechnicianService(ATMRepository atmRepo) {
        this.atmRepo = atmRepo;
    }

    // Main technician loop: SRP, loop, switch, input validation
    public void start() {
        System.out.print("Technician PIN: ");
        if (!sc.next().equals("9999")) { // Logic: access control
            System.out.println("Access Denied.");
            return;
        }

        boolean technicianActive = true;
        while (technicianActive) { // Loop
            ATMState state = atmRepo.loadATM(); // Encapsulation

            // Menu options: switch + logic
            System.out.println("\n--- Maintenance, Diagnostics & Upgrades ---");
            System.out.println("1. Display Note Stock");
            System.out.println("2. Add Cash Notes");
            System.out.println("3. System Diagnostics");
            System.out.println("4. Hardware Upgrade");
            System.out.println("5. Software/Firmware Update");
            System.out.println("0. Exit Technician Mode");
            System.out.print("Choice: ");

            String choice = sc.next();
            if (choice.equals("0")) break;

            switch (choice) {
                case "1" -> { // Loop + Encapsulation
                    System.out.println("\n--- Current Cash Stock ---");
                    System.out.println("100€: " + state.getCountFor(100) + " | 50€: " + state.getCountFor(50) +
                            " | 20€: " + state.getCountFor(20) + " | 10€: " + state.getCountFor(10) +
                            " | 5€: " + state.getCountFor(5));
                }
                case "2" -> { // Input + Encapsulation
                    System.out.print("Note Denomination (5, 10, 20, 50, 100): ");
                    int denom = readInt();
                    System.out.print("Quantity to add: ");
                    int qty = readInt();
                    state.addNotes(denom, qty);
                    System.out.println("Cash restocked successfully.");
                }
                case "3" -> { // Logic simulation
                    System.out.println("Running remote on-site diagnostics...");
                    System.out.println("Dispenser: OK | Sensors: OK | Encryption: SECURE");
                }
                case "4" -> { // Logic: Hardware upgrade simulation
                    System.out.println("Initiating hardware upgrade... Replacing Dispenser Unit.");
                    System.out.println("Upgrade Complete.");
                }
                case "5" -> { // Software update logic
                    System.out.print("Enter New Firmware Version: ");
                    String ver = sc.next();
                    state = new ATMState(state.getCountFor(100), state.getCountFor(50),
                            state.getCountFor(20), state.getCountFor(10),
                            state.getCountFor(5), state.getPaperAmount(),
                            state.getInkAmount(), ver);
                    System.out.println("Firmware updated to " + ver);
                }
                default -> {
                    System.out.println("Invalid option choose correct option.");
                    continue;
                }
            }

            atmRepo.saveATM(state); // Encapsulation: save changes

            System.out.print("\nPerform another maintenance task? (yes/no): ");
            if (!validateYesNo()) { // Loop + validation
                System.out.println("Exiting Maintenance Mode...");
                technicianActive = false;
            }
        }
    }
//helper functions
    private int readInt() { // Loop + input validation
        while (true) {
            try {
                return sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please input figures or numbers: ");
                sc.next();
            }
        }
    }

    private boolean validateYesNo() { // Loop + input validation
        while (true) {
            String input = sc.next().toLowerCase();
            if (input.equals("yes")) return true;
            if (input.equals("no")) return false;
            System.out.print("Wrong input. Input correct option (yes/no): ");
        }
    }
}
