package ra.edu.business.service.student;

import ra.edu.business.model.Student;

import java.util.List;

public interface IStudentService {
    Student login(String email, String password) throws Exception;
    void addStudent(Student student) throws Exception;
    void updateStudent(Student student) throws Exception;
    void deleteStudent(int id) throws Exception;
    List<Student> getAllStudents() throws Exception;
    List<Student> searchStudent(String name, String email, int id) throws Exception;
    List<Student> sortStudent(int sortBy) throws Exception;
    List<Student> getStudentsByCourse(int courseId) throws Exception;
    void changePassword(int studentId, String oldPassword, String newPassword) throws Exception;
}
