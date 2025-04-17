package ra.edu.utils;

import ra.edu.business.model.Course;
import ra.edu.business.model.Student;

import java.util.List;

public class TableFormatter {
    public static void displayCourses(List<Course> courses) {
        if (courses.isEmpty()) {
            System.out.println("Không có khóa học nào.");
            return;
        }
        System.out.println("\n+-----+--------------------------------+----------+----------------+------------+");
        System.out.println("| ID  | Tên khóa học                   | Thời lượng | Giảng viên     | Ngày tạo   |");
        System.out.println("+-----+--------------------------------+----------+----------------+------------+");
        for (Course course : courses) {
            System.out.printf("| %-3d | %-30s | %-8d | %-14s | %-10s |%n",
                    course.getId(), course.getName(), course.getDuration(),
                    course.getInstructor(), course.getCreateAt());
        }
        System.out.println("+-----+--------------------------------+----------+----------------+------------+");
    }

    public static void displayStudents(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("Không có học viên nào.");
            return;
        }
        System.out.println("\n+-----+--------------------------------+------------+------------------------+--------+---------------+");
        System.out.println("| ID  | Tên học viên                   | Ngày sinh  | Email                  | Giới tính | SĐT           |");
        System.out.println("+-----+--------------------------------+------------+------------------------+--------+---------------+");
        for (Student student : students) {
            System.out.printf("| %-3d | %-30s | %-10s | %-22s | %-8s | %-13s |%n",
                    student.getId(), student.getName(), student.getDob(),
                    student.getEmail(), student.isSex() ? "Nam" : "Nữ", student.getPhone());
        }
        System.out.println("+-----+--------------------------------+------------+------------------------+--------+---------------+");
    }
}
