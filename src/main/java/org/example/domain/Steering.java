package org.example.domain;

public enum Steering {
    BOSCH("Bosch"),
    MOBIS("Mobis");

    public final String label;

    Steering(String label) {
        this.label = label;
    }
}
