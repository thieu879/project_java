package ra.edu.validate;

import ra.edu.business.model.Student;
import ra.edu.exception.ValidationException;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class StudentValidator {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PHONE_REGEX = "\\d{10,11}";

    public static void validate(Student student) throws ValidationException {
        if (student == null) {
            throw new ValidationException("Học viên không được null.");
        }
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new ValidationException("Tên học viên không được để trống.");
        }
        if (student.getDob() == null || student.getDob().isAfter(LocalDate.now().minusYears(16))) {
            throw new ValidationException("Học viên phải từ 16 tuổi trở lên.");
        }
        if (student.getEmail() == null || !Pattern.matches(EMAIL_REGEX, student.getEmail())) {
            throw new ValidationException("Email không hợp lệ.");
        }
        if (student.getPhone() != null && !student.getPhone().isEmpty() && !Pattern.matches(PHONE_REGEX, student.getPhone())) {
            throw new ValidationException("Số điện thoại phải có 10-11 chữ số.");
        }
    }
}
