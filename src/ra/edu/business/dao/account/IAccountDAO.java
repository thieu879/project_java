package ra.edu.business.dao.account;

import ra.edu.exception.DatabaseException;

public interface IAccountDAO {
    int registerAccount(String email, String password, String role) throws DatabaseException;
    int loginAccount(String email, String password, String[] userRole, String[] userEmail) throws DatabaseException;
    int logoutAccount(String email) throws DatabaseException;
    int changeStudentPassword(String email, String oldPassword, String newPassword) throws DatabaseException;
}