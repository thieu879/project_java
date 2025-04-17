package ra.edu.business.model;

public enum StudentStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    private final String value;

    StudentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
