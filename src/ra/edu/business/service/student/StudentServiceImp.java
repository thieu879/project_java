package ra.edu.business.service.student;

import ra.edu.business.dao.student.IStudentDAO;
import ra.edu.business.dao.student.StudentDAOImp;
import ra.edu.business.model.Student;
import ra.edu.exception.DatabaseException;
import ra.edu.exception.ValidationException;
import ra.edu.validate.StudentValidator;

import java.util.List;

public class StudentServiceImp implements IStudentService {
    private IStudentDAO studentDAO = new StudentDAOImp();

    @Override
    public void addStudent(Student student, String password) throws ValidationException, DatabaseException {
        StudentValidator.validate(student);
        int result = studentDAO.addStudent(student, password);
        if (result == 0) {
            throw new DatabaseException("Học viên đã tồn tại.");
        } else if (result == -1) {
            throw new DatabaseException("Lỗi không xác định khi thêm học viên.");
        }
    }

    @Override
    public void updateStudent(Student student, String password) throws ValidationException, DatabaseException {
        StudentValidator.validate(student);
        int result = studentDAO.updateStudent(student, password);
        if (result == 0) {
            throw new DatabaseException("Học viên không tồn tại.");
        }
    }

    @Override
    public void deleteStudent(int id) throws DatabaseException {
        int result = studentDAO.deleteStudent(id);
        if (result == 0) {
            throw new DatabaseException("Học viên không tồn tại.");
        }
    }

    @Override
    public List<Student> displayStudents(int page, int pageSize, int[] totalPages) throws DatabaseException {
        return studentDAO.displayStudents(page, pageSize, totalPages);
    }

    @Override
    public List<Student> searchStudent(String name, String email, int id, int page, int pageSize, int[] totalPages) throws DatabaseException {
        return studentDAO.searchStudent(name, email, id, page, pageSize, totalPages);
    }

    @Override
    public List<Student> sortStudent(int sortBy, boolean isAsc, int page, int pageSize, int[] totalPages) throws DatabaseException {
        if (sortBy != 1 && sortBy != 2) {
            throw new DatabaseException("Tiêu chí sắp xếp không hợp lệ.");
        }
        return studentDAO.sortStudent(sortBy, isAsc, page, pageSize, totalPages);
    }

    @Override
    public int getStudentIdByEmail(String email) throws DatabaseException {
        int studentId = studentDAO.getStudentIdByEmail(email);
        if (studentId == 0) {
            throw new DatabaseException("Học viên không tồn tại hoặc không hoạt động.");
        }
        return studentId;
    }
}