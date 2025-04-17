package ra.edu.business.dao.course;

import ra.edu.business.model.Course;
import ra.edu.exception.DatabaseException;

import java.util.List;

public interface ICourseDAO {
    int addCourse(Course course) throws DatabaseException;
    int updateCourse(Course course) throws DatabaseException;
    int deleteCourse(int id) throws DatabaseException;
    List<Course> searchCourse(String name, int page, int pageSize, int[] totalPages) throws DatabaseException;
    List<Course> sortCourse(int sortBy, boolean isAsc, int page, int pageSize, int[] totalPages) throws DatabaseException;
    List<Course> displayCourses(int page, int pageSize, int[] totalPages) throws DatabaseException;
}
