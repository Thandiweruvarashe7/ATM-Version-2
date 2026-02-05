package model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class ATMState {

    private int c100, c50, c20, c10, c5; // Added 5 Euro notes
    private int paper;
    private int ink;
    private String firmware;

    // Standard constructor for Version 2
    public ATMState(int c100, int c50, int c20, int c10, int c5, int paper, int ink, String firmware) {
        this.c100 = c100;
        this.c50 = c50;
        this.c20 = c20;
        this.c10 = c10;
        this.c5 = c5;
        this.paper = paper;
        this.ink = ink;
        this.firmware = firmware;
    }

    // Backwards-compatible constructor
    public ATMState(double cash, int paper, String firmware) {
        int remaining = (int) cash;
        this.c100 = remaining / 100; remaining %= 100;
        this.c50 = remaining / 50; remaining %= 50;
        this.c20 = remaining / 20; remaining %= 20;
        this.c10 = remaining / 10; remaining %= 10;
        this.c5 = remaining / 5;   remaining %= 5;
        this.paper = paper;
        this.ink = 100;
        this.firmware = firmware;
    }

    public double getCashAmount() {
        return (c100 * 100) + (c50 * 50) + (c20 * 20) + (c10 * 10) + (c5 * 5);
    }

    public int getCountFor(int denom) {
        return switch (denom) {
            case 100 -> c100;
            case 50 -> c50;
            case 20 -> c20;
            case 10 -> c10;
            case 5 -> c5;
            default -> 0;
        };
    }

    public void addNotes(int denom, int count) {
        switch (denom) {
            case 100 -> c100 += count;
            case 50 -> c50 += count;
            case 20 -> c20 += count;
            case 10 -> c10 += count;
            case 5 -> c5 += count;
        }
    }

    public boolean removeRequestedNotes(Map<Integer, Integer> requested) {
        for (Map.Entry<Integer, Integer> e : requested.entrySet()) {
            if (getCountFor(e.getKey()) < e.getValue()) return false;
        }
        for (Map.Entry<Integer, Integer> e : requested.entrySet()) {
            addNotes(e.getKey(), -e.getValue());
        }
        return true;
    }

    // This is the specific method your Unit Test is looking for!
    public Map<Integer, Integer> simulateAllocationForAmount(int amount) {
        int remaining = amount;
        Map<Integer, Integer> result = new LinkedHashMap<>();
        int[] denoms = {100, 50, 20, 10, 5};

        for (int d : denoms) {
            int count = Math.min(remaining / d, getCountFor(d));
            result.put(d, count);
            remaining -= count * d;
        }

        if (remaining != 0) return null;
        return result;
    }

    public List<Map<Integer, Integer>> findAllocationsForAmount(int amount, int limit) {
        List<Map<Integer, Integer>> results = new ArrayList<>();
        int[] denoms = new int[]{100, 50, 20, 10, 5};
        findAllocationsRecursive(amount, 0, denoms, new LinkedHashMap<>(), results, limit);
        return results;
    }

    private void findAllocationsRecursive(int remaining, int idx, int[] denoms, Map<Integer, Integer> current, List<Map<Integer, Integer>> results, int limit) {
        if (results.size() >= limit) return;
        if (remaining == 0) {
            results.add(new LinkedHashMap<>(current));
            return;
        }
        if (idx >= denoms.length) return;

        int denom = denoms[idx];
        int maxAvailable = Math.min(remaining / denom, getCountFor(denom));
        for (int cnt = maxAvailable; cnt >= 0; cnt--) {
            current.put(denom, cnt);
            findAllocationsRecursive(remaining - (cnt * denom), idx + 1, denoms, current, results, limit);
            if (results.size() >= limit) return;
        }
    }

    public Map<Integer, Integer> allocateNotesForAmount(int amount) {
        Map<Integer, Integer> allocation = simulateAllocationForAmount(amount);
        if (allocation != null) removeRequestedNotes(allocation);
        return allocation;
    }

    public int getPaperAmount() { return paper; }
    public void setPaperAmount(int paper) { this.paper = paper; }
    public int getInkAmount() { return ink; }
    public void setInkAmount(int ink) { this.ink = ink; }
    public void useInk(int amount) { this.ink = Math.max(0, this.ink - amount); }
    public String getFirmware() { return firmware; }
}

