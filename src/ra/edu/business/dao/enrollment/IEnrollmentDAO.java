package ra.edu.business.dao.enrollment;

import ra.edu.business.model.Course;
import ra.edu.business.model.Student;
import ra.edu.exception.DatabaseException;

import java.util.List;

public interface IEnrollmentDAO {
    int addStudentToCourse(int studentId, int courseId) throws DatabaseException;
    int removeStudentFromCourse(int studentId, int courseId) throws DatabaseException;
    List<Student> displayStudentsByCourse(int courseId, int page, int pageSize, int[] totalPages) throws DatabaseException;
    int registerCourse(int studentId, int courseId) throws DatabaseException;
    List<Course> viewRegisteredCourses(int studentId, int page, int pageSize, int[] totalPages) throws DatabaseException;
    int cancelRegistration(int studentId, int courseId) throws DatabaseException;
    int[] countCoursesAndStudents() throws DatabaseException;
    int countStudentsByCourse(int courseId) throws DatabaseException;
    List<Object[]> top5CoursesByStudents() throws DatabaseException;
    List<Object[]> coursesWithMoreThan10Students() throws DatabaseException;
    List<Student> displayWaitingStudentsByCourse(int courseId, int page, int pageSize, int[] totalPages) throws DatabaseException;
    void approveStudentEnrollment(int courseId, int studentId, String status) throws DatabaseException;
}
