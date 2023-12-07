package com.example.sem10_conc.bankaccount;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void runWithThreads(BankAccount account) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            account.deposit(100);
        });
        Thread t2 = new Thread(() -> {
            account.deposit(100);
//            System.out.println(Thread.currentThread().getName());
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("final balance = $" + account.getBalance());
    }

    public static void runWithThreads2(BankAccount account) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            account.deposit(100);
        });
        Thread t2 = new Thread(() -> {
            account.deposit(100);
        });
        Thread t3 = new Thread(() -> {
            account.withdraw(200);
        });

        t3.start();
        Thread.sleep(1000);
        t1.start();
        t2.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("final balance = $" + account.getBalance());
    }

    public static void runReaders(BankAccount account) throws InterruptedException {
        Thread t1 = new Thread(account::getBalance);
        Thread t2 = new Thread(account::getBalance);

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    private static void runWithThreadPool(BankAccount account) throws InterruptedException {
        int numThreads = 3;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        for (int i = 1; i <= 11; i++) {
            if (i == 11) {
                executor.submit(() -> {
                    System.out.println(account.getBalance());
                });
            }
            else {
                executor.submit(() -> account.deposit(100));
                if (i % 3 == 1) {
                    executor.submit(() -> account.withdraw(150));
                }
            }
        }
        executor.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        BankAccount account;
        // wrong
//         account = new BankAccountWrong(0);

        // correct
//        account = new BankAccountSync(0);

//        account = new BankAccountSyncNotify(0);

//        runWithThreads(account);
//        runWithThreads2(account);
//        //runWithThreadPool(account);

        account = new BankAccountReentrantNotify(0);
//        runWithThreadPool(account);

        runWithThreadPool(account);
    }
}
