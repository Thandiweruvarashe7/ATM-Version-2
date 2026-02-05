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

            System.out.println("\n--- Maintenance & Diagnostics ---");
            System.out.println("1. Display Note Stock");
            System.out.println("2. Add Cash Notes");
            System.out.println("3. System Diagnostics (View Levels)");
            System.out.println("4. Firmware Update");
            System.out.println("5. Refill Ink Quantity");
            System.out.println("6. Refill Paper Quantity");
            System.out.println("0. Exit Technician Mode");
            System.out.print("Choice: ");

            String choice = sc.next();
            if (choice.equals("0")) break;

            switch (choice) {
                case "1" -> {
                    System.out.println("\n--- Stock ---");
                    System.out.println("100€: " + state.getCountFor(100) + " | 50€: " + state.getCountFor(50) +
                            " | 20€: " + state.getCountFor(20) + " | 10€: " + state.getCountFor(10) +
                            " | 5€: " + state.getCountFor(5));
                }
                case "2" -> {
                    System.out.print("Denomination (5, 10, 20, 50, 100): ");
                    int denom = readInt();
                    System.out.print("Quantity: ");
                    int qty = readInt();
                    state.addNotes(denom, qty);
                    System.out.println("Cash restocked.");
                }
                // Inside the switch statement in start()
                case "3" -> {
                    System.out.println("\n--- Diagnostics ---");
                    System.out.println("Dispenser: OK | Sensors: OK");
                    System.out.println("Firmware Version: " + state.getFirmware()); // ADDED THIS LINE
                    System.out.println("Current Ink Level: " + state.getInkAmount());
                    System.out.println("Current Paper Level: " + state.getPaperAmount());
                }

                case "4" -> {
                    System.out.print("New Version: ");
                    String ver = sc.next();
                    state = new ATMState(state.getCountFor(100), state.getCountFor(50),
                            state.getCountFor(20), state.getCountFor(10),
                            state.getCountFor(5), state.getPaperAmount(),
                            state.getInkAmount(), ver);
                    System.out.println("Firmware Updated.");
                }
                case "5" -> {
                    System.out.print("Enter amount of Ink to add: ");
                    int inkToAdd = readInt();
                    state.setInkAmount(state.getInkAmount() + inkToAdd);
                    System.out.println("Ink refilled.");
                }
                case "6" -> {
                    System.out.print("Enter amount of Paper to add: ");
                    int paperToAdd = readInt();
                    state.setPaperAmount(state.getPaperAmount() + paperToAdd);
                    System.out.println("Paper refilled.");
                }
                default -> System.out.println("Invalid option.");
            }

            // Critical: Saves the updated state (ink/paper/cash) back to JSON
            atmRepo.saveATM(state);

            System.out.print("\nAnother task? (yes/no): ");
            if (!validateYesNo()) technicianActive = false;
        }
    }

    private int readInt() {
        while (true) {
            try { return sc.nextInt(); }
            catch (Exception e) {
                System.out.print("Invalid input. Please input figures: ");
                sc.next();
            }
        }
    }

    private boolean validateYesNo() {
        while (true) {
            String input = sc.next().toLowerCase();
            if (input.equals("yes")) return true;
            if (input.equals("no")) return false;
            System.out.print("Wrong input. Input (yes/no): ");
        }
    }
}
