package ir.map.g221;

import ir.map.g221.domain.Message;
import ir.map.g221.domain.MessageTask;
import ir.map.g221.domain.SortingTask;
import ir.map.g221.domain.Task;
import ir.map.g221.domain.sorting.SortingStrategies;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Task task1 = new SortingTask("123", "desc", Arrays.asList(3,1,4,5,2),
                SortingStrategies.BUBBLE_SORT.getSorter());
        task1.execute();

        printMessageTasks();
    }

    public static void printMessageTasks() {
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

        list.forEach(task -> task.execute());
    }
}