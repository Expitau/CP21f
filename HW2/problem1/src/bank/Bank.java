package bank;

import bank.event.*;
import security.*;
import security.key.*;

public class Bank {
    private int numAccounts = 0;
    final static int maxAccounts = 100;
    private BankAccount[] accounts = new BankAccount[maxAccounts];
    private String[] ids = new String[maxAccounts];

    public void createAccount(String id, String password) {
        createAccount(id, password, 0);
    }

    public void createAccount(String id, String password, int initBalance) {
        //TODO: Problem 1.1
        BankAccount account = find(id);
        if(account != null) return;
        accounts[numAccounts] = new BankAccount(id, password, initBalance);
        ids[numAccounts] = id;
        numAccounts+=1;
    }

    public boolean deposit(String id, String password, int amount) {
        //TODO: Problem 1.1
        BankAccount account = find(id);
        if (account == null || !account.authenticate(password)) {
            return false;
        }
        account.deposit(amount);
        return true;
    }

    public boolean withdraw(String id, String password, int amount) {
        //TODO: Problem 1.1
        BankAccount account = find(id);
        if (account == null || !account.authenticate(password)) {
            return false;
        }
        return account.withdraw(amount);
    }

    public boolean transfer(String sourceId, String password, String targetId, int amount) {
        //TODO: Problem 1.1
        BankAccount sourceAccount = find(sourceId);
        BankAccount targetAccount = find(targetId);
        if (sourceAccount == null || targetAccount == null || !sourceAccount.authenticate(password)) {
            return false;
        }
        if (sourceAccount.send(amount)) {
            targetAccount.receive(amount);
            return true;
        }
        return false;
    }

    public Event[] getEvents(String id, String password) {
        //TODO: Problem 1.1
        BankAccount account = find(id);
        if (account == null || !account.authenticate(password)) {
            return null;
        }
        return account.getEvents();
    }

    public int getBalance(String id, String password) {
        //TODO: Problem 1.1
        BankAccount account = find(id);
        if (account == null || !account.authenticate(password)) {
            return -1;
        }
        return account.getBalance();
    }

    private static String randomUniqueStringGen() {
        return Encryptor.randomUniqueStringGen();
    }

    private BankAccount find(String id) {
        for (int i = 0; i < numAccounts; i++) {
            if (ids[i].equals(id)) {
                return accounts[i];
            }
        }
        return null;
    }

    final static int maxSessionKey = 100;
    int numSessionKey = 0;
    String[] sessionKeyArr = new String[maxSessionKey];
    BankAccount[] bankAccountmap = new BankAccount[maxSessionKey];

    String generateSessionKey(String id, String password) {
        BankAccount account = find(id);
        if (account == null || !account.authenticate(password)) {
            return null;
        }
        String sessionkey = randomUniqueStringGen();
        sessionKeyArr[numSessionKey] = sessionkey;
        bankAccountmap[numSessionKey] = account;
        numSessionKey += 1;
        return sessionkey;
    }

    BankAccount getAccount(String sessionkey) {
        for (int i = 0; i < numSessionKey; i++) {
            if (sessionKeyArr[i] != null && sessionKeyArr[i].equals(sessionkey)) {
                return bankAccountmap[i];
            }
        }
        return null;
    }

    boolean deposit(String sessionkey, int amount) {
        //TODO: Problem 1.2
        BankAccount account = getAccount(sessionkey);
        if(account == null){
            return false;
        }
        account.deposit(amount);
        return true;
    }

    boolean withdraw(String sessionkey, int amount) {
        //TODO: Problem 1.2
        BankAccount account = getAccount(sessionkey);
        if(account == null) {
            return false;
        }
        return account.withdraw(amount);
    }

    boolean transfer(String sessionkey, String targetId, int amount) {
        //TODO: Problem 1.2
        BankAccount sourceAccount = getAccount(sessionkey);
        BankAccount targetAccount = find(targetId);
        if(sourceAccount == null || targetAccount == null){
            return false;
        }
        if(sourceAccount.send(amount)){
            targetAccount.receive(amount);
            return true;
        }
        return false;
    }

    private BankSecretKey secretKey;

    public BankPublicKey getPublicKey() {
        BankKeyPair keypair = Encryptor.publicKeyGen(); // generates two keys : BankPublicKey, BankSecretKey
        secretKey = keypair.deckey; // stores BankSecretKey internally
        return keypair.enckey;
    }

    int maxHandshakes = 10000;
    int numSymmetrickeys = 0;
    BankSymmetricKey[] bankSymmetricKeys = new BankSymmetricKey[maxHandshakes];
    String[] AppIds = new String[maxHandshakes];

    public int getAppIdIndex(String AppId) {
        for (int i = 0; i < numSymmetrickeys; i++) {
            if (AppIds[i].equals(AppId)) {
                return i;
            }
        }
        return -1;
    }

    public void fetchSymKey(Encrypted<BankSymmetricKey> encryptedKey, String AppId) {
        //TODO: Problem 1.3
        if(encryptedKey == null) return;
        BankSymmetricKey bnkSymKey = encryptedKey.decrypt(secretKey);
        if(bnkSymKey == null) return;

        int appIndex = getAppIdIndex(AppId);
        if(appIndex == -1) {
            bankSymmetricKeys[numSymmetrickeys] = bnkSymKey;
            AppIds[numSymmetrickeys] = AppId;
            numSymmetrickeys += 1;
        }else{
            bankSymmetricKeys[appIndex] = bnkSymKey;
        }
    }

    public Encrypted<Boolean> processRequest(Encrypted<Message> messageEnc, String AppId) {
        //TODO: Problem 1.3
        if(messageEnc == null) return null;

        int appIndex = getAppIdIndex(AppId);
        if(appIndex == -1) return null;

        BankSymmetricKey bnkSymKey = bankSymmetricKeys[appIndex];
        Message msg = messageEnc.decrypt(bnkSymKey);
        if(msg == null) return null;

        Encrypted<Boolean> ret = null;
        if(msg.getRequestType().equals("deposit")){
            boolean res = deposit(msg.getId(), msg.getPassword(), msg.getAmount());
            ret = new Encrypted<Boolean>(res, bnkSymKey);
        }else if(msg.getRequestType().equals("withdraw")){
            boolean res = withdraw(msg.getId(), msg.getPassword(), msg.getAmount());
            ret = new Encrypted<Boolean>(res, bnkSymKey);
        }
        return ret;
    }
}