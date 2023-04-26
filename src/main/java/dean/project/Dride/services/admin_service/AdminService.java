package dean.project.Dride.services.admin_service;

import dean.project.Dride.data.dto.response.AdminDTO;
import dean.project.Dride.utilities.Paginate;
import dean.project.Dride.data.dto.request.AdminDetailsRequest;
import dean.project.Dride.data.dto.request.InviteAdminRequest;
import dean.project.Dride.data.dto.response.GlobalApiResponse;
import dean.project.Dride.data.models.Admin;

import java.util.Optional;

public interface AdminService {
    GlobalApiResponse sendInviteRequests(InviteAdminRequest invitation);
    AdminDTO getAdminByEmail(String email);
    Optional<Admin> getAdminByUserId(Long userId);
    AdminDTO adminDetails(AdminDetailsRequest adminDetails);
    AdminDTO getAdminById(Long adminId);
    void saveAdmin(Admin admin);
    Paginate<AdminDTO> getAllAdmins(int pageNumber);
}
