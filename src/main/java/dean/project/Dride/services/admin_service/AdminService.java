package dean.project.Dride.services.admin_service;

import com.github.fge.jsonpatch.JsonPatch;
import dean.project.Dride.data.dto.request.AdminDetailsRequest;
import dean.project.Dride.data.dto.request.InviteAdminRequest;
import dean.project.Dride.data.dto.response.api_response.GlobalApiResponse;
import dean.project.Dride.data.dto.response.entity_dtos.AdminDTO;
import dean.project.Dride.data.models.Admin;
import dean.project.Dride.utilities.Paginate;

import java.util.Optional;

public interface AdminService {
    GlobalApiResponse sendInviteRequests(InviteAdminRequest invitation);
    AdminDTO getAdminByEmail(String email);
    Optional<Admin> getAdminByUserId(Long adminId);
    AdminDTO adminDetails(AdminDetailsRequest adminDetails);
    AdminDTO getCurrentAdmin();
    void saveAdmin(Admin admin);
    Paginate<AdminDTO> getAllAdmins(int pageNumber);
    GlobalApiResponse updateAdmin(JsonPatch jsonPatch);
}
