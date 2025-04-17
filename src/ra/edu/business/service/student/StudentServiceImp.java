package ra.edu.business.service.student;

import ra.edu.business.dao.student.StudentDAO;
import ra.edu.business.dao.student.StudentDAOImp;
import ra.edu.business.model.Student;
import ra.edu.exception.InvalidInputException;
import ra.edu.validate.StudentValidator;

import java.util.List;

public class StudentServiceImp implements IStudentService {
    private final StudentDAO studentDAO = new StudentDAOImp();

    @Override
    public Student login(String email, String password) throws Exception {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new InvalidInputException("Email và mật khẩu không được để trống.");
        }
        Student student = studentDAO.login(email, password);
        if (student == null) {
            throw new InvalidInputException("Đăng nhập thất bại. Email hoặc mật khẩu không đúng.");
        }
        return student;
    }

    @Override
    public void addStudent(Student student) throws Exception {
        StudentValidator.validate(student);
        studentDAO.addStudent(student);
    }

    @Override
    public void updateStudent(Student student) throws Exception {
        StudentValidator.validate(student);
        studentDAO.updateStudent(student);
    }

    @Override
    public void deleteStudent(int id) throws Exception {
        if (id <= 0) {
            throw new InvalidInputException("Mã học viên phải lớn hơn 0.");
        }
        studentDAO.deleteStudent(id);
    }

    @Override
    public List<Student> getAllStudents() throws Exception {
        return studentDAO.getAllStudents();
    }

    @Override
    public List<Student> searchStudent(String name, String email, int id) throws Exception {
        return studentDAO.searchStudent(name, email, id);
    }

    @Override
    public List<Student> sortStudent(int sortBy) throws Exception {
        if (sortBy != 1 && sortBy != 2) {
            throw new InvalidInputException("Tùy chọn sắp xếp không hợp lệ.");
        }
        return studentDAO.sortStudent(sortBy);
    }

    @Override
    public List<Student> getStudentsByCourse(int courseId) throws Exception {
        if (courseId <= 0) {
            throw new InvalidInputException("Mã khóa học phải lớn hơn 0.");
        }
        return studentDAO.getStudentsByCourse(courseId);
    }

    @Override
    public void changePassword(int studentId, String oldPassword, String newPassword) throws Exception {
        if (studentId <= 0) {
            throw new InvalidInputException("Mã học viên phải lớn hơn 0.");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new InvalidInputException("Mật khẩu mới không được để trống.");
        }
        studentDAO.changePassword(studentId, oldPassword, newPassword);
    }
}
