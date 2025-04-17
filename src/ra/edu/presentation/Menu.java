package ra.edu.presentation;

import ra.edu.controller.AdminController;
import ra.edu.controller.StudentController;
import ra.edu.exception.InvalidInputException;
import ra.edu.utils.InputUtil;
import ra.edu.utils.SessionManager;

public class Menu {
    private final AdminController adminController = new AdminController();
    private final StudentController studentController = new StudentController();
    public void start() {
        while (true) {
            try {
                System.out.println("\n============ HỆ THỐNG QUẢN LÝ ĐÀO TẠO ============");
                System.out.println("1. Đăng nhập với tư cách quản trị viên");
                System.out.println("2. Đăng nhập với tư cách học viên");
                System.out.println("===================================================");
                int choice = InputUtil.getChoice(1, 2, "Nhập lựa chọn: ");
                if (choice == 1) {
                    handleAdminLogin();
                } else {
                    handleStudentLogin();
                }
            } catch (InvalidInputException e) {
                System.out.println("Lỗi: " + e.getMessage());
            }
        }
    }
    private void handleAdminLogin() {
        try {
            String username = InputUtil.getString("Nhập tên đăng nhập: ");
            String password = InputUtil.getString("Nhập mật khẩu: ");
            var admin = adminController.handleLogin(username, password);
            if (admin != null) {
                SessionManager.loginAdmin(admin.getId());
                System.out.println("Đăng nhập quản trị viên thành công! ID: " + SessionManager.getAdminId());
                showAdminMenu();
            } else {
                System.out.println("Đăng nhập thất bại.");
            }
        } catch (InvalidInputException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    private void handleStudentLogin() {
        try {
            String email = InputUtil.getString("Nhập email: ");
            String password = InputUtil.getString("Nhập mật khẩu: ");
            var student = studentController.handleLogin(email, password);
            if (student != null) {
                SessionManager.loginStudent(student.getId());
                System.out.println("Đăng nhập học viên thành công! ID: " + SessionManager.getStudentId());
                showStudentMenu();
            } else {
                System.out.println("Đăng nhập thất bại.");
            }
        } catch (InvalidInputException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }
    private void showAdminMenu() {
        while (true) {
            try {
                System.out.println("\n============ MENU ADMIN ============");
                System.out.println("1. Quản lý khoá học");
                System.out.println("2. Quản lý học viên");
                System.out.println("3. Quản lý đăng ký học");
                System.out.println("4. Thống kê học viên theo khoá học");
                System.out.println("5. Đăng xuất");
                System.out.println("====================================");
                int choice = InputUtil.getChoice(1, 5, "Nhập lựa chọn: ");
                switch (choice) {
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        SessionManager.logout();
                        System.out.println("Đã đăng xuất. Session ID: " + SessionManager.getAdminId());
                        return;
                }
            } catch (InvalidInputException e) {
                System.out.println("Lỗi: " + e.getMessage());
            }
        }
    }
    private void showStudentMenu() {
        while (true) {
            try {
                System.out.println("\n============= Menu Học viên =============");
                System.out.println("1. Xem danh sách khoá học");
                System.out.println("2. Đăng ký khoá học");
                System.out.println("3. Xem khoá học đã đăng ký");
                System.out.println("4. Huỷ đăng ký");
                System.out.println("5. Đổi mật khẩu");
                System.out.println("6. Đăng xuất");
                System.out.println("========================================");
                int choice = InputUtil.getChoice(1, 6, "Nhập lựa chọn: ");
                switch (choice) {
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        String oldPassword = InputUtil.getString("Nhập mật khẩu cũ: ");
                        String newPassword = InputUtil.getString("Nhập mật khẩu mới: ");
                        if (studentController.handleChangePassword(SessionManager.getStudentId(), oldPassword, newPassword)) {
                            System.out.println("Đổi mật khẩu thành công!");
                        }
                        break;
                    case 6:
                        SessionManager.logout();
                        System.out.println("Đã đăng xuất. Session ID: " + SessionManager.getStudentId());
                        return;
                }
            } catch (InvalidInputException e) {
                System.out.println("Lỗi: " + e.getMessage());
            }
        }
    }
}
