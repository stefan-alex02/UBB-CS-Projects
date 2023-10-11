package ir.map.g221.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Message(int id, String subject, String body, String from, String to, LocalDateTime date) {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[ yyyy-MM-dd  hh:mm ]");

    @Override
    public String toString() {
        return "[ id=" + id +
                " | description ='" + subject + '\'' +
                " | message ='" + body + '\'' +
                " | from='" + from + '\'' +
                " | to='" + to + '\'' +
                " | date=" + formatter.format(date) +
                ']';
    }
}
