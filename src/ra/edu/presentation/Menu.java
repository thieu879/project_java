package ra.edu.presentation;

import ra.edu.business.model.Role;
import ra.edu.controller.AccountController;
import ra.edu.controller.CourseController;
import ra.edu.controller.EnrollmentController;
import ra.edu.controller.StudentController;
import ra.edu.utils.InputUtil;
import ra.edu.utils.Session;

public class Menu {
    private AccountController accountController = new AccountController();
    private CourseController courseController = new CourseController();
    private StudentController studentController = new StudentController();
    private EnrollmentController enrollmentController = new EnrollmentController();

    public void start() {
        while (true) {
            displayLoginMenu();
            try {
                int choice = InputUtil.getChoice(1, 3, "Chọn chức năng: ");
                switch (choice) {
                    case 1:
                        handleLogin();
                        break;
                    case 2:
                        handleRegister();
                        break;
                    case 3:
                        System.exit(0);
                }
                if (Session.isLoggedIn()) {
                    if (Session.getUserRole().equals(Role.ADMIN.getValue())) {
                        displayAdminMenu();
                    } else {
                        displayStudentMenu();
                    }
                }
            } catch (Exception e) {
                System.out.println("Lỗi: " + e.getMessage());
            }
        }
    }

    private void displayLoginMenu() {
        System.out.println("\n=== HỆ THỐNG QUẢN LÝ KHÓA HỌC VÀ HỌC VIÊN ===");
        System.out.println("1. Đăng nhập");
        System.out.println("2. Đăng ký");
        System.out.println("3. Thoát");
    }

    private void displayAdminMenu() {
        while (true) {
            System.out.println("\n=== MENU QUẢN TRỊ VIÊN ===");
            System.out.println("1. Quản lý khóa học");
            System.out.println("2. Quản lý học viên");
            System.out.println("3. Quản lý đăng ký");
            System.out.println("4. Thống kê");
            System.out.println("5. Đăng xuất");
            try {
                int choice = InputUtil.getChoice(1, 5, "Chọn chức năng: ");
                switch (choice) {
                    case 1:
                        manageCourses();
                        break;
                    case 2:
                        manageStudents();
                        break;
                    case 3:
                        manageEnrollments();
                        break;
                    case 4:
                        displayStatistics();
                        break;
                    case 5:
                        accountController.logoutAccount();
                        return;
                }
            } catch (Exception e) {
                System.out.println("Lỗi: " + e.getMessage());
            }
        }
    }

    private void displayStatistics() {
        while (true) {
            System.out.println("\n=== MENU THỐNG KÊ ===");
            System.out.println("1. Thống kê tổng số lượng khóa học và tổng số học viên");
            System.out.println("2. Thống kê tổng số học viên theo từng khóa");
            System.out.println("3. Thống kê top 5 khóa học đông sinh viên nhất");
            System.out.println("4. Liệt kê các khóa học có trên 10 học viên");
            System.out.println("5. Quay lại");
            try {
                int choice = InputUtil.getChoice(1, 5, "Chọn chức năng: ");
                switch (choice) {
                    case 1:
                        enrollmentController.displayCountCoursesAndStudents();
                        break;
                    case 2:
                        enrollmentController.countStudentsByCourse();
                        break;
                    case 3:
                        enrollmentController.displayTop5CoursesByStudents();
                        break;
                    case 4:
                        enrollmentController.displayCoursesWithMoreThan10Students();
                        break;
                    case 5:
                        return;
                }
            } catch (Exception e) {
                System.out.println("Lỗi: " + e.getMessage());
            }
        }
    }
    private void displayStudentMenu() {
        while (true) {
            System.out.println("\n=== MENU HỌC VIÊN ===");
            System.out.println("1. Xem danh sách khóa học");
            System.out.println("2. Đăng ký khóa học");
            System.out.println("3. Xem khóa học đã đăng ký");
            System.out.println("4. Hủy đăng ký khóa học");
            System.out.println("5. Đổi mật khẩu");
            System.out.println("6. Tìm kiếm khoá học");
            System.out.println("7. Đăng xuất");
            try {
                int choice = InputUtil.getChoice(1, 7, "Chọn chức năng: ");
                switch (choice) {
                    case 1:
                        courseController.displayCourses();
                        break;
                    case 2:
                        enrollmentController.registerCourse();
                        break;
                    case 3:
                        enrollmentController.viewRegisteredCourses();
                        break;
                    case 4:
                        enrollmentController.cancelRegistration();
                        break;
                    case 5:
                        accountController.changeStudentPassword();
                        break;
                    case 6:
                        courseController.searchCourse();
                        break;
                    case 7:
                        accountController.logoutAccount();
                        return;
                }
            } catch (Exception e) {
                System.out.println("Lỗi: " + e.getMessage());
            }
        }
    }

