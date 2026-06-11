package org.example.validator;

public class ValidationResult {

    private final boolean passed;
    private final String reason;

    private ValidationResult(boolean passed, String reason) {
        this.passed = passed;
        this.reason = reason;
    }

    public static ValidationResult pass() {
        return new ValidationResult(true, null);
    }

    public static ValidationResult fail(String reason) {
        return new ValidationResult(false, reason);
    }

    public boolean isPassed() { return passed; }
    public String getReason() { return reason; }
}
