package ra.edu.validate;

import ra.edu.business.model.Course;
import ra.edu.exception.InvalidInputException;

public class CourseValidator {
    public static void validate(Course course) throws InvalidInputException {
        if (course == null) {
            throw new InvalidInputException("Khóa học không được null.");
        }
        if (course.getName() == null || course.getName().trim().isEmpty()) {
            throw new InvalidInputException("Tên khóa học không được để trống.");
        }
        if (course.getDuration() <= 0) {
            throw new InvalidInputException("Thời lượng khóa học phải lớn hơn 0.");
        }
        if (course.getInstructor() == null || course.getInstructor().trim().isEmpty()) {
            throw new InvalidInputException("Tên giảng viên không được để trống.");
        }
    }
}