    private void handleLogin() {
        if (accountController.loginAccount()) {
            System.out.println("Đăng nhập thành công! Vai trò: " + Session.getUserRole());
        }
    }

    private void handleRegister() {
        accountController.registerAccount();
    }

    private void manageCourses() {
        while (true) {
            System.out.println("\n=== QUẢN LÝ KHÓA HỌC ===");
            System.out.println("1. Thêm khóa học");
            System.out.println("2. Cập nhật khóa học");
            System.out.println("3. Xóa khóa học");
            System.out.println("4. Tìm kiếm khóa học");
            System.out.println("5. Sắp xếp khóa học");
            System.out.println("6. Hiển thị danh sách khóa học");
            System.out.println("7. Quay lại");
            try {
                int choice = InputUtil.getChoice(1, 7, "Chọn chức năng: ");
                switch (choice) {
                    case 1:
                        courseController.addCourse();
                        break;
                    case 2:
                        courseController.updateCourse();
                        break;
                    case 3:
                        courseController.deleteCourse();
                        break;
                    case 4:
                        courseController.searchCourse();
                        break;
                    case 5:
                        courseController.sortCourse();
                        break;
                    case 6:
                        courseController.displayCourses();
                        break;
                    case 7:
                        return;
                }
            } catch (Exception e) {
                System.out.println("Lỗi: " + e.getMessage());
            }
        }
    }

    private void manageStudents() {
        while (true) {
            System.out.println("\n=== QUẢN LÝ HỌC VIÊN ===");
            System.out.println("1. Thêm học viên");
            System.out.println("2. Cập nhật học viên");
            System.out.println("3. Xóa học viên");
            System.out.println("4. Tìm kiếm học viên");
            System.out.println("5. Sắp xếp học viên");
            System.out.println("6. Hiển thị danh sách học viên");
            System.out.println("7. Quay lại");
            try {
                int choice = InputUtil.getChoice(1, 7, "Chọn chức năng: ");
                switch (choice) {
                    case 1:
                        studentController.addStudent();
                        break;
                    case 2:
                        studentController.updateStudent();
                        break;
                    case 3:
                        studentController.deleteStudent();
                        break;
                    case 4:
                        studentController.searchStudent();
                        break;
                    case 5:
                        studentController.sortStudent();
                        break;
                    case 6:
                        studentController.displayStudents();
                        break;
                    case 7:
                        return;
                }
            } catch (Exception e) {
                System.out.println("Lỗi: " + e.getMessage());
            }
        }
    }

    private void manageEnrollments() {
        while (true) {
            System.out.println("\n=== QUẢN LÝ ĐĂNG KÝ ===");
            System.out.println("1. Thêm học viên vào khóa học");
            System.out.println("2. Xóa học viên khỏi khóa học");
            System.out.println("3. Xem học viên theo khóa học");
            System.out.println("4. Xem số học viên theo khóa học");
            System.out.println("5. Duyệt sinh viên đăng ký khoá học");
            System.out.println("6. Quay lại");
            try {
                int choice = InputUtil.getChoice(1, 6, "Chọn chức năng: ");
                switch (choice) {
                    case 1:
                        enrollmentController.addStudentToCourse();
                        break;
                    case 2:
                        enrollmentController.removeStudentFromCourse();
                        break;
                    case 3:
                        enrollmentController.displayStudentsByCourse();
                        break;
                    case 4:
                        enrollmentController.countStudentsByCourse();
                        break;
                    case 5:
                        enrollmentController.approveStudentEnrollment();
                    case 6:
                        return;
                }
            } catch (Exception e) {
                System.out.println("Lỗi: " + e.getMessage());
            }
        }
    }
}