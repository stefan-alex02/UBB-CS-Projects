package org.example;

import org.example.tasks.DelayedPrintTask;
import org.example.tasks.PrintTask;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            // Test 1
            System.out.println("---- Test 1 ----");
            PrintTask printTask1 = new PrintTask("Message 1.");
            DelayedPrintTask delayedPrintTask1 =
                    new DelayedPrintTask("Delayed message 1", 1000);
            DelayedPrintTask delayedPrintTask2 =
                    new DelayedPrintTask("Delayed message 2", 3000);

            ConcurrentTaskRunner taskRunner = new ConcurrentTaskRunner(3);

            taskRunner.enqueueItem(delayedPrintTask2);
            taskRunner.enqueueItem(delayedPrintTask1);
            taskRunner.enqueueItem(printTask1);

            Thread.sleep(5000);

            // Test 2
            System.out.println("---- Test 2 ----");
            DelayedPrintTask delayedPrintTask3 =
                    new DelayedPrintTask("Delayed message 3", 3000);
            PrintTask printTask2 = new PrintTask("Message 2.");

            taskRunner.enqueueItem(printTask2);
            Thread.sleep(1000);
            taskRunner.enqueueItem(delayedPrintTask3);

            Thread.sleep(5000);

            // Test 3
            System.out.println("---- Test 3 ----");
            taskRunner.enqueueItem(delayedPrintTask3);
            taskRunner.enqueueItem(delayedPrintTask2);
            taskRunner.enqueueItem(delayedPrintTask3);
            taskRunner.enqueueItem(delayedPrintTask1);
            taskRunner.enqueueItem(printTask1);
            taskRunner.enqueueItem(printTask2);
            taskRunner.enqueueItem(delayedPrintTask3);

            Thread.sleep(10000);

            System.out.println("---- Tests finished ----");
            taskRunner.shutdown(true);

            System.out.println("Press any key to continue...");
            System.in.read();
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}