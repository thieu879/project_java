package ra.edu.utils;

import ra.edu.business.model.Course;
import ra.edu.business.model.Enrollment;
import ra.edu.business.model.Student;

import java.util.List;

public class TableFormatter {
    public static void displayCourses(List<Course> courses) {
        System.out.println("\nDanh sách khóa học:");
        System.out.printf("%-5s %-30s %-10s %-20s %-15s %-10s%n", "ID", "Tên khóa học", "Thời lượng", "Giảng viên", "Ngày tạo", "Trạng thái");
        System.out.println("-".repeat(90));
        for (Course course : courses) {
            System.out.printf("%-5d %-30s %-10d %-20s %-15s %-10s%n",
                    course.getId(), course.getName(), course.getDuration(), course.getInstructor(),
                    course.getCreateAt(), course.getStatus());
        }
    }

    public static void displayStudents(List<Student> students) {
        System.out.println("\nDanh sách học viên:");
        System.out.printf("%-5s %-20s %-12s %-25s %-10s %-15s %-15s %-10s%n",
                "ID", "Tên", "Ngày sinh", "Email", "Giới tính", "SĐT", "Ngày tạo", "Trạng thái");
        System.out.println("-".repeat(120));
        for (Student student : students) {
            System.out.printf("%-5d %-20s %-12s %-25s %-10s %-15s %-15s %-10s%n",
                    student.getId(), student.getName(), student.getDob(), student.getEmail(),
                    student.isSex() ? "Nam" : "Nữ", student.getPhone(), student.getCreateAt(), student.getStatus());
        }
    }

    public static void displayStatistics(List<Object[]> stats, String title, String col1, String col2) {
        System.out.println("\n" + title + ":");
        System.out.printf("%-30s %-15s%n", col1, col2);
        System.out.println("-".repeat(45));
        for (Object[] stat : stats) {
            System.out.printf("%-30s %-15s%n", stat[0], stat[1]);
        }
    }
}