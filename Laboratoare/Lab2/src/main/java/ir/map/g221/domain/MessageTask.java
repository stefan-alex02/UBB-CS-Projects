package ir.map.g221.domain;

public class MessageTask extends Task {
    private final Message mesaj;

    public MessageTask(String taskID, String descriere, Message mesaj) {
        super(taskID, descriere);
        this.mesaj = mesaj;
    }

    @Override
    public void execute() {
        System.out.println(mesaj.toString());
    }
}
