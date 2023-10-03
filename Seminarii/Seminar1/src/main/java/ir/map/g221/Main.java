package ir.map.g221;

import ir.map.g221.domain.MessageTask;
import ir.map.g221.domain.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        MessageTask t = new MessageTask("2020", "Ana", "Mesaj", "Ioana", "Andrei", LocalDateTime.now());
        System.out.println(t);
    }
}