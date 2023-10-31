package ir.map.g221;

import ir.map.g221.factory.Factory;

public class Main {
    public static void main(String[] args) {
        var container = Factory.getInstance().build();
        container.getUi().run();
    }
}