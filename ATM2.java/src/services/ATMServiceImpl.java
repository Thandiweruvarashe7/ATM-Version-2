package services;

import model.*;
import interfaces.ATMService;
import interfaces.ATMRepository;
import java.util.Map;

public class ATMServiceImpl implements ATMService {
    private ATMRepository atmRepo;

    public ATMServiceImpl(ATMRepository atmRepo) {
        this.atmRepo = atmRepo;
    }

    @Override
    public boolean withdraw(User user, int amount) {
        return withdraw(user, atmRepo.loadATM(), amount);
    }

    @Override
    public boolean withdraw(User user, ATMState atmState, int amount) {
        Map<Integer, Integer> allocation = atmState.allocateNotesForAmount(amount);
        if (allocation == null) return false;
        user.withdraw(amount);
        atmRepo.saveATM(atmState);
        return true;
    }

    @Override
    public boolean withdraw(User user, ATMState atmState, int amount, Map<Integer, Integer> requestedNotes) {
        int sum = requestedNotes.entrySet().stream().mapToInt(e -> e.getKey() * e.getValue()).sum();
        if (sum != amount || !atmState.removeRequestedNotes(requestedNotes)) return false;
        user.withdraw(amount);
        atmRepo.saveATM(atmState);
        return true;
    }

    @Override
    public boolean printReceipt() {
        ATMState state = atmRepo.loadATM();
        if (state.getPaperAmount() > 0 && state.getInkAmount() > 0) {
            state.setPaperAmount(state.getPaperAmount() - 1);
            state.useInk(5);
            atmRepo.saveATM(state);
            return true;
        }
        return false;
    }

    @Override
    public ATMState getATMState() {
        return atmRepo.loadATM();
    }
}
