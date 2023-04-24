package dean.project.Dride.services.admin_service;

import dean.project.Dride.utilities.Paginate;
import dean.project.Dride.data.dto.request.AdminDetailsRequest;
import dean.project.Dride.data.dto.request.InviteAdminRequest;
import dean.project.Dride.data.dto.response.ApiResponse;
import dean.project.Dride.data.models.Admin;

import java.util.Optional;

public interface AdminService {
    ApiResponse sendInviteRequests(InviteAdminRequest invitation);
    Admin getAdminByEmail(String email);
    Optional<Admin> getAdminByUserId(Long userId);
    Admin adminDetails(AdminDetailsRequest adminDetails);
    Admin getAdminById(Long adminId);
    void saveAdmin(Admin admin);
    Paginate<Admin> getAllAdmins(int pageNumber);
}
