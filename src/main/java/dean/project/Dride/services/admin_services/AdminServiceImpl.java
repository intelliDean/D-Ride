package dean.project.Dride.services.admin_services;

import dean.project.Dride.data.dto.request.EmailNotificationRequest;
import dean.project.Dride.data.dto.request.InviteAdminRequest;
import dean.project.Dride.data.dto.request.Recipient;
import dean.project.Dride.data.dto.response.ApiResponse;
import dean.project.Dride.data.models.Admin;
import dean.project.Dride.data.repositories.AdminRepository;
import dean.project.Dride.exceptions.DrideException;
import dean.project.Dride.notification.MailService;
import dean.project.Dride.utilities.DrideUtilities;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final MailService mailService;

    @Override
    public ApiResponse sendInviteRequests(Set<InviteAdminRequest> inviteAdminRequest) {
        EmailNotificationRequest request = new EmailNotificationRequest();
        List<Recipient> recipients = inviteAdminRequest.stream()
                .map(adminRequest -> createAdminProfile(adminRequest))      //initialise the admin entity
                .map(adminEntity -> new Recipient(adminEntity.getDetails().getName(), adminEntity.getDetails().getEmail())).toList();       //initialise the new recipient entity
        request.getTo().addAll(recipients);

        String adminMail = DrideUtilities.getAdminMailTemplate();
        request.setHtmlContent(String.format(adminMail,"Admin", DrideUtilities.getAdminMailTemplate()));
        String response = mailService.sendHtmlMail(request);
        if (response != null) {
            ApiResponse.builder()
                    .message("Admin Invitation Mail sent successfully")
                    .status(HttpStatus.OK.value())
                    .build();
        }
        throw new DrideException("Admin Invitation Mail sending failed");
    }


    private Admin createAdminProfile(InviteAdminRequest inviteAdmin) {
        Admin admin = new Admin();
        admin.getDetails().setName(inviteAdmin.getName());
        admin.getDetails().setEmail(inviteAdmin.getEmail());
        //mapped inviteAdminRequest to Admin model and save in the db
        return adminRepository.save(admin);
    }
}
