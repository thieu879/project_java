package ra.edu.business.service.course;

import ra.edu.business.model.Course;
import ra.edu.exception.DatabaseException;
import ra.edu.exception.ValidationException;

import java.util.List;

public interface ICourseService {
    void addCourse(Course course) throws ValidationException, DatabaseException;
    void updateCourse(Course course) throws ValidationException, DatabaseException;
    void deleteCourse(int id) throws DatabaseException;
    List<Course> searchCourse(String name, String instructor, int page, int pageSize, int[] totalPages) throws DatabaseException;
    List<Course> sortCourse(int sortBy, boolean isAsc, int page, int pageSize, int[] totalPages) throws DatabaseException;
    List<Course> displayCourses(int page, int pageSize, int[] totalPages) throws DatabaseException;
}
