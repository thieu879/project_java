package ra.edu.controller;

import ra.edu.business.model.Course;
import ra.edu.business.service.course.CourseServiceImp;
import ra.edu.business.service.course.ICourseService;
import ra.edu.exception.DatabaseException;
import ra.edu.exception.ValidationException;
import ra.edu.utils.InputUtil;
import ra.edu.utils.TableFormatter;

import java.time.LocalDate;
import java.util.List;

public class CourseController {
    private ICourseService courseService = new CourseServiceImp();
    private static final int PAGE_SIZE = 2;

    public void addCourse() {
        try {
            Course course = new Course();
            course.setName(InputUtil.getNonEmptyString("Nhập tên khóa học: "));
            course.setDuration(InputUtil.getPositiveInt("Nhập thời lượng (giờ): "));
            course.setInstructor(InputUtil.getNonEmptyString("Nhập tên giảng viên: "));
            course.setCreateAt(LocalDate.now());
            course.setStatus("ACTIVE");
            courseService.addCourse(course);
            System.out.println("Thêm khóa học thành công!");
        } catch (ValidationException | DatabaseException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void updateCourse() {
        try {
            Course course = new Course();
            course.setId(InputUtil.getPositiveInt("Nhập ID khóa học: "));
            course.setName(InputUtil.getNonEmptyString("Nhập tên khóa học: "));
            course.setDuration(InputUtil.getPositiveInt("Nhập thời lượng (giờ): "));
            course.setInstructor(InputUtil.getNonEmptyString("Nhập tên giảng viên: "));
            course.setCreateAt(LocalDate.now());
            course.setStatus("ACTIVE");
            courseService.updateCourse(course);
            System.out.println("Cập nhật khóa học thành công!");
        } catch (ValidationException | DatabaseException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void deleteCourse() {
        try {
            int id = InputUtil.getPositiveInt("Nhập ID khóa học: ");
            courseService.deleteCourse(id);
            System.out.println("Xóa khóa học thành công!");
        } catch (DatabaseException | ValidationException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void searchCourse() {
        try {
            String name = InputUtil.getNonEmptyString("Nhập tên khóa học để tìm kiếm: ");
            int page = 1;
            int[] totalPages = new int[1];
            while (true) {
                List<Course> courses = courseService.searchCourse(name, page, PAGE_SIZE, totalPages);
                TableFormatter.displayCourses(courses);
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

    public void sortCourse() {
        try {
            int sortBy = InputUtil.getChoice(1, 2, "Chọn tiêu chí sắp xếp (1: Tên, 2: Thời lượng): ");
            boolean isAsc = InputUtil.getBoolean("Sắp xếp tăng dần? (Nam/Nữ): ");
            int page = 1;
            int[] totalPages = new int[1];
            while (true) {
                List<Course> courses = courseService.sortCourse(sortBy, isAsc, page, PAGE_SIZE, totalPages);
                TableFormatter.displayCourses(courses);
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

    public void displayCourses() {
        try {
            int page = 1;
            int[] totalPages = new int[1];
            while (true) {
                List<Course> courses = courseService.displayCourses(page, PAGE_SIZE, totalPages);
                TableFormatter.displayCourses(courses);
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