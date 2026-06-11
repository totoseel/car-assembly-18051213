package org.example.domain;

public enum Engine {
    GM("GM"),
    TOYOTA("TOYOTA"),
    WIA("WIA");

    public final String label;

    Engine(String label) {
        this.label = label;
    }
}
