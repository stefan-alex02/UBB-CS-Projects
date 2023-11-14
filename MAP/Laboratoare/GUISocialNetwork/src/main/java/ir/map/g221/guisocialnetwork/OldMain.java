package ir.map.g221.guisocialnetwork;

import ir.map.g221.guisocialnetwork.factory.Factory;

public class OldMain {
    public static void main(String[] args) {
        var container = Factory.getInstance().build();
        container.getUi().run();
    }
}