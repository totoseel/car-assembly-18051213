package org.example.domain;

public enum CarType {
    SEDAN("Sedan"),
    SUV("SUV"),
    TRUCK("Truck");

    public final String label;

    CarType(String label) {
        this.label = label;
    }
}
