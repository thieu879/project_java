package ra.edu.business.dao.account;

import ra.edu.business.config.ConnectionDB;
import ra.edu.exception.DatabaseException;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

public class AccountDAOImp implements IAccountDAO {
    @Override
    public int registerAccount(String email, String password, String role) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL register_account(?,?,?,?)}");
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, role);
            stmt.registerOutParameter(4, Types.INTEGER);
            stmt.execute();
            conn.commit();
            return stmt.getInt(4);
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi đăng ký tài khoản: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public int loginAccount(String email, String password, String[] userRole, String[] userEmail) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL login_account(?,?,?,?)}");
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.registerOutParameter(3, Types.INTEGER);
            stmt.registerOutParameter(4, Types.VARCHAR);
            stmt.execute();
            conn.commit();
            userRole[0] = stmt.getString(4);
            userEmail[0] = email; // Lấy email từ input thay vì tham số đầu ra
            return stmt.getInt(3);
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi đăng nhập: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public int logoutAccount(String email) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL logout_account(?,?)}");
            stmt.setString(1, email);
            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.execute();
            conn.commit();
            return stmt.getInt(2);
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi đăng xuất: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }

    @Override
    public int changeStudentPassword(String email, String oldPassword, String newPassword) throws DatabaseException {
        Connection conn = null;
        CallableStatement stmt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL change_student_password(?,?,?,?)}");
            stmt.setString(1, email);
            stmt.setString(2, oldPassword);
            stmt.setString(3, newPassword);
            stmt.registerOutParameter(4, Types.INTEGER);
            stmt.execute();
            conn.commit();
            return stmt.getInt(4);
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException("Lỗi rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Lỗi khi đổi mật khẩu: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }
}
