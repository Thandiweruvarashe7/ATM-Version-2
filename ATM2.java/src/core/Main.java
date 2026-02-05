package core;

import services.*;
import model.*;
import interfaces.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        JsonStorage storage = new JsonStorage();
        ATMService atmService = new ATMServiceImpl(storage);
        TechnicianService technicianService = new TechnicianService(storage);
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n*******************************");
            System.out.println("* Welcome to Version 2 ATM    *");
            System.out.println("*******************************");
            System.out.println("1. Customer Login");
            System.out.println("2. Technician Login");
            System.out.println("0. Exit");
            System.out.print("Choice: ");

            String choice = sc.next();
            if (choice.equals("1")) {
                customerLogin(sc, atmService, storage, storage);
            } else if (choice.equals("2")) {
                technicianService.start();
            } else if (choice.equals("0")) {
                System.out.println("System shutting down...");
                System.exit(0);
            } else {
                System.out.println("Invalid choice. Please select 1, 2, or 0.");
            }
        }
    }

    private static void customerLogin(Scanner sc, ATMService atmService, UserRepository userRepo, ATMRepository atmRepo) {
        System.out.print("Account: ");
        String acc = sc.next();
        System.out.print("PIN: ");
        User user = userRepo.authenticate(acc, sc.next());
        if (user == null) {
            System.out.println("Login failed.");
            return;
        }
        customerMenu(sc, atmService, userRepo, atmRepo, user);
    }

    private static void customerMenu(Scanner sc, ATMService atmService, UserRepository userRepo, ATMRepository atmRepo, User user) {
        boolean sessionActive = true;
        while (sessionActive) {
            System.out.println("\n--- Welcome, " + user.getAccount() + " ---");
            System.out.println("1. Balance\n2. Withdraw\n3. Deposit\n4. Transfer\n5. History\n6. Logout/Exit");
            System.out.print("Choice: ");

            String choice = sc.next();
            switch (choice) {
                case "1" -> System.out.println("Current Balance: " + user.getBalance() + " Euro");
                case "2" -> withdrawAction(sc, atmService, atmRepo, userRepo, user);
                case "3" -> depositAction(sc, userRepo, user);
                case "4" -> transferAction(sc, userRepo, atmService, user);
                case "5" -> user.getHistory().forEach(h -> System.out.println("- " + h));
                case "6" -> { sessionActive = false; continue; }
                default -> { System.out.println("Invalid option."); continue; }
            }

            System.out.print("\nWould you like to do another action? (yes/no): ");
            if (!validateYesNo(sc)) {
                System.out.println("Thank you for using our ATM. Goodbye!");
                sessionActive = false;
            }
        }
    }

    // --- UPDATED TRANSFER ACTION WITH FUND CHECK ---
    private static void transferAction(Scanner sc, UserRepository repo, ATMService svc, User user) {
        System.out.print("Recipient Account: ");
        String to = sc.next();
        System.out.print("Amount to Transfer (Euro): ");
        double amt = readDouble(sc);

        // Check for insufficient funds
        if (amt > user.getBalance()) {
            System.out.println("Transfer Failed: Insufficient funds.");
            return;
        }

        try {
            repo.transfer(user, to, amt);
            System.out.println("Transfer done successfully.");
            offerReceipt(sc, svc);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void withdrawAction(Scanner sc, ATMService atmService, ATMRepository atmRepo, UserRepository userRepo, User user) {
        System.out.print("Enter amount: ");
        int amount = readInt(sc);
        ATMState state = atmRepo.loadATM();

        if (amount > user.getBalance()) {
            System.out.println("Error: Insufficient funds.");
            return;
        }

        List<Map<Integer, Integer>> options = state.findAllocationsForAmount(amount, 3);
        if (options.isEmpty()) {
            System.out.println("ATM cannot provide this amount with current notes.");
            System.out.print("1. Choose Other Amount\n2. Cancel and Exit\nChoice: ");
            String choice = sc.next();
            if (choice.equals("1")) withdrawAction(sc, atmService, atmRepo, userRepo, user);
            return;
        }

        System.out.println("\nSelect your note combination:");
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i).toString());
        }
        System.out.println((options.size() + 1) + ". Cancel Transaction");

        System.out.print("Choice: ");
        int pick = readInt(sc);
        if (pick > 0 && pick <= options.size()) {
            if (atmService.withdraw(user, state, amount, options.get(pick - 1))) {
                System.out.println("Withdrawal Successful! New balance: " + user.getBalance());
                offerReceipt(sc, atmService);
            }
        } else if (pick == options.size() + 1) {
            System.out.println("Transaction Cancelled.");
        } else {
            System.out.println("Invalid option choose correct option.");
        }
    }

    private static void depositAction(Scanner sc, UserRepository repo, User user) {
        System.out.print("Deposit Amount: ");
        double amt = readDouble(sc);
        user.deposit(amt);
        repo.updateUser(user);
        System.out.println("Deposit Successful! New balance: " + user.getBalance());
    }

    private static double readDouble(Scanner sc) {
        while (true) {
            try { return sc.nextDouble(); }
            catch (Exception e) { System.out.print("Invalid input. Please input figures or numbers: "); sc.next(); }
        }
    }

    private static int readInt(Scanner sc) {
        while (true) {
            try { return sc.nextInt(); }
            catch (Exception e) { System.out.print("Invalid input. Please input figures or numbers: "); sc.next(); }
        }
    }

    private static boolean validateYesNo(Scanner sc) {
        while (true) {
            String s = sc.next().toLowerCase();
            if (s.equals("yes")) return true;
            if (s.equals("no")) return false;
            System.out.print("Wrong input. Input correct option (yes/no): ");
        }
    }

    private static void offerReceipt(Scanner sc, ATMService atmService) {
        System.out.print("Would you like a receipt? (yes/no): ");
        if (validateYesNo(sc)) {
            if (atmService.printReceipt()) System.out.println("Receipt printed.");
            else System.out.println("Error: No paper/ink.");
        }
    }
}