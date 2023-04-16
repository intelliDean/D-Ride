package dean.project.Dride.services.admin_service;

import dean.project.Dride.data.dto.request.AdminDetailsRequest;
import dean.project.Dride.data.dto.request.EmailNotificationRequest;
import dean.project.Dride.data.dto.request.InviteAdminRequest;
import dean.project.Dride.data.dto.request.Recipient;
import dean.project.Dride.data.dto.response.ApiResponse;
import dean.project.Dride.data.models.Admin;
import dean.project.Dride.data.models.User;
import dean.project.Dride.data.repositories.AdminRepository;
import dean.project.Dride.exceptions.DrideException;
import dean.project.Dride.exceptions.UserNotFoundException;
import dean.project.Dride.services.notification.MailService;
import dean.project.Dride.utilities.DrideUtilities;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDateTime;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static dean.project.Dride.data.models.Role.ADMINISTRATOR;
import static dean.project.Dride.utilities.DrideUtilities.ADMIN_SUBJECT;


@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final MailService mailService;
    private final PasswordEncoder encoder;

    @Override
    public ApiResponse sendInviteRequests(InviteAdminRequest invitation) {
        EmailNotificationRequest request = new EmailNotificationRequest();
        Admin admin = createAdminProfile(invitation);
        Recipient recipient = createRecipient(admin);

        request.setSubject(ADMIN_SUBJECT);
        request.getTo().add(recipient);
        Long userId = admin.getId();
        String adminName = admin.getUser().getName();

        String adminMail = DrideUtilities.getAdminMailTemplate();
        request.setHtmlContent(String.format(adminMail, adminName, DrideUtilities.generateVerificationLink(userId)));
        var response = mailService.sendHtmlMail(request);
        if (response != null) {
            return ApiResponse.builder()
                    .message("invite requests sent")
                    .build();
        }
        throw new DrideException("Invitation request sending failed");
    }
    @Override
    public Admin adminDetails(AdminDetailsRequest adminDetails) {
        UUID uuid = new UUID(7L, 12L);
        Admin admin = getAdminById(adminDetails.getAdminId());
        admin.setEmployeeId(uuid.toString());

        User user = admin.getUser();
        user.setPassword(encoder.encode(adminDetails.getPassword()));
        user.setCreatedAt(LocalDateTime.now().toString());
        user.setRoles(new HashSet<>());
        user.getRoles().add(ADMINISTRATOR);

        return adminRepository.save(admin);
    }

    @Override
    public Admin getAdminById(Long adminId) {
        return adminRepository.findById(adminId)
                .orElseThrow(()-> new UserNotFoundException("Admin could not be found"));
    }

    @Override
    public Admin getAdminByEmail(String email) {
        return adminRepository.findAdminByUser_Email(email)
                .orElseThrow(() -> new UserNotFoundException("Admin not found"));
    }

    @Override
    public Optional<Admin> getAdminByUserId(Long userId) {
        return adminRepository.findAdminByUser_Id(userId);
    }
    private Admin createAdminProfile(InviteAdminRequest invitation) {
        User user = User.builder()
                .email(invitation.getEmail())
                .name(invitation.getName())
                .build();

        Admin admin = Admin.builder()
                .user(user)
                .build();
        return adminRepository.save(admin);
    }

    private Recipient createRecipient(Admin admin) {
        return Recipient.builder()
                .name(admin.getUser().getName())
                .email(admin.getUser().getEmail())
                .build();
    }
}
