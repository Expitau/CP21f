package bank;

import security.key.BankPublicKey;
import security.key.BankSymmetricKey;
import security.*;

public class MobileApp {

    private String randomUniqueStringGen(){
        return Encryptor.randomUniqueStringGen();
    }
    private final String AppId = randomUniqueStringGen();
    public String getAppId() {
        return AppId;
    }
    private BankSymmetricKey bankSymKey;

    String id, password;
    public MobileApp(String id, String password){
        this.id = id;
        this.password = password;
    }

    public Encrypted<BankSymmetricKey> sendSymKey(BankPublicKey publickey){
        //TODO: Problem 1.3
        bankSymKey = new BankSymmetricKey(randomUniqueStringGen());
        Encrypted<BankSymmetricKey> ret = new Encrypted<BankSymmetricKey>(bankSymKey, publickey);
        return ret;
    }

    public Encrypted<Message> deposit(int amount){
        //TODO: Problem 1.3
        Message msg = new Message("deposit", id, password, amount);
        Encrypted<Message> ret = new Encrypted<Message>(msg, bankSymKey);
        return ret;
    }

    public Encrypted<Message> withdraw(int amount){
        //TODO: Problem 1.3
        Message msg = new Message("withdraw", id, password, amount);
        Encrypted<Message> ret = new Encrypted<Message>(msg, bankSymKey);
        return ret;
    }

    public boolean processResponse(Encrypted<Boolean> obj) {
        //TODO: Problem 1.3
        Boolean res = obj.decrypt(bankSymKey);
        if(res == null){
            return false;
        }
        return res;
    }
}

