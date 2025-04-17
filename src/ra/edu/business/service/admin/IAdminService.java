package ra.edu.business.service.admin;

import ra.edu.business.model.Admin;

public interface IAdminService {
    Admin login(String username, String password) throws Exception;
}
