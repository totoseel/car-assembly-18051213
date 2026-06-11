package org.example.domain;

public enum Brake {
    MANDO("MANDO"),
    CONTINENTAL("CONTINENTAL"),
    BOSCH("BOSCH");

    public final String label;

    Brake(String label) {
        this.label = label;
    }
}
