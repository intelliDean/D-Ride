package dean.project.Dride.services.admin_services;

import dean.project.Dride.data.dto.request.InviteAdminRequest;
import dean.project.Dride.data.dto.response.ApiResponse;

import java.util.Set;

public interface AdminService {
    ApiResponse sendInviteRequests(Set<InviteAdminRequest> inviteAdminRequestList);
}
