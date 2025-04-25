package ra.edu.presentation;

import ra.edu.business.model.Role;
import ra.edu.controller.AccountController;
import ra.edu.controller.CourseController;
import ra.edu.controller.EnrollmentController;
import ra.edu.controller.StudentController;
import ra.edu.utils.InputUtil;
import ra.edu.utils.Session;

public class Menu {
    // ANSI color codes
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String RED = "\u001B[31m";

    private static final int MAIN_FRAME_WIDTH = 47;
    private static final int SUB_FRAME_WIDTH = 62;
    private static final int MANAGER_COURSE_FRAME_WIDTH = 61;

    private AccountController accountController = new AccountController();
    private CourseController courseController = new CourseController();
    private StudentController studentController = new StudentController();
    private EnrollmentController enrollmentController = new EnrollmentController();

    public void start() {
        while (true) {
            displayLoginMenu();
            try {
                int choice = InputUtil.getChoice(1, 3, CYAN + "➤ Chọn chức năng: " + RESET);
                switch (choice) {
                    case 1:
                        handleLogin();
                        break;
                    case 2:
                        handleRegister();
                        break;
                    case 3:
                        System.out.println(RED + "Đã thoát chương trình." + RESET);
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
                System.out.println(RED + "Lỗi: " + e.getMessage() + RESET);
            }
        }
    }

    private void displayLoginMenu() {
        System.out.println(CYAN + "\n╔═══════════════════════════════════════════════╗" + RESET);
        System.out.printf(CYAN + "║%-" + MAIN_FRAME_WIDTH + "s║%n" + RESET, centerText("HỆ THỐNG QUẢN LÝ KHÓA HỌC VÀ HỌC VIÊN", MAIN_FRAME_WIDTH));
        System.out.println(CYAN + "╠═══════════════════════════════════════════════╣" + RESET);
        System.out.printf(CYAN + "║ " + GREEN + "%-45s" + RESET + " ║%n", "1. Đăng nhập");
        System.out.printf(CYAN + "║ " + GREEN + "%-45s" + RESET + " ║%n", "2. Đăng ký");
        System.out.printf(CYAN + "║ " + GREEN + "%-45s" + RESET + " ║%n", "3. Thoát");
        System.out.println(CYAN + "╚═══════════════════════════════════════════════╝" + RESET);
    }

    private void displayAdminMenu() {
        while (true) {
            System.out.println(BLUE + "\n╔═══════════════════════════════════════════════╗" + RESET);
            System.out.printf(BLUE + "║%-" + MAIN_FRAME_WIDTH + "s║%n" + RESET, centerText("MENU QUẢN TRỊ VIÊN", MAIN_FRAME_WIDTH));
            System.out.println(BLUE + "╠═══════════════════════════════════════════════╣" + RESET);
            System.out.printf(BLUE + "║ " + YELLOW + "%-45s" + RESET + " ║%n", "1. Quản lý khóa học");
            System.out.printf(BLUE + "║ " + YELLOW + "%-45s" + RESET + " ║%n", "2. Quản lý học viên");
            System.out.printf(BLUE + "║ " + YELLOW + "%-45s" + RESET + " ║%n", "3. Quản lý đăng ký");
            System.out.printf(BLUE + "║ " + YELLOW + "%-45s" + RESET + " ║%n", "4. Thống kê");
            System.out.printf(BLUE + "║ " + YELLOW + "%-45s" + RESET + " ║%n", "5. Đăng xuất");
            System.out.println(BLUE + "╚═══════════════════════════════════════════════╝" + RESET);
            try {
                int choice = InputUtil.getChoice(1, 5, CYAN + "➤ Chọn chức năng: " + RESET);
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
                        System.out.println(GREEN + "Đã đăng xuất." + RESET);
                        return;
                }
            } catch (Exception e) {
                System.out.println(RED + "Lỗi: " + e.getMessage() + RESET);
            }
        }
    }

