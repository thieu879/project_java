package ra.edu.business.dao.admin;

import ra.edu.business.model.Admin;

public interface AdminDAO {
    Admin login(String username, String password) throws Exception;
}