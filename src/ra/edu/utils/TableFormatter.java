package ra.edu.utils;

import ra.edu.business.model.Course;
import ra.edu.business.model.Enrollment;
import ra.edu.business.model.Student;

import java.util.List;

public class TableFormatter {
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";

    private static final int COURSE_TABLE_WIDTH = 97;
    private static final int STUDENT_TABLE_WIDTH = 120;
    private static final int STATS_TABLE_WIDTH = 45;

    public static void displayCourses(List<Course> courses) {
        System.out.println(CYAN + "\n╔═" + "═".repeat(COURSE_TABLE_WIDTH) + "═╗" + RESET);
        System.out.printf(CYAN + "║%-" + COURSE_TABLE_WIDTH + "s║%n" + RESET, centerText("Danh sách khóa học", 99));
        System.out.println(CYAN + "╠═" + "═".repeat(COURSE_TABLE_WIDTH) + "═╣" + RESET);
        System.out.printf(CYAN + "║ " + YELLOW + "%-5s %-30s %-10s %-20s %-15s %-12s" + RESET + " ║%n",
                "ID", "Tên khóa học", "Thời lượng", "Giảng viên", "Ngày tạo", "Trạng thái");
        System.out.println(CYAN + "╟─" + "─".repeat(COURSE_TABLE_WIDTH) + "─╢" + RESET);
        for (Course course : courses) {
            System.out.printf(CYAN + "║ " + GREEN + "%-5d %-30s %-10d %-20s %-15s %-12s" + RESET + " ║%n",
                    course.getId(),
                    truncate(course.getName(), 30),
                    course.getDuration(),
                    truncate(course.getInstructor(), 20),
                    truncate(String.valueOf(course.getCreateAt()), 15),
                    truncate(course.getStatus(), 10));
        }
        System.out.println(CYAN + "╚═" + "═".repeat(COURSE_TABLE_WIDTH) + "═╝" + RESET);
    }

    public static void displayRegisteredCourses(List<Pair<Course, String>> courses) {
        System.out.println(CYAN + "\n╔═" + "═ " + "═".repeat(101) + "═╗" + RESET);
        System.out.printf(CYAN + "║%-" + 105 + "s║%n" + RESET,
                centerText("Danh sách khóa học đã đăng ký", 103));
        System.out.println(CYAN + "╠═" + "═".repeat(103) + "═╣" + RESET);
        System.out.printf(CYAN + "║ " + YELLOW + "%-5s %-30s %-10s %-20s %-15s %-15s" + RESET + " ║%n",
                "ID", "Tên khóa học", "Thời lượng", "Giảng viên", "Ngày tạo", "Trạng thái đăng ký");
        System.out.println(CYAN + "╟─" + "─".repeat(103) + "─╢" + RESET);

        for (Pair<Course, String> pair : courses) {
            Course course = pair.getLeft();
            String enrollmentStatus = pair.getRight();
            System.out.printf(CYAN + "║ " + GREEN + "%-5d %-30s %-10d %-20s %-15s %-18s" + RESET + " ║%n",
                    course.getId(),
                    truncate(course.getName(), 30),
                    course.getDuration(),
                    truncate(course.getInstructor(), 20),
                    truncate(String.valueOf(course.getCreateAt()), 15),
                    translateEnrollmentStatus(truncate(enrollmentStatus, 15)));
        }
        System.out.println(CYAN + "╚═" + "═".repeat(103) + "═╝" + RESET);
    }
    private static String translateEnrollmentStatus(String status) {
        switch (status.toUpperCase()) {
            case "WAITING":
                return "Đang chờ";
            case "CONFIRM":
                return "Đã xác nhận";
            case "DENIED":
                return "Bị từ chối";
            case "CANCER":
                return "Đã hủy";
            default:
                return status;
        }
    }

    public static void displayStudents(List<Student> students) {
        System.out.println(CYAN + "\n╔═" + "═".repeat(STUDENT_TABLE_WIDTH) + "═╗" + RESET);
        System.out.printf(CYAN + "║%-" + STUDENT_TABLE_WIDTH + "s║%n" + RESET, centerText("Danh sách học viên", 122));
        System.out.println(CYAN + "╠═" + "═".repeat(STUDENT_TABLE_WIDTH) + "═╣" + RESET);
        System.out.printf(CYAN + "║ " + YELLOW + "%-5s %-20s %-12s %-25s %-10s %-15s %-15s %-11s" + RESET + " ║%n",
                "ID", "Tên", "Ngày sinh", "Email", "Giới tính", "SĐT", "Ngày tạo", "Trạng thái");
        System.out.println(CYAN + "╟─" + "─".repeat(STUDENT_TABLE_WIDTH) + "─╢" + RESET);
        for (Student student : students) {
            System.out.printf(CYAN + "║ " + GREEN + "%-5d %-20s %-12s %-25s %-10s %-15s %-15s %-11s" + RESET + " ║%n",
                    student.getId(),
                    truncate(student.getName(), 20),
                    truncate(String.valueOf(student.getDob()), 12),
                    truncate(student.getEmail(), 25),
                    student.isSex() ? "Nam" : "Nữ",
                    truncate(student.getPhone(), 15),
                    truncate(String.valueOf(student.getCreateAt()), 15),
                    truncate(student.getStatus(), 10));
        }
        System.out.println(CYAN + "╚═" + "═".repeat(STUDENT_TABLE_WIDTH) + "═╝" + RESET);
    }

    public static void displayStatistics(List<Object[]> stats, String title, String col1, String col2) {
        System.out.println(CYAN + "\n╔═" + "═".repeat(STATS_TABLE_WIDTH) + "═╗" + RESET);
        System.out.printf(CYAN + "║%-" + STATS_TABLE_WIDTH + "s║%n" + RESET, centerText(title, 47));
        System.out.println(CYAN + "╠═" + "═".repeat(STATS_TABLE_WIDTH) + "═╣" + RESET);
        System.out.printf(CYAN + "║ " + YELLOW + "%-30s %-14s" + RESET + " ║%n", col1, col2);
        System.out.println(CYAN + "╟─" + "─".repeat(STATS_TABLE_WIDTH) + "─╢" + RESET);
        for (Object[] stat : stats) {
            System.out.printf(CYAN + "║ " + GREEN + "%-30s %-14s" + RESET + " ║%n",
                    truncate(stat[0].toString(), 30),
                    truncate(stat[1].toString(), 13));
        }
        System.out.println(CYAN + "╚═" + "═".repeat(STATS_TABLE_WIDTH) + "═╝" + RESET);
    }

    private static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }

    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        if (padding < 0) padding = 0;
        return " ".repeat(padding) + text + " ".repeat(width - text.length() - padding);
    }
}