package ra.edu.controller;

import ra.edu.business.service.account.AccountServiceImp;
import ra.edu.business.service.account.IAccountService;
import ra.edu.exception.DatabaseException;
import ra.edu.exception.ValidationException;
import ra.edu.utils.InputUtil;
import ra.edu.utils.Session;

public class AccountController {
    private IAccountService accountService = new AccountServiceImp();

    public void registerAccount() {
        try {
            String email = InputUtil.getNonEmptyString("Nhập email: ");
            String password = InputUtil.getNonEmptyString("Nhập mật khẩu: ");
            String role = InputUtil.getNonEmptyString("Nhập vai trò (ADMIN/STUDENT): ").toUpperCase();
            accountService.registerAccount(email, password, role);
            System.out.println("Đăng ký tài khoản thành công!");
        } catch (ValidationException | DatabaseException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public boolean loginAccount() {
        try {
            String email = InputUtil.getNonEmptyString("Nhập email: ");
            String password = InputUtil.getNonEmptyString("Nhập mật khẩu: ");
            String[] userRole = new String[1];
            String[] userEmail = new String[1];
            int result = accountService.loginAccount(email, password, userRole, userEmail);
            if (result > 0) {
                Session.login(userEmail[0], userRole[0], result);
                return true;
            } else if (result == -1 ) {
                System.err.println("Tài khoản đã bị khoá");
            }
            return false;
        } catch (ValidationException | DatabaseException e) {
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        }
    }

    public void logoutAccount() {
        try {
            if (Session.isLoggedIn()) {
                accountService.logoutAccount(Session.getUserEmail());
                Session.logout();
                System.out.println("Đăng xuất thành công!");
            } else {
                System.out.println("Chưa có tài khoản nào đang đăng nhập.");
            }
        } catch (DatabaseException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    public void changeStudentPassword() {
        try {
            if (!Session.isLoggedIn() || !Session.getUserRole().equals("STUDENT")) {
                throw new ValidationException("Chỉ học viên đang đăng nhập mới có thể đổi mật khẩu.");
            }
            String oldPassword = InputUtil.getNonEmptyString("Nhập mật khẩu cũ: ");
            String newPassword = InputUtil.getNonEmptyString("Nhập mật khẩu mới: ");
            accountService.changeStudentPassword(Session.getUserEmail(), oldPassword, newPassword);
            System.out.println("Đổi mật khẩu thành công!");
        } catch (ValidationException | DatabaseException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }
}
