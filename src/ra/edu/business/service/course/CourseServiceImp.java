package ra.edu.business.service.course;

import ra.edu.business.dao.course.CourseDAOImp;
import ra.edu.business.dao.course.ICourseDAO;
import ra.edu.business.model.Course;
import ra.edu.exception.DatabaseException;
import ra.edu.exception.ValidationException;
import ra.edu.validate.CourseValidator;

import java.util.List;

public class CourseServiceImp implements ICourseService {
    private ICourseDAO courseDAO = new CourseDAOImp();

    @Override
    public void addCourse(Course course) throws ValidationException, DatabaseException {
        CourseValidator.validate(course);
        int result = courseDAO.addCourse(course);
        if (result == 0) {
            throw new DatabaseException("Khóa học đã tồn tại.");
        }
    }

    @Override
    public void updateCourse(Course course) throws ValidationException, DatabaseException {
        CourseValidator.validate(course);
        int result = courseDAO.updateCourse(course);
        if (result == 0) {
            throw new DatabaseException("Khóa học không tồn tại.");
        }
    }

    @Override
    public void deleteCourse(int id) throws DatabaseException {
        courseDAO.deleteCourse(id);
    }

    @Override
    public List<Course> searchCourse(String name, String instructor, int page, int pageSize, int[] totalPages) throws DatabaseException {
        return courseDAO.searchCourse(name, instructor, page, pageSize, totalPages);
    }

    @Override
    public List<Course> sortCourse(int sortBy, boolean isAsc, int page, int pageSize, int[] totalPages) throws DatabaseException {
        if (sortBy != 1 && sortBy != 2) {
            throw new DatabaseException("Tiêu chí sắp xếp không hợp lệ.");
        }
        return courseDAO.sortCourse(sortBy, isAsc, page, pageSize, totalPages);
    }

    @Override
    public List<Course> displayCourses(int page, int pageSize, int[] totalPages) throws DatabaseException {
        return courseDAO.displayCourses(page, pageSize, totalPages);
    }
}
