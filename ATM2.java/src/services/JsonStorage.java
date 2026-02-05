package services;

import model.*;
import interfaces.ATMRepository;
import interfaces.UserRepository;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class JsonStorage implements ATMRepository, UserRepository {
    private Map<String, User> memoryUsers = new HashMap<>();
    private ATMState memoryATM;
    private final String FILE_PATH = "src/data/data.json";

    public JsonStorage() {
        // Hardcoded users so login ALWAYS works immediately
        User u1 = new User("1001", "1234", 270.0);
        User u2 = new User("1002", "5678", 1030.0);
        memoryUsers.put(u1.getAccount(), u1);
        memoryUsers.put(u2.getAccount(), u2);
        loadDataFromFile();
    }

    private void loadDataFromFile() {
        try {
            if (Files.exists(Paths.get(FILE_PATH))) {
                String content = Files.readString(Paths.get(FILE_PATH));
                int paper = parseJsonInt(content, "paper");
                int ink = parseJsonInt(content, "ink");
                String firmware = content.contains("firmware") ? findStringValue(content, "firmware") : "2.0.0";
                this.memoryATM = new ATMState(10, 10, 10, 10, 10, paper, ink, firmware);
            } else {
                this.memoryATM = new ATMState(10, 10, 10, 10, 10, 2, 2, "2.0.0");
            }
        } catch (Exception e) {
            this.memoryATM = new ATMState(10, 10, 10, 10, 10, 2, 2, "2.0.0");
        }
    }

    @Override
    public void transfer(User from, String to, double amt) {
        User target = memoryUsers.get(to);
        if (from != null && target != null) {
            // Use the specific methods already in your User.java
            from.transfer(amt, to);            // Adds "Transferred..." string
            target.receive(amt, from.getAccount()); // Adds "Received..." string
            saveAllData();
        }
    }

    @Override
    public void saveATM(ATMState state) {
        this.memoryATM = state;
        saveAllData();
    }

    private void saveAllData() {
        try {
            String json = "{\n" +
                    "  \"paper\": " + memoryATM.getPaperAmount() + ",\n" +
                    "  \"ink\": " + memoryATM.getInkAmount() + ",\n" +
                    "  \"firmware\": \"" + memoryATM.getFirmware() + "\"\n" +
                    "}";
            Files.writeString(Paths.get(FILE_PATH), json);
        } catch (Exception e) {}
    }

    private int parseJsonInt(String json, String key) {
        try {
            int start = json.indexOf("\"" + key + "\"");
            int colon = json.indexOf(":", start);
            int end = json.indexOf(",", colon);
            if (end == -1) end = json.indexOf("}", colon);
            return Integer.parseInt(json.substring(colon + 1, end).replaceAll("[^0-9]", "").trim());
        } catch (Exception e) { return 2; }
    }

    private String findStringValue(String json, String key) {
        try {
            int start = json.indexOf("\"" + key + "\"") + key.length() + 4;
            int end = json.indexOf("\"", start);
            return json.substring(start, end);
        } catch (Exception e) { return "2.0.0"; }
    }

    @Override public ATMState loadATM() { return memoryATM; }
    @Override public void updateUser(User u) { memoryUsers.put(u.getAccount(), u); }
    @Override public User authenticate(String a, String p) {
        User u = memoryUsers.get(a);
        return (u != null && u.getPin().equals(p)) ? u : null;
    }
}