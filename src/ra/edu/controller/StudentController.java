package ra.edu.controller;

import ra.edu.business.model.Student;
import ra.edu.business.service.student.IStudentService;
import ra.edu.business.service.student.StudentServiceImp;

import java.util.List;

public class StudentController {
    private final IStudentService studentService = new StudentServiceImp();

    public Student handleLogin(String email, String password) {
        try {
            return studentService.login(email, password);
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            return null;
        }
    }

    public boolean handleAddStudent(Student student) {
        try {
            studentService.addStudent(student);
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        }
    }

    public boolean handleUpdateStudent(Student student) {
        try {
            studentService.updateStudent(student);
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        }
    }

    public boolean handleDeleteStudent(int id) {
        try {
            studentService.deleteStudent(id);
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        }
    }

    public List<Student> handleGetAllStudents() {
        try {
            return studentService.getAllStudents();
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            return List.of();
        }
    }

    public List<Student> handleSearchStudent(String name, String email, int id) {
        try {
            return studentService.searchStudent(name, email, id);
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            return List.of();
        }
    }

    public List<Student> handleSortStudent(int sortBy) {
        try {
            return studentService.sortStudent(sortBy);
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            return List.of();
        }
    }

    public List<Student> handleGetStudentsByCourse(int courseId) {
        try {
            return studentService.getStudentsByCourse(courseId);
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            return List.of();
        }
    }

    public boolean handleChangePassword(int studentId, String oldPassword, String newPassword) {
        try {
            studentService.changePassword(studentId, oldPassword, newPassword);
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        }
    }
}
