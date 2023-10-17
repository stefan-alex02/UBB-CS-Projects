package ir.map.g221.runners;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PrinterTaskRunner extends AbstractTaskRunner{
    public PrinterTaskRunner(TaskRunner runner) {
        super(runner);
    }

    @Override
    public void executeOneTask() {
        super.executeOneTask();

        System.out.println("Task executed successfully at " + DateTimeFormatter.ofPattern("hh:mm:ss").format(LocalDateTime.now()));
    }
}
