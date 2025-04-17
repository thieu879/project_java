package ra.edu.business.model;

public enum EnrollmentStatus {
    WAITING("WAITING"),
    DENIED("DENIED"),
    CANCER("CANCER"),
    CONFIRM("CONFIRM");

    private final String value;

    EnrollmentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
