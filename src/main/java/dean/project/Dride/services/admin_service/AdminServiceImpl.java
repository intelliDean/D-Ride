package dean.project.Dride.services.admin_service;

import dean.project.Dride.utilities.Paginate;
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
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDateTime;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Optional;

import static dean.project.Dride.data.models.Role.ADMINISTRATOR;
import static dean.project.Dride.utilities.DrideUtilities.ADMIN_SUBJECT;
import static dean.project.Dride.utilities.DrideUtilities.NUMBER_OF_ITEMS_PER_PAGE;


@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final MailService mailService;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse sendInviteRequests(InviteAdminRequest invitation) {

        EmailNotificationRequest request = new EmailNotificationRequest();
        Admin admin = createAdminProfile(invitation);
        Recipient recipient = createRecipient(admin);

        request.setSubject(ADMIN_SUBJECT);
        request.getTo().add(recipient);
        Long userId = admin.getUser().getId();
        String adminName = admin.getUser().getName();

        String adminMail = DrideUtilities.getAdminMailTemplate();
        request.setHtmlContent(String.format(adminMail, adminName, DrideUtilities.generateVerificationLink(userId)));
        var response = mailService.sendHTMLMail(request);
        if (response != null) {
            return ApiResponse.builder()
                    .message("invite requests sent to admin with id: "+admin.getId())
                    .build();
        }
        throw new DrideException("Invitation request sending failed");
    }
    @Override
    public Admin adminDetails(AdminDetailsRequest adminDetails) {
        Admin admin = getAdminById(adminDetails.getAdminId());
        admin.setEmployeeId(generateEmployeeId(admin));

        User user = admin.getUser();
        user.setRoles(new HashSet<>());
        user.getRoles().add(ADMINISTRATOR);
        user.setPassword(encoder.encode(adminDetails.getPassword()));

        return adminRepository.save(admin);
    }

    private String generateEmployeeId(Admin admin) {
        StringBuilder builder = new StringBuilder();
        String[] first = admin.getUser().getName().split(" ");
        for (String s : first) {
            builder.append(s.charAt(0));
        }
        String init = builder.toString().toUpperCase();
        String adminId = String.valueOf(admin.getId());
        String userId = String.valueOf(admin.getUser().getId());
        return String.format("%s%s%s", init, adminId, userId);
    }

    @Override
    public Admin getAdminById(Long adminId) {
        return adminRepository.findById(adminId)
                .orElseThrow(()-> new UserNotFoundException("Admin could not be found"));
    }

    @Override
    public void saveAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    @Override
    public Paginate<Admin> getAllAdmins(int pageNumber) {
        if (pageNumber < 0) pageNumber = 0;
        else pageNumber -= 1;

        Pageable pageable = PageRequest.of(pageNumber, NUMBER_OF_ITEMS_PER_PAGE);
        Page<Admin> admin = adminRepository.findAll(pageable);
         Type type = new TypeToken<Paginate<Admin>>(){}.getType();
        return modelMapper.map(admin, type);
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
                .createdAt(LocalDateTime.now().toString())
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
