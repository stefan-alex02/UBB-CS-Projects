package com.example.sem10_conc.bankaccount;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccountReentrantNotify implements BankAccount {
    private double balance;

    private final Lock lock = new ReentrantLock();
    private final Condition insuficient = lock.newCondition();

    public BankAccountReentrantNotify(double bal) {
        balance = bal;
    }

    public BankAccountReentrantNotify() {
        this(0);
    }


    public double getBalance() {
        try {
            System.out.println(Thread.currentThread().getName() + " waiting for lock");
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " acquired lock");
            try {
                Thread.sleep(1000); // simulate processing time
            } catch (InterruptedException ie) {
                System.err.println(ie.getMessage());
            }
            return balance;
        } finally {
            lock.unlock();
            System.out.println(Thread.currentThread().getName() + " released lock");
        }
    }

    public void deposit(double amt) {
        try {
            lock.lock();
            double temp = balance + amt;
            try {
                Thread.sleep(100); // simulate processing time
            } catch (InterruptedException ie) {
                System.err.println(ie.getMessage());
            }
            balance = temp;
            System.out.println("[" + Thread.currentThread().getName() +
                    "] : After deposit balance = $" + balance);

            insuficient.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(double amt) {
        try {
            lock.lock();
            while (balance < amt) {
                System.out.println("[" + Thread.currentThread().getName() +
                        "] : Insufficient funds! Waiting...");
                insuficient.await();
            }
            double temp = balance - amt;
            try {
                Thread.sleep(100); // simulate processing time
            } catch (InterruptedException ie) {
                System.err.println(ie.getMessage());
            }
            balance = temp;
            System.out.println("[" + Thread.currentThread().getName() +
                    "] : After withdrawal balance = $" + balance);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
