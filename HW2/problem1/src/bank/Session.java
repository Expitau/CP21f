package bank;

public class Session {

    private String sessionKey;
    private Bank bank;
    private boolean valid;
    private int transLimit = 3;
    private int transCnt;

    Session(String sessionKey,Bank bank){
        this.sessionKey = sessionKey;
        this.bank = bank;
        valid = true;
        transCnt = 0;
    }


    void expireSession(){
        valid = false;
    }

    private void insTransCnt(){
        transCnt += 1;
        if(transCnt == transLimit){
            expireSession();
        }
    }

    public boolean deposit(int amount) {
        //TODO: Problem 1.2
        if(!valid){
            return false;
        }
        boolean ret = bank.deposit(sessionKey, amount);
        insTransCnt();
        return ret;
    }

    public boolean withdraw(int amount) {
        //TODO: Problem 1.2
        if(!valid){
            return false;
        }
        boolean ret = bank.withdraw(sessionKey, amount);
        insTransCnt();
        return ret;
    }

    public boolean transfer(String targetId, int amount) {
        //TODO: Problem 1.2
        if(!valid){
            return false;
        }
        boolean ret = bank.transfer(sessionKey, targetId, amount);
        insTransCnt();
        return ret;
    }
}
