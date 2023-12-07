package com.example.sem10_conc.bankaccount;

public class BankAccountSync implements BankAccount {
    private double balance;

    public BankAccountSync(double bal) {
        balance = bal;
    }

    public BankAccountSync() {
        this(0);
    }

    public double getBalance() {
        synchronized(this) {
                return balance;
        }
    }

//    public synchronized void deposit(double amt) {
//        double temp = balance + amt;
//        try {
//            Thread.sleep(100); // simulate processing time
//        } catch (InterruptedException ie) {
//            System.err.println(ie.getMessage());
//        }
//        balance = temp;
//        System.out.println("[" + Thread.currentThread().getName() +
//                "] : after deposit balance = $" + balance);
//    }

    public void deposit(double amt) {
        synchronized (this) {
            double temp = getBalance() + amt;
            try {
                Thread.sleep(100); // simulate processing time
            } catch (InterruptedException ie) {
                System.err.println(ie.getMessage());
            }
            balance = temp;
        }
        System.out.println("[" + Thread.currentThread().getName() +
                "] : after deposit balance = $" + balance);
    }

    public synchronized void withdraw(double amt) {
        if (balance < amt) {
            System.out.println("[" + Thread.currentThread().getName() +
            "] : Insufficient funds!");
            return;
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
