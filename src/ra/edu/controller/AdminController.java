package ra.edu.controller;

import ra.edu.business.model.Admin;
import ra.edu.business.service.admin.AdminServiceImp;
import ra.edu.business.service.admin.IAdminService;

public class AdminController {
    private final IAdminService adminService = new AdminServiceImp();

    public Admin handleLogin(String username, String password) {
        try {
            return adminService.login(username, password);
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            return null;
        }
    }
}
