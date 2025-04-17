package ra.edu.validate;

import ra.edu.exception.ValidationException;

public class EnrollmentValidator {
    public static void validate(int studentId, int courseId) throws ValidationException {
        if (studentId <= 0) {
            throw new ValidationException("Mã học viên không hợp lệ.");
        }
        if (courseId <= 0) {
            throw new ValidationException("Mã khóa học không hợp lệ.");
        }
    }
}
