package interfaces;

import model.*;
import java.util.Map;

public interface ATMService {

    boolean withdraw(User user, int amount);
    boolean withdraw(User user, ATMState atm, int amount);
    boolean withdraw(User user, ATMState atm, int amount, Map<Integer,Integer> requestedNotes);

    boolean printReceipt();
    ATMState getATMState();
} 
