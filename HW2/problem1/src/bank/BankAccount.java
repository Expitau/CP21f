package bank;

import bank.event.*;

class BankAccount {
    private Event[] events = new Event[maxEvents];
    final static int maxEvents = 100;
    private String id, password;
    private int balance, eventCnt;


    BankAccount(String id, String password, int balance) {
        //TODO: Problem 1.1
        this.id = id;
        this.password = password;
        this.balance = balance;
        this.eventCnt = 0;
    }

    void pushEvent(Event evt){
        events[eventCnt++] = evt;
    }


    Event[] getEvents(){
        Event[] res = new Event[eventCnt];
        for(int i=0; i<eventCnt; i++) res[i]=events[i];
        return res;
    }

    int getBalance(){
        return this.balance;
    }

    boolean authenticate(String password) {
        //TODO: Problem 1.1
        return this.password.equals(password);
    }

    void deposit(int amount) {
        //TODO: Problem 1.1
        this.balance += amount;
        pushEvent(new DepositEvent());
    }

    boolean withdraw(int amount) {
        //TODO: Problem 1.1
        if(this.balance < amount) return false;
        this.balance -= amount;
        pushEvent(new WithdrawEvent());
        return true;
    }

    void receive(int amount) {
        //TODO: Problem 1.1
        this.balance += amount;
        pushEvent(new ReceiveEvent());
    }

    boolean send(int amount) {
        //TODO: Problem 1.1
        if(this.balance < amount) return false;
        this.balance -= amount;
        pushEvent(new SendEvent());
        return true;
    }

}
