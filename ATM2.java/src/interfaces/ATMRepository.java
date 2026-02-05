package interfaces;

import model.ATMState;

public interface ATMRepository {
    ATMState loadATM();
    void saveATM(ATMState state);
}