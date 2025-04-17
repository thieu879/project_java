package ra.edu.validate;

import ra.edu.business.model.Student;
import ra.edu.exception.InvalidInputException;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class StudentValidator {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PHONE_REGEX = "\\d{10,11}";

    public static void validate(Student student) throws InvalidInputException {
        if (student == null) {
            throw new InvalidInputException("Học viên không được null.");
        }
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new InvalidInputException("Tên học viên không được để trống.");
        }
        if (student.getEmail() == null || !Pattern.matches(EMAIL_REGEX, student.getEmail())) {
            throw new InvalidInputException("Email không hợp lệ.");
        }
        if (student.getPhone() == null || !Pattern.matches(PHONE_REGEX, student.getPhone())) {
            throw new InvalidInputException("Số điện thoại phải có 10-11 chữ số.");
        }
        if (student.getDob() == null || student.getDob().isAfter(LocalDate.now().minusYears(15))) {
            throw new InvalidInputException("Học viên phải từ 15 tuổi trở lên.");
        }
        if (student.getPassword() == null || student.getPassword().trim().isEmpty()) {
            throw new InvalidInputException("Mật khẩu không được để trống.");
        }
    }
}
