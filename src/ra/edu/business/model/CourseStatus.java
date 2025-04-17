package ra.edu.business.model;

public enum CourseStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    private final String value;

    CourseStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
