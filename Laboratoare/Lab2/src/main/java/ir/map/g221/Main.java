package ir.map.g221;

import ir.map.g221.containers.Container;
import ir.map.g221.containers.factory.ContainerStrategy;
import ir.map.g221.containers.factory.TaskContainerFactory;
import ir.map.g221.domain.Message;
import ir.map.g221.domain.MessageTask;
import ir.map.g221.domain.SortingTask;
import ir.map.g221.domain.Task;
import ir.map.g221.domain.sorting.AbstractSorter;
import ir.map.g221.domain.sorting.SortingStrategies;
import ir.map.g221.runners.DelayTaskRunner;
import ir.map.g221.runners.PrinterTaskRunner;
import ir.map.g221.runners.StrategyTaskRunner;
import ir.map.g221.runners.TaskRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
//        sortingTaskTests();
//        printMessageTasks();
//        createContainersUsingFactory(ContainerStrategy.FIFO);
//        createContainersUsingFactory(ContainerStrategy.LIFO);

        executeTasksUsingRunners(args[0]);
    }

    private static void executeTasksUsingRunners(String arg) {
//        TaskRunner runner1 = new StrategyTaskRunner(ContainerStrategy.valueOf(arg));
//        runner1.addTask(new MessageTask("567", "desc1",
//                new Message(111, "subject1", "body1", "sender1", "receiver1",
//                        LocalDateTime.now())));
//        runner1.addTask(new MessageTask("567", "desc2",
//                new Message(222, "subject2", "body2", "sender2", "receiver2",
//                        LocalDateTime.now())));
//
//        System.out.println("\nStrategy Task Runner test method: ");
//        runner1.executeAll();

        TaskRunner runner2 = new DelayTaskRunner(new PrinterTaskRunner(new StrategyTaskRunner(ContainerStrategy.valueOf(arg))));
        runner2.addTask(new MessageTask("567", "desc1",
                new Message(111, "subject1", "body1", "sender1", "receiver1",
                        LocalDateTime.now())));
        runner2.addTask(new MessageTask("567", "desc2",
                new Message(222, "subject2", "body2", "sender2", "receiver2",
                        LocalDateTime.now())));
        runner2.addTask(new MessageTask("567", "desc3",
                new Message(333, "subject3", "body3", "sender3", "receiver3",
                        LocalDateTime.now())));
        runner2.addTask(new MessageTask("567", "desc4",
                new Message(444, "subject4", "body4", "sender4", "receiver4",
                        LocalDateTime.now())));

        System.out.println("\nPrinter Task Runner test method: ");
        runner2.executeAll();
//
//        TaskRunner runner3 = new DelayTaskRunner(new StrategyTaskRunner(ContainerStrategy.valueOf(arg)));
//        runner3.addTask(new MessageTask("567", "desc1",
//                new Message(111, "subject1", "body1", "sender1", "receiver1",
//                        LocalDateTime.now())));
//        runner3.addTask(new MessageTask("567", "desc2",
//                new Message(222, "subject2", "body2", "sender2", "receiver2",
//                        LocalDateTime.now())));
//        runner3.addTask(new MessageTask("567", "desc3",
//                new Message(333, "subject3", "body3", "sender3", "receiver3",
//                        LocalDateTime.now())));
//        runner3.addTask(new MessageTask("567", "desc4",
//                new Message(444, "subject4", "body4", "sender4", "receiver4",
//                        LocalDateTime.now())));
//
//        System.out.println("\nDelay Task Runner test method: ");
//        runner3.executeAll();
    }

    private static void createContainersUsingFactory(ContainerStrategy strategy) {
        Container container = TaskContainerFactory.getInstance().createContainer(strategy);
        container.add(new MessageTask("123", "desc1",
                new Message(111, "subject1", "body1", "sender1", "receiver1",
                        LocalDateTime.now())));
        container.add(new MessageTask("123", "desc2",
                new Message(222, "subject2", "body2", "sender2", "receiver2",
                        LocalDateTime.now())));
        container.add(new MessageTask("123", "desc3",
                new Message(333, "subject3", "body3", "sender3", "receiver3",
                        LocalDateTime.now())));

        while (!container.isEmpty()) {
            container.remove().execute();
        }
    }

    private static void sortingTaskTests() {
        AbstractSorter bubbleSorter = SortingStrategies.BUBBLE_SORT.getSorter();
        AbstractSorter quickSorter = SortingStrategies.QUICK_SORT.getSorter();

        sortUsingSorter(bubbleSorter);
        sortUsingSorter(quickSorter);
    }

    private static void sortUsingSorter(AbstractSorter sorter) {
        new SortingTask("123", "desc", Arrays.asList(3,1,4,5,2), sorter).execute();
        new SortingTask("123", "desc", Arrays.asList(9, 1, 2, 6, 7, 5, 3, 8, 4, 12, 10, 11),
                sorter).execute();
        new SortingTask("123", "desc", Arrays.asList(2, 3, 1), sorter).execute();
        new SortingTask("123", "desc", Arrays.asList(1, 2, 3, 4, 5), sorter).execute();
        new SortingTask("123", "desc", Arrays.asList(-3, 1, -4, 5, -2), sorter).execute();
        new SortingTask("123", "desc", Arrays.asList(54, 23, 67, 12, 89, 45, 78, 34, 56, 90, 23, 68,
                12, 78, 32, 98, 65, 43, 21, 76, 43, 89, 45, 67, 32, 54, 21, 76, 90, 65, 98, 56, 34, 23, 67, 12, 89,
                45, 78, 34, 56, 90, 23, 68, 12, 78, 32, 98, 65, 43, 21, 76, 43, 89, 45, 67, 32, 54, 21, 76, 90, 65,
                98, 56, 34, 23, 67, 12, 89, 45, 78, 34, 56, 90, 23, 68, 12, 78, 32, 98, 65, 43, 21, 76, 43, 89, 45,
                67, 32, 54, 21, 76, 90, 65, 98, 56, 34), sorter).execute();
    }

    private static void printMessageTasks() {
        List<Task> list = Arrays.asList(
                new MessageTask("id1", "desc1", new Message(1, "Feedback lab",
                "Ai obtinut 9.60", "Gigi", "Ana", LocalDateTime.now())),
                new MessageTask("id2", "desc2", new Message(2, "subject2",
                        "body2", "sender2", "receiver2", LocalDateTime.now())),
                new MessageTask("id3", "desc3", new Message(3, "subject3",
                        "body3", "sender3", "receiver3", LocalDateTime.now())),
                new MessageTask("id4", "desc4", new Message(4, "subject4",
                        "body4", "sender4", "receiver4", LocalDateTime.now())),
                new MessageTask("id5", "desc5", new Message(5, "subject5",
                        "body5", "sender5", "receiver5", LocalDateTime.now()))
        );

        list.forEach(Task::execute);
    }
}