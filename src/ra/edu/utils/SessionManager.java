package ra.edu.utils;

public class SessionManager {
    private static Integer adminId = null;
    private static Integer studentId = null;

    public static void loginAdmin(int id) {
        adminId = id;
        studentId = null; // Đảm bảo chỉ một loại user được đăng nhập
    }

    public static void loginStudent(int id) {
        studentId = id;
        adminId = null; // Đảm bảo chỉ một loại user được đăng nhập
    }

    public static void logout() {
        adminId = null;
        studentId = null;
    }

    public static Integer getAdminId() {
        return adminId;
    }

    public static Integer getStudentId() {
        return studentId;
    }

    public static boolean isAdminLoggedIn() {
        return adminId != null;
    }

    public static boolean isStudentLoggedIn() {
        return studentId != null;
    }
}