package services;

import model.ATMState;
import interfaces.ATMRepository;
import java.util.Scanner;
import java.util.InputMismatchException;

public class TechnicianService {
    private ATMRepository atmRepo;
    private Scanner sc = new Scanner(System.in);

    public TechnicianService(ATMRepository atmRepo) {
        this.atmRepo = atmRepo;
    }

    public void start() {
        System.out.print("Technician PIN: ");
        if (!sc.next().equals("9999")) {
            System.out.println("Access Denied.");
            return;
        }

        boolean technicianActive = true;
        while (technicianActive) {
            ATMState state = atmRepo.loadATM();
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
                case "1" -> {
                    System.out.println("\n--- Current Cash Stock ---");
                    System.out.println("100€: " + state.getCountFor(100) + " | 50€: " + state.getCountFor(50) +
                            " | 20€: " + state.getCountFor(20) + " | 10€: " + state.getCountFor(10) +
                            " | 5€: " + state.getCountFor(5));
                }
                case "2" -> {
                    System.out.print("Note Denomination (5, 10, 20, 50, 100): ");
                    int denom = readInt();
                    System.out.print("Quantity to add: ");
                    int qty = readInt();
                    state.addNotes(denom, qty);
                    System.out.println("Cash restocked successfully.");
                }
                case "3" -> {
                    System.out.println("Running remote on-site diagnostics...");
                    System.out.println("Dispenser: OK | Sensors: OK | Encryption: SECURE");
                }
                case "4" -> {
                    System.out.println("Initiating hardware upgrade... Replacing Dispenser Unit.");
                    System.out.println("Upgrade Complete.");
                }
                case "5" -> {
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

            // Save state after any change
            atmRepo.saveATM(state);

            // THE FIX: Ask technician if they want to continue or exit
            System.out.print("\nPerform another maintenance task? (yes/no): ");
            if (!validateYesNo()) {
                System.out.println("Exiting Maintenance Mode...");
                technicianActive = false;
            }
        }
    }

    private int readInt() {
        while (true) {
            try {
                return sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please input figures or numbers: ");
                sc.next();
            }
        }
    }

    private boolean validateYesNo() {
        while (true) {
            String input = sc.next().toLowerCase();
            if (input.equals("yes")) return true;
            if (input.equals("no")) return false;
            System.out.print("Wrong input. Input correct option (yes/no): ");
        }
    }
}