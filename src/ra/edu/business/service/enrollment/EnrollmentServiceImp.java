package ra.edu.business.service.enrollment;

import ra.edu.business.dao.enrollment.EnrollmentDAOImp;
import ra.edu.business.dao.enrollment.IEnrollmentDAO;
import ra.edu.business.model.Course;
import ra.edu.business.model.Student;
import ra.edu.exception.DatabaseException;
import ra.edu.exception.ValidationException;
import ra.edu.validate.EnrollmentValidator;

import java.util.List;

public class EnrollmentServiceImp implements IEnrollmentService {
    private IEnrollmentDAO enrollmentDAO = new EnrollmentDAOImp();

    @Override
    public void addStudentToCourse(int studentId, int courseId) throws ValidationException, DatabaseException {
        EnrollmentValidator.validate(studentId, courseId);
        int result = enrollmentDAO.addStudentToCourse(studentId, courseId);
        if (result == 0) {
            throw new DatabaseException("Học viên đã tồn tại trong khóa học.");
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) throws ValidationException, DatabaseException {
        EnrollmentValidator.validate(studentId, courseId);
        int result = enrollmentDAO.removeStudentFromCourse(studentId, courseId);
        if (result == 0) {
            throw new DatabaseException("Học viên không tồn tại trong khóa học.");
        }
    }

    @Override
    public List<Student> displayStudentsByCourse(int courseId, int page, int pageSize, int[] totalPages) throws ValidationException, DatabaseException {
        if (courseId <= 0) {
            throw new ValidationException("Mã khóa học không hợp lệ.");
        }
        return enrollmentDAO.displayStudentsByCourse(courseId, page, pageSize, totalPages);
    }

    @Override
    public void registerCourse(int studentId, int courseId) throws ValidationException, DatabaseException {
        EnrollmentValidator.validate(studentId, courseId);
        int result = enrollmentDAO.registerCourse(studentId, courseId);
        if (result == 0) {
            throw new DatabaseException("Đăng ký khóa học thất bại hoặc học viên đã đăng ký.");
        }
    }

    @Override
    public List<Course> viewRegisteredCourses(int studentId, int page, int pageSize, int[] totalPages) throws ValidationException, DatabaseException {
        if (studentId <= 0) {
            throw new ValidationException("Mã học viên không hợp lệ.");
        }
        return enrollmentDAO.viewRegisteredCourses(studentId, page, pageSize, totalPages);
    }

    @Override
    public void cancelRegistration(int studentId, int courseId) throws ValidationException, DatabaseException {
        EnrollmentValidator.validate(studentId, courseId);
        int result = enrollmentDAO.cancelRegistration(studentId, courseId);
        if (result == 0) {
            throw new DatabaseException("Hủy đăng ký thất bại hoặc học viên chưa đăng ký khóa học này.");
        }
    }

    @Override
    public int[] countCoursesAndStudents() throws DatabaseException {
        return enrollmentDAO.countCoursesAndStudents();
    }

    @Override
    public int countStudentsByCourse(int courseId) throws ValidationException, DatabaseException {
        if (courseId <= 0) {
            throw new ValidationException("Mã khóa học không hợp lệ.");
        }
        return enrollmentDAO.countStudentsByCourse(courseId);
    }

    @Override
    public List<Object[]> top5CoursesByStudents() throws DatabaseException {
        return enrollmentDAO.top5CoursesByStudents();
    }

    @Override
    public List<Object[]> coursesWithMoreThan10Students() throws DatabaseException {
        return enrollmentDAO.coursesWithMoreThan10Students();
    }

    @Override
    public List<Student> displayWaitingStudentsByCourse(int courseId, int page, int pageSize, int[] totalPages) throws DatabaseException {
        return enrollmentDAO.displayWaitingStudentsByCourse(courseId, page, pageSize, totalPages);
    }

    @Override
    public void approveStudentEnrollment(int courseId, int studentId, String status) throws DatabaseException {
        enrollmentDAO.approveStudentEnrollment(courseId, studentId, status);
    }
}
