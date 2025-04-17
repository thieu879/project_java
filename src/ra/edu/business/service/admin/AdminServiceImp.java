package ra.edu.business.service.admin;

import ra.edu.business.dao.admin.AdminDAO;
import ra.edu.business.dao.admin.AdminDAOImp;
import ra.edu.business.model.Admin;
import ra.edu.exception.InvalidInputException;

public class AdminServiceImp implements IAdminService {
    private final AdminDAO adminDAO = new AdminDAOImp();

    @Override
    public Admin login(String username, String password) throws Exception {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new InvalidInputException("Tên đăng nhập và mật khẩu không được để trống.");
        }
        Admin admin = adminDAO.login(username, password);
        if (admin == null) {
            throw new InvalidInputException("Đăng nhập thất bại. Tên đăng nhập hoặc mật khẩu không đúng.");
        }
        return admin;
    }
}
