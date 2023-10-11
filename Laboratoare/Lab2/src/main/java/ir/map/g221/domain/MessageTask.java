package ir.map.g221.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageTask extends Task {
    private Message mesaj;

    public MessageTask(String taskID, String descriere, Message mesaj) {
        super(taskID, descriere);
        this.mesaj = mesaj;
    }

    @Override
    public void execute() {
        System.out.println(mesaj.toString());
    }
}