    private void displayStatistics() {
        while (true) {
            System.out.println(BLUE + "\n    ╔══════════════════════════════════════════════════════════════╗" + RESET);
            System.out.printf(BLUE + "    ║%-" + SUB_FRAME_WIDTH + "s║%n" + RESET, centerText("MENU THỐNG KÊ", SUB_FRAME_WIDTH));
            System.out.println(BLUE + "    ╠══════════════════════════════════════════════════════════════╣" + RESET);
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "1. Thống kê tổng số lượng khóa học và tổng số học viên");
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "2. Thống kê tổng số học viên theo từng khóa");
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "3. Thống kê top 5 khóa học đông sinh viên nhất");
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "4. Liệt kê các khóa học có trên 10 học viên");
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "5. Quay lại");
            System.out.println(BLUE + "    ╚══════════════════════════════════════════════════════════════╝" + RESET);
            try {
                int choice = InputUtil.getChoice(1, 5, CYAN + "    ➤ Chọn chức năng: " + RESET);
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
                System.out.println(RED + "    Lỗi: " + e.getMessage() + RESET);
            }
        }
    }

    private void displayStudentMenu() {
        while (true) {
            System.out.println(BLUE + "\n╔═══════════════════════════════════════════════╗" + RESET);
            System.out.printf(BLUE + "║%-" + MAIN_FRAME_WIDTH + "s║%n" + RESET, centerText("MENU HỌC VIÊN", MAIN_FRAME_WIDTH));
            System.out.println(BLUE + "╠═══════════════════════════════════════════════╣" + RESET);
            System.out.printf(BLUE + "║ " + YELLOW + "%-45s" + RESET + " ║%n", "1. Xem danh sách khóa học");
            System.out.printf(BLUE + "║ " + YELLOW + "%-45s" + RESET + " ║%n", "2. Đăng ký khóa học");
            System.out.printf(BLUE + "║ " + YELLOW + "%-45s" + RESET + " ║%n", "3. Xem khóa học đã đăng ký");
            System.out.printf(BLUE + "║ " + YELLOW + "%-45s" + RESET + " ║%n", "4. Hủy đăng ký khóa học");
            System.out.printf(BLUE + "║ " + YELLOW + "%-45s" + RESET + " ║%n", "5. Đổi mật khẩu");
            System.out.printf(BLUE + "║ " + YELLOW + "%-45s" + RESET + " ║%n", "6. Tìm kiếm khóa học");
            System.out.printf(BLUE + "║ " + YELLOW + "%-45s" + RESET + " ║%n", "7. Đăng xuất");
            System.out.println(BLUE + "╚═══════════════════════════════════════════════╝" + RESET);
            try {
                int choice = InputUtil.getChoice(1, 7, CYAN + "➤ Chọn chức năng: " + RESET);
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
                        System.out.println(GREEN + "Đã đăng xuất." + RESET);
                        return;
                }
            } catch (Exception e) {
                System.out.println(RED + "Lỗi: " + e.getMessage() + RESET);
            }
        }
    }

    private void handleLogin() {
        if (accountController.loginAccount()) {
            try {
                String name = accountController.getStudentNameByEmail();
//                System.out.println(GREEN + "Đăng nhập thành công! Vai trò: " + Session.getUserRole() + " | Tên: " + name + RESET);
                String message = GREEN + "Đăng nhập thành công! Vai trò: " + Session.getUserRole() + " | Tên: " + name + RESET;
                for (int i = 0; i < message.length(); i++) {
                    System.out.print(message.charAt(i));
                    Thread.sleep(50);  // Tạm dừng 100ms mỗi ký tự
                }
            } catch (InterruptedException e) {
                System.out.println("\nThread bị gián đoạn!");
            }

        }
    }

    private void handleRegister() {
        accountController.registerAccount();
    }

    private void manageCourses() {
        while (true) {
            System.out.println(BLUE + "\n    ╔══════════════════════════════════════════════════════════════╗" + RESET);
            System.out.printf(BLUE + "    ║%-" + SUB_FRAME_WIDTH + "s║%n" + RESET, centerText("QUẢN LÝ KHÓA HỌC", MANAGER_COURSE_FRAME_WIDTH));
            System.out.println(BLUE + "    ╠══════════════════════════════════════════════════════════════╣" + RESET);
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "1. Thêm khóa học");
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "2. Cập nhật khóa học");
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "3. Xóa khóa học");
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "4. Tìm kiếm khóa học");
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "5. Sắp xếp khóa học");
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "6. Hiển thị danh sách khóa học");
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "7. Quay lại");
            System.out.println(BLUE + "    ╚══════════════════════════════════════════════════════════════╝" + RESET);
            try {
                int choice = InputUtil.getChoice(1, 7, CYAN + "    ➤ Chọn chức năng: " + RESET);
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
                System.out.println(RED + "    Lỗi: " + e.getMessage() + RESET);
            }
        }
    }

    private void manageStudents() {
        while (true) {
            System.out.println(BLUE + "\n    ╔══════════════════════════════════════════════════════════════╗" + RESET);
            System.out.printf(BLUE + "    ║%-" + SUB_FRAME_WIDTH + "s║%n" + RESET, centerText("QUẢN LÝ HỌC VIÊN", MANAGER_COURSE_FRAME_WIDTH));
            System.out.println(BLUE + "    ╠══════════════════════════════════════════════════════════════╣" + RESET);
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "1. Thêm học viên");
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "2. Cập nhật học viên");
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "3. Xóa học viên");
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "4. Tìm kiếm học viên");
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "5. Sắp xếp học viên");
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "6. Hiển thị danh sách học viên");
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "7. Quay lại");
            System.out.println(BLUE + "    ╚══════════════════════════════════════════════════════════════╝" + RESET);
            try {
                int choice = InputUtil.getChoice(1, 7, CYAN + "    ➤ Chọn chức năng: " + RESET);
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
                System.out.println(RED + "    Lỗi: " + e.getMessage() + RESET);
            }
        }
    }

    private void manageEnrollments() {
        while (true) {
            System.out.println(BLUE + "\n    ╔══════════════════════════════════════════════════════════════╗" + RESET);
            System.out.printf(BLUE + "    ║%-" + SUB_FRAME_WIDTH + "s║%n" + RESET, centerText("QUẢN LÝ ĐĂNG KÝ", MANAGER_COURSE_FRAME_WIDTH));
            System.out.println(BLUE + "    ╠══════════════════════════════════════════════════════════════╣" + RESET);
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "1. Thêm học viên vào khóa học");
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "2. Xóa học viên khỏi khóa học");
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "3. Xem học viên theo khóa học");
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "4. Xem số học viên theo khóa học");
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "5. Duyệt sinh viên đăng ký khóa học");
            System.out.printf(BLUE + "    ║    " + YELLOW + "%-58s" + RESET + "║%n", "6. Quay lại");
            System.out.println(BLUE + "    ╚══════════════════════════════════════════════════════════════╝" + RESET);
            try {
                int choice = InputUtil.getChoice(1, 6, CYAN + "    ➤ Chọn chức năng: " + RESET);
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
                        break;
                    case 6:
                        return;
                }
            } catch (Exception e) {
                System.out.println(RED + "    Lỗi: " + e.getMessage() + RESET);
            }
        }
    }

    // Helper method to center text within a specified width
    private String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        if (padding < 0) padding = 0;
        return " ".repeat(padding) + text + " ".repeat(width - text.length() - padding);
    }
}