package ra.edu.business.model;

public enum Role {
    ADMIN("ADMIN"),
    STUDENT("STUDENT");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}