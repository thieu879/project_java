package ra.edu.business.dao.student;

import ra.edu.business.model.Student;
import ra.edu.exception.DatabaseException;

import java.util.List;

public interface IStudentDAO {
    int addStudent(Student student, String password) throws DatabaseException;
    void updateStudent(Student student, String password) throws DatabaseException;
    void deleteStudent(int id) throws DatabaseException;
    List<Student> displayStudents(int page, int pageSize, int[] totalPages) throws DatabaseException;
    List<Student> searchStudent(String name, String email, int id, int page, int pageSize, int[] totalPages) throws DatabaseException;
    List<Student> sortStudent(int sortBy, boolean isAsc, int page, int pageSize, int[] totalPages) throws DatabaseException;
    int getStudentIdByEmail(String email) throws DatabaseException;
}
