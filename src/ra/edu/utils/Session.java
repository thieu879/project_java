package ra.edu.utils;

public class Session {
    private static String userEmail;
    private static String userRole;
    private static Integer studentId;

    public static void login(String email, String role, Integer studentIdIfApplicable) {
        userEmail = email;
        userRole = role;
        studentId = (role.equals("STUDENT")) ? studentIdIfApplicable : null;
    }

    public static void logout() {
        userEmail = null;
        userRole = null;
        studentId = null;
    }

    public static String getUserEmail() {
        return userEmail;
    }

    public static String getUserRole() {
        return userRole;
    }

    public static Integer getStudentId() {
        return studentId;
    }

    public static boolean isLoggedIn() {
        return userEmail != null && userRole != null;
    }
}
