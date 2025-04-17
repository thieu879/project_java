package ra.edu.controller;

import ra.edu.business.model.Student;
import ra.edu.business.service.student.IStudentService;
import ra.edu.business.service.student.StudentServiceImp;
import ra.edu.exception.DatabaseException;
import ra.edu.exception.ValidationException;
import ra.edu.utils.InputUtil;
import ra.edu.utils.TableFormatter;

import java.time.LocalDate;
import java.util.List;

public class StudentController {
    private IStudentService studentService = new StudentServiceImp();
    private static final int PAGE_SIZE = 2;

    public void addStudent() {
        try {
            Student student = new Student();
            student.setName(InputUtil.getNonEmptyString("Nhập tên học viên: "));
            student.setDob(InputUtil.getValidDate("Nhập ngày sinh (yyyy-MM-dd): "));
            student.setEmail(InputUtil.getNonEmptyString("Nhập email: "));
            student.setSex(InputUtil.getBoolean("Nhập giới tính (Nam/Nữ): "));
            student.setPhone(InputUtil.getNonEmptyString("Nhập số điện thoại: "));
            student.setCreateAt(LocalDate.now());
            student.setStatus("ACTIVE");
            String password = InputUtil.getNonEmptyString("Nhập mật khẩu: ");
            studentService.addStudent(student, password);
            System.out.println("Thêm học viên thành công!");
        } catch (ValidationException | DatabaseException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void updateStudent() {
        try {
            Student student = new Student();
            student.setId(InputUtil.getPositiveInt("Nhập ID học viên: "));
            student.setName(InputUtil.getNonEmptyString("Nhập tên học viên: "));
            student.setDob(InputUtil.getValidDate("Nhập ngày sinh (yyyy-MM-dd): "));
            student.setEmail(InputUtil.getNonEmptyString("Nhập email: "));
            student.setSex(InputUtil.getBoolean("Nhập giới tính (Nam/Nữ): "));
            student.setPhone(InputUtil.getNonEmptyString("Nhập số điện thoại: "));
            student.setCreateAt(LocalDate.now());
            student.setStatus("ACTIVE");
            String password = InputUtil.getNonEmptyString("Nhập mật khẩu mới (hoặc để trống): ");
            studentService.updateStudent(student, password.isEmpty() ? null : password);
            System.out.println("Cập nhật học viên thành công!");
        } catch (ValidationException | DatabaseException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void deleteStudent() {
        try {
            int id = InputUtil.getPositiveInt("Nhập ID học viên: ");
            studentService.deleteStudent(id);
            System.out.println("Xóa học viên thành công!");
        } catch (DatabaseException | ValidationException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void searchStudent() {
        try {
            String name = InputUtil.getNonEmptyString("Nhập tên học viên (hoặc để trống): ");
            String email = InputUtil.getNonEmptyString("Nhập email (hoặc để trống): ");
            int id = 0;
            try {
                id = InputUtil.getPositiveInt("Nhập ID học viên (hoặc 0 nếu không tìm theo ID): ");
            } catch (ValidationException ignored) {}
            int page = 1;
            int[] totalPages = new int[1];
            while (true) {
                List<Student> students = studentService.searchStudent(name.isEmpty() ? null : name, email.isEmpty() ? null : email, id, page, PAGE_SIZE, totalPages);
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
        } catch (DatabaseException | ValidationException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void sortStudent() {
        try {
            int sortBy = InputUtil.getChoice(1, 2, "Chọn tiêu chí sắp xếp (1: Tên, 2: Ngày sinh): ");
            boolean isAsc = InputUtil.getBoolean("Sắp xếp tăng dần? (Nam/Nữ): ");
            int page = 1;
            int[] totalPages = new int[1];
            while (true) {
                List<Student> students = studentService.sortStudent(sortBy, isAsc, page, PAGE_SIZE, totalPages);
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

    public void displayStudents() {
        try {
            int page = 1;
            int[] totalPages = new int[1];
            while (true) {
                List<Student> students = studentService.displayStudents(page, PAGE_SIZE, totalPages);
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
        } catch (DatabaseException | ValidationException e) {
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