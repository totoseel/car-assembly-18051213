package org.example.domain;

public enum Brake {
    MANDO("Mando"),
    CONTINENTAL("Continental"),
    BOSCH("Bosch");

    public final String label;

    Brake(String label) {
        this.label = label;
    }
}
