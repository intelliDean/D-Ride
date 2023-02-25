package dean.project.Dride.services.admin_services;

import dean.project.Dride.data.dto.request.InviteAdminRequest;
import dean.project.Dride.data.dto.response.ApiResponse;
import dean.project.Dride.data.repositories.AdminRepository;
import dean.project.Dride.notification.MailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@AllArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final MailService mailService;

    @Override
    public ApiResponse sendInviteRequests(Set<InviteAdminRequest> inviteAdminRequestList) {
        return null;
    }
}
