package com.example.sem10_conc.bankaccount;

public class BankAccountSyncNotify implements BankAccount {
    private double balance;

    public BankAccountSyncNotify(double bal) {
        balance = bal;
    }

    public BankAccountSyncNotify() {
        this(0);
    }

    public double getBalance() {
        synchronized(this) {
                return balance;
        }
    }

    public synchronized void deposit(double amt) {
        double temp = getBalance() + amt;
        try {
            Thread.sleep(100); // simulate processing time
        } catch (InterruptedException ie) {
            System.err.println(ie.getMessage());
        }
        balance = temp;
        System.out.println("[" + Thread.currentThread().getName() +
                "] : after deposit balance = $" + balance);

        notifyAll();
    }

    public synchronized void withdraw(double amt) {
        while (balance < amt) {
            System.out.println("[" + Thread.currentThread().getName() +
            "] : Insufficient funds! Waiting...");

            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getStackTrace());
//                throw new RuntimeException(e);
            }
        }
        double temp = balance - amt;
        try {
            Thread.sleep(100); // simulate processing time
        } catch (InterruptedException ie) {
            System.err.println(ie.getMessage());
        }
        balance = temp;
        System.out.println("[" + Thread.currentThread().getName() +
                "] : after withdrawal balance = $" + balance);

    }
}
