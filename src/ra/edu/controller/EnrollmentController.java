package ra.edu.controller;

import ra.edu.business.model.Course;
import ra.edu.business.model.Enrollment;
import ra.edu.business.model.Student;
import ra.edu.business.service.course.CourseServiceImp;
import ra.edu.business.service.enrollment.EnrollmentServiceImp;
import ra.edu.business.service.enrollment.IEnrollmentService;
import ra.edu.exception.DatabaseException;
import ra.edu.exception.ValidationException;
import ra.edu.utils.InputUtil;
import ra.edu.utils.Session;
import ra.edu.utils.TableFormatter;

import java.util.List;
import java.util.Scanner;

public class EnrollmentController {
    private IEnrollmentService enrollmentService = new EnrollmentServiceImp();
    private static final int PAGE_SIZE = 2;
    private Scanner scanner = new Scanner(System.in);

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
                int choice = InputUtil.getChoice(1, 4, "Chọn chức năng: ");
                if (choice == 1 && page < totalPages[0]) {
                    page++;
                } else if (choice == 2 && page > 1) {
                    page--;
                } else if (choice == 3) {
                    page = InputUtil.getPositiveInt("Nhập số trang muốn chuyển đến (1-" + totalPages[0] + "): ");
                    if (page < 1 || page > totalPages[0]) {
                        System.out.println("Số trang không hợp lệ. Vui lòng nhập từ 1 đến " + totalPages[0] + ".");
                        page = Math.max(1, Math.min(page, totalPages[0]));
                    }
                } else if (choice == 4) {
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
                int choice = InputUtil.getChoice(1, 4, "Chọn chức năng: ");
                if (choice == 1 && page < totalPages[0]) {
                    page++;
                } else if (choice == 2 && page > 1) {
                    page--;
                } else if (choice == 3) {
                    page = InputUtil.getPositiveInt("Nhập số trang muốn chuyển đến (1-" + totalPages[0] + "): ");
                    if (page < 1 || page > totalPages[0]) {
                        System.out.println("Số trang không hợp lệ. Vui lòng nhập từ 1 đến " + totalPages[0] + ".");
                        page = Math.max(1, Math.min(page, totalPages[0]));
                    }
                } else if (choice == 4) {
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

    public void displayCountCoursesAndStudents() {
        try {
            int[] counts = enrollmentService.countCoursesAndStudents();
            System.out.println("Tổng số khóa học: " + counts[0]);
            System.out.println("Tổng số học viên: " + counts[1]);
        } catch (DatabaseException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void displayTop5CoursesByStudents() {
        try {
            List<Object[]> top5Courses = enrollmentService.top5CoursesByStudents();
            TableFormatter.displayStatistics(top5Courses, "Top 5 khóa học có nhiều học viên nhất", "Tên khóa học", "Số học viên");
        } catch (DatabaseException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void displayCoursesWithMoreThan10Students() {
        try {
            List<Object[]> coursesWithMoreThan10 = enrollmentService.coursesWithMoreThan10Students();
            TableFormatter.displayStatistics(coursesWithMoreThan10, "Khóa học có trên 10 học viên", "Tên khóa học", "Số học viên");
        } catch (DatabaseException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void approveStudentEnrollment() {
        try {
            // Nhập courseId và hiển thị danh sách sinh viên đang chờ
            int courseId = InputUtil.getPositiveInt("Nhập ID khóa học: ");
            System.out.println("\nDanh sách sinh viên đang chờ duyệt cho khóa học " + courseId + ":");
            int page = 1;
            String status = "WAITING";
            int[] totalPages = new int[1];
            List<Student> students = enrollmentService.displayWaitingStudentsByCourse(courseId, page, PAGE_SIZE, totalPages);
            TableFormatter.displayStudents(students);
            System.out.println("Trang: " + page + "/" + totalPages[0]);

            // Nhập studentId và xác nhận
            int studentId = InputUtil.getPositiveInt("Nhập ID sinh viên cần duyệt: ");
            System.out.print("Bạn có chắc chắn muốn duyệt sinh viên này cho khóa học? (Y/N): ");
            String confirm = scanner.nextLine().trim().toUpperCase();
            if (!confirm.equals("Y")) {
                status = "DENIED";
                enrollmentService.approveStudentEnrollment(courseId, studentId, status);
                System.out.println("Đã hủy duyệt sinh viên.");
                return;
            }
            status = "CONFIRM";
            enrollmentService.approveStudentEnrollment(courseId, studentId, status);
            System.out.println("Duyệt sinh viên thành công!");
        } catch (DatabaseException | ValidationException e) {
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
        System.out.println("3. Chuyển đến trang");
        System.out.println("4. Quay lại");
    }
}