package ra.edu.utils;

import ra.edu.exception.InvalidInputException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    public static String getString(String prompt) throws InvalidInputException {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            throw new InvalidInputException("Dữ liệu không được để trống.");
        }
        return input;
    }

    public static int getPositiveInt(String prompt) throws InvalidInputException {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                if (value > 0) {
                    return value;
                }
                throw new InvalidInputException("Vui lòng nhập số lớn hơn 0.");
            } catch (NumberFormatException e) {
                throw new InvalidInputException("Vui lòng nhập số hợp lệ.");
            }
        }
    }

    public static LocalDate getDate(String prompt) throws InvalidInputException {
        while (true) {
            try {
                System.out.print(prompt);
                return LocalDate.parse(scanner.nextLine());
            } catch (DateTimeParseException e) {
                throw new InvalidInputException("Vui lòng nhập ngày theo định dạng yyyy-MM-dd.");
            }
        }
    }

    public static boolean getBoolean(String prompt) throws InvalidInputException {
        System.out.print(prompt + " (Nam: true, Nữ: false): ");
        String input = scanner.nextLine().trim().toLowerCase();
        if (input.equals("true") || input.equals("nam")) {
            return true;
        } else if (input.equals("false") || input.equals("nữ")) {
            return false;
        }
        throw new InvalidInputException("Vui lòng nhập 'Nam' hoặc 'Nữ'.");
    }

    public static int getChoice(int min, int max, String prompt) throws InvalidInputException {
        while (true) {
            try {
                System.out.print(prompt);
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= min && choice <= max) {
                    return choice;
                }
                throw new InvalidInputException("Vui lòng chọn từ " + min + " đến " + max + ".");
            } catch (NumberFormatException e) {
                throw new InvalidInputException("Vui lòng nhập số hợp lệ.");
            }
        }
    }
}
