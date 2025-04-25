package ra.edu.validate;

import ra.edu.business.model.Account;
import ra.edu.business.model.Role;
import ra.edu.exception.ValidationException;
import ra.edu.utils.PrintError;

import java.util.regex.Pattern;

public class AccountValidator {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9]+@gmail\\.com$";
    private static final int MIN_PASSWORD_LENGTH = 6;

    public static void validate(Account account) throws ValidationException {
        if (account == null) {
            throw new ValidationException("Tài khoản không được null.");
        }
        if (account.getEmail() == null || !Pattern.matches(EMAIL_REGEX, account.getEmail())) {
            throw new ValidationException("Email không hợp lệ.");
        }
        if (account.getPassword() == null || account.getPassword().length() < MIN_PASSWORD_LENGTH) {
            throw new ValidationException("Mật khẩu phải có ít nhất " + MIN_PASSWORD_LENGTH + " ký tự.");
        }
        if (account.getRole() == null || (!account.getRole().equals(Role.ADMIN.getValue()) && !account.getRole().equals(Role.STUDENT.getValue()))) {
            throw new ValidationException("Vai trò phải là ADMIN hoặc STUDENT.");
        }
    }

    public static void validateEmail(String email) throws ValidationException{
        if (email == null || !Pattern.matches(EMAIL_REGEX, email)) {
            throw new ValidationException("Email không hợp lệ.");
        }
    }

    public static void validateLogin(String email, String password) throws ValidationException {
        if (email == null || !Pattern.matches(EMAIL_REGEX, email)) {
            PrintError.println("Email không hợp lệ.");
        }
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            PrintError.println("Mật khẩu phải có ít nhất " + MIN_PASSWORD_LENGTH + " ký tự.");
        }
    }

    public static void validateChangePassword(String email, String oldPassword, String newPassword) throws ValidationException {
        if (email == null || !Pattern.matches(EMAIL_REGEX, email)) {
            PrintError.println("Email không hợp lệ.");
        }
        if (oldPassword == null || oldPassword.length() < MIN_PASSWORD_LENGTH) {
            PrintError.println("Mật khẩu cũ không hợp lệ.");
        }
        if (newPassword == null || newPassword.length() < MIN_PASSWORD_LENGTH) {
            PrintError.println("Mật khẩu mới phải có ít nhất " + MIN_PASSWORD_LENGTH + " ký tự.");
        }
    }
}
