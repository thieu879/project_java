package ra.edu.business.service.account;

import ra.edu.exception.DatabaseException;
import ra.edu.exception.ValidationException;

public interface IAccountService {
    void registerAccount(String email, String password, String role) throws ValidationException, DatabaseException;
    int loginAccount(String email, String password, String[] userRole, String[] userEmail) throws ValidationException, DatabaseException;
    void logoutAccount(String email) throws DatabaseException;
    void changeStudentPassword(String email, String oldPassword, String newPassword) throws ValidationException, DatabaseException;
}
