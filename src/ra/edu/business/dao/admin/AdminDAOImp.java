package ra.edu.business.dao.admin;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.Admin;
import ra.edu.exception.DatabaseException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAOImp implements AdminDAO {
    @Override
    public Admin login(String username, String password) throws Exception {
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareCall("{CALL log_in_admin(?,?,?)}");
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.registerOutParameter(3, java.sql.Types.INTEGER);
            stmt.execute();
            int returnCode = stmt.getInt(3);
            if (returnCode == 1) {
                rs = stmt.getResultSet();
                if (rs.next()) {
                    Admin admin = new Admin();
                    admin.setId(rs.getInt("id"));
                    admin.setUsername(username);
                    admin.setPassword(password);
                    conn.commit();
                    return admin;
                }
            }
            conn.rollback();
            return null;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw new DatabaseException("Lỗi khi đăng nhập admin: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, stmt);
        }
    }
}
