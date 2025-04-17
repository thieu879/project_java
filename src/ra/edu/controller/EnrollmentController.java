package ra.edu.controller;

import ra.edu.business.model.Course;
import ra.edu.business.model.Student;
import ra.edu.business.service.enrollment.EnrollmentServiceImp;
import ra.edu.business.service.enrollment.IEnrollmentService;
import ra.edu.exception.DatabaseException;
import ra.edu.exception.ValidationException;
import ra.edu.utils.InputUtil;
import ra.edu.utils.Session;
import ra.edu.utils.TableFormatter;

import java.util.List;

public class EnrollmentController {
    private IEnrollmentService enrollmentService = new EnrollmentServiceImp();
    private static final int PAGE_SIZE = 2;

    public void addStudentToCourse() {
        try {
            int studentId = InputUtil.getPositiveInt("Nhập ID học viên: ");
            int courseId = InputUtil.getPositiveInt("Nhập ID khóa học: ");
            enrollmentService.addStudentToCourse(studentId, courseId);
            System.out.println("Thêm học viên vào khóa học thành công!");
        } catch (ValidationException | DatabaseException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void removeStudentFromCourse() {
        try {
            int studentId = InputUtil.getPositiveInt("Nhập ID học viên: ");
            int courseId = InputUtil.getPositiveInt("Nhập ID khóa học: ");
            enrollmentService.removeStudentFromCourse(studentId, courseId);
            System.out.println("Xóa học viên khỏi khóa học thành công!");
        } catch (ValidationException | DatabaseException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void displayStudentsByCourse() {
        try {
            int courseId = InputUtil.getPositiveInt("Nhập ID khóa học: ");
            int page = 1;
            int[] totalPages = new int[1];
            while (true) {
                List<Student> students = enrollmentService.displayStudentsByCourse(courseId, page, PAGE_SIZE, totalPages);
                TableFormatter.displayStudents(students);
                System.out.println("Trang: " + page + "/" + totalPages[0]);
                displayPagingMenu();
                int choice = InputUtil.getChoice(1, 3, "Chọn chức năng: ");
                if (choice == 1 && page < totalPages[0]) {
                    page++;
                } else if (choice == 2 && page > 1) {
                    page--;
                } else if (choice == 3) {
                    break;
                }
            }
        } catch (ValidationException | DatabaseException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void registerCourse() {
        try {
            if (!Session.isLoggedIn() || !Session.getUserRole().equals("STUDENT")) {
                throw new ValidationException("Chỉ học viên đang đăng nhập mới có thể đăng ký khóa học.");
            }
            int courseId = InputUtil.getPositiveInt("Nhập ID khóa học: ");
            enrollmentService.registerCourse(Session.getStudentId(), courseId);
            System.out.println("Đăng ký khóa học thành công!");
        } catch (ValidationException | DatabaseException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void viewRegisteredCourses() {
        try {
            if (!Session.isLoggedIn() || !Session.getUserRole().equals("STUDENT")) {
                throw new ValidationException("Chỉ học viên đang đăng nhập mới có thể xem khóa học đã đăng ký.");
            }
            int page = 1;
            int[] totalPages = new int[1];
            while (true) {
                List<Course> courses = enrollmentService.viewRegisteredCourses(Session.getStudentId(), page, PAGE_SIZE, totalPages);
                TableFormatter.displayCourses(courses);
                System.out.println("Trang: " + page + "/" + totalPages[0]);
                displayPagingMenu();
                int choice = InputUtil.getChoice(1, 3, "Chọn chức năng: ");
                if (choice == 1 && page < totalPages[0]) {
                    page++;
                } else if (choice == 2 && page > 1) {
                    page--;
                } else if (choice == 3) {
                    break;
                }
            }
        } catch (ValidationException | DatabaseException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void cancelRegistration() {
        try {
            if (!Session.isLoggedIn() || !Session.getUserRole().equals("STUDENT")) {
                throw new ValidationException("Chỉ học viên đang đăng nhập mới có thể hủy đăng ký.");
            }
            int courseId = InputUtil.getPositiveInt("Nhập ID khóa học: ");
            enrollmentService.cancelRegistration(Session.getStudentId(), courseId);
            System.out.println("Hủy đăng ký thành công!");
        } catch (ValidationException | DatabaseException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void displayStatistics() {
        try {
            int[] counts = enrollmentService.countCoursesAndStudents();
            System.out.println("Tổng số khóa học: " + counts[0]);
            System.out.println("Tổng số học viên: " + counts[1]);

            List<Object[]> top5Courses = enrollmentService.top5CoursesByStudents();
            TableFormatter.displayStatistics(top5Courses, "Top 5 khóa học có nhiều học viên nhất", "Tên khóa học", "Số học viên");

            List<Object[]> coursesWithMoreThan10 = enrollmentService.coursesWithMoreThan10Students();
            TableFormatter.displayStatistics(coursesWithMoreThan10, "Khóa học có trên 10 học viên", "Tên khóa học", "Số học viên");
        } catch (DatabaseException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void countStudentsByCourse() {
        try {
            int courseId = InputUtil.getPositiveInt("Nhập ID khóa học: ");
            int count = enrollmentService.countStudentsByCourse(courseId);
            System.out.println("Số học viên trong khóa học: " + count);
        } catch (ValidationException | DatabaseException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    private void displayPagingMenu() {
        System.out.println("\n=== MENU PHÂN TRANG ===");
        System.out.println("1. Trang tiếp");
        System.out.println("2. Trang trước");
        System.out.println("3. Quay lại");
    }
}