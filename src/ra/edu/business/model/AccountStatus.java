package ra.edu.business.model;

public enum AccountStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    BLOCKED("BLOCKED");

    private final String value;

    AccountStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
