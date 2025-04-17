package ra.edu.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataFormatter {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : "";
    }

    public static String formatSex(boolean sex) {
        return sex ? "Nam" : "Nữ";
    }
}
