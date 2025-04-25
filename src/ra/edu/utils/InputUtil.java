package ra.edu.utils;

import ra.edu.exception.ValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    public static int getPositiveInt(String prompt) throws ValidationException {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine());
                if (value > 0) return value;
                PrintError.println("Vui lòng nhập số lớn hơn 0.");
            } catch (NumberFormatException e) {
                PrintError.println("Vui lòng nhập số hợp lệ.");
            }
        }
    }

    public static String getNonEmptyString(String prompt) throws ValidationException {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input.trim().isEmpty()) {
                PrintError.println("Dữ liệu không được để trống.");
            }else {
                return input;
            }
        }
    }

    public static LocalDate getValidDate(String prompt) throws ValidationException {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalDate.parse(scanner.nextLine());
            } catch (DateTimeParseException e) {
                PrintError.println("Vui lòng nhập ngày theo định dạng yyyy-MM-dd.");
            }
        }
    }

    public static boolean getBoolean(String prompt) throws ValidationException {
        System.out.print(prompt);
        String input = scanner.nextLine().toLowerCase();
        if (input.equals("nam") || input.equals("1")) return true;
        if (input.equals("nữ") || input.equals("0")) return false;
        throw new ValidationException("Vui lòng nhập 'Nam' hoặc 'Nữ'.");
    }

    public static int getChoice(int min, int max, String prompt) throws ValidationException {
        while (true) {
            System.out.print(prompt);
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= min && choice <= max) return choice;
                PrintError.println("Vui lòng chọn từ " + min + " đến " + max + ".");
            } catch (NumberFormatException e) {
                throw new ValidationException("Vui lòng nhập số hợp lệ.");
            }
        }
    }

    public static String validateEmail(String prompt) throws ValidationException {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input.trim().isEmpty()) {
                PrintError.println("Dữ liệu không được để trống.");
            }else if (!input.matches("^[A-Za-z0-9]+@gmail\\.com$")) {
                PrintError.println("Sai định dạng email. Làm ơn nhập kiểu example@gmail.com!");
            }else {
                return input;
            }
        }
    }
}