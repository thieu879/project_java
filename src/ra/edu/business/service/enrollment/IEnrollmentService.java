package ra.edu.business.service.enrollment;

import ra.edu.business.model.Course;
import ra.edu.business.model.Student;
import ra.edu.exception.DatabaseException;
import ra.edu.exception.ValidationException;

import java.util.List;

public interface IEnrollmentService {
    void addStudentToCourse(int studentId, int courseId) throws ValidationException, DatabaseException;
    void removeStudentFromCourse(int studentId, int courseId) throws ValidationException, DatabaseException;
    List<Student> displayStudentsByCourse(int courseId, int page, int pageSize, int[] totalPages) throws ValidationException, DatabaseException;
    void registerCourse(int studentId, int courseId) throws ValidationException, DatabaseException;
    List<Course> viewRegisteredCourses(int studentId, int page, int pageSize, int[] totalPages) throws ValidationException, DatabaseException;
    void cancelRegistration(int studentId, int courseId) throws ValidationException, DatabaseException;
    int[] countCoursesAndStudents() throws DatabaseException;
    int countStudentsByCourse(int courseId) throws ValidationException, DatabaseException;
    List<Object[]> top5CoursesByStudents() throws DatabaseException;
    List<Object[]> coursesWithMoreThan10Students() throws DatabaseException;
}