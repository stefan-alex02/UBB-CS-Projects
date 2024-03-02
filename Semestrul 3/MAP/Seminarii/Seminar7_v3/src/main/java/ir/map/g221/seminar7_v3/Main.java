package ir.map.g221.seminar7_v3;

import ir.map.g221.seminar7_v3.factory.Factory;

public class Main {
    public static void main(String[] args) {
        var container = Factory.getInstance().build();
        container.getUi().run();
    }
}