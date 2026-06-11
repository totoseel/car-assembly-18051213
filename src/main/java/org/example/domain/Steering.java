package org.example.domain;

public enum Steering {
    BOSCH("BOSCH"),
    MOBIS("MOBIS");

    public final String label;

    Steering(String label) {
        this.label = label;
    }
}
