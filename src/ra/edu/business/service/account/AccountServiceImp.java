package ra.edu.business.service.account;

import ra.edu.business.dao.account.AccountDAOImp;
import ra.edu.business.dao.account.IAccountDAO;
import ra.edu.exception.DatabaseException;
import ra.edu.exception.ValidationException;
import ra.edu.validate.AccountValidator;

public class AccountServiceImp implements IAccountService {
    private IAccountDAO accountDAO = new AccountDAOImp();

    @Override
    public void registerAccount(String email, String password, String role) throws ValidationException, DatabaseException {
        AccountValidator.validateLogin(email, password);
        int result = accountDAO.registerAccount(email, password, role);
        if (result == 0) {
            throw new DatabaseException("Tài khoản đã tồn tại.");
        } else if (result == -1) {
            throw new DatabaseException("Email đã được sử dụng.");
        }
    }

    @Override
    public int loginAccount(String email, String password, String[] userRole, String[] userEmail) throws ValidationException, DatabaseException {
        AccountValidator.validateLogin(email, password);
        int result = accountDAO.loginAccount(email, password, userRole, userEmail);
        if (result == 0) {
            throw new DatabaseException("Đăng nhập thất bại: Email hoặc mật khẩu không đúng.");
        }
        return result;
    }

    @Override
    public void logoutAccount(String email) throws DatabaseException {
        int result = accountDAO.logoutAccount(email);
        if (result == 0) {
            throw new DatabaseException("Tài khoản không tồn tại.");
        }
    }

    @Override
    public void changeStudentPassword(String email, String oldPassword, String newPassword) throws ValidationException, DatabaseException {
        AccountValidator.validateChangePassword(email, oldPassword, newPassword);
        int result = accountDAO.changeStudentPassword(email, oldPassword, newPassword);
        if (result == 0) {
            throw new DatabaseException("Tài khoản không tồn tại hoặc mật khẩu cũ không đúng.");
        }
    }
}
