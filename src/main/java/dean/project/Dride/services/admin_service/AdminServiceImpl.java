package dean.project.Dride.services.admin_service;

import dean.project.Dride.data.dto.request.AdminDetailsRequest;
import dean.project.Dride.data.dto.request.EmailNotificationRequest;
import dean.project.Dride.data.dto.request.InviteAdminRequest;
import dean.project.Dride.data.dto.request.Recipient;
import dean.project.Dride.data.dto.response.api_response.GlobalApiResponse;
import dean.project.Dride.data.dto.response.entity_dtos.AdminDTO;
import dean.project.Dride.data.models.Admin;
import dean.project.Dride.data.models.User;
import dean.project.Dride.data.repositories.AdminRepository;
import dean.project.Dride.exceptions.DrideException;
import dean.project.Dride.exceptions.UserNotFoundException;
import dean.project.Dride.services.notification.MailService;
import dean.project.Dride.utilities.DrideUtilities;
import dean.project.Dride.utilities.Paginate;
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
import static dean.project.Dride.exceptions.ExceptionMessage.EMAIL_EXCEPTION;
import static dean.project.Dride.utilities.Constants.*;


@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final MailService mailService;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;
    private final GlobalApiResponse.GlobalApiResponseBuilder globalResponse;

    @Override
    public GlobalApiResponse sendInviteRequests(InviteAdminRequest invitation) {

        EmailNotificationRequest request = new EmailNotificationRequest();
        Admin admin = createAdminProfile(invitation);
        Recipient recipient = createRecipient(admin);

        request.setSubject(ADMIN_SUBJECT);
        request.getTo().add(recipient);
        Long userId = admin.getUser().getId();
        String adminName = admin.getUser().getName();

        String adminMail = DrideUtilities.getAdminMailTemplate();
        String link = DrideUtilities.generateVerificationLink(userId);
        request.setHtmlContent(String.format(adminMail, adminName, link));

        var response = mailService.sendHTMLMail(request);
        if (response != null) {
            return globalResponse
                    .message(String.format(ADMIN_IV, admin.getId()))
                    .build();
        }
        throw new DrideException(EMAIL_EXCEPTION);
    }

    @Override
    public AdminDTO adminDetails(AdminDetailsRequest adminDetails) {
        Admin admin = adminRepository.findById(adminDetails.getAdminId())
                .orElseThrow(UserNotFoundException::new);
        admin.setEmployeeId(generateEmployeeId(admin));

        User user = admin.getUser();
        user.setRoles(new HashSet<>());
        user.getRoles().add(ADMINISTRATOR);
        user.setPassword(encoder.encode(adminDetails.getPassword()));

        admin = adminRepository.save(admin);
        return modelMapper.map(admin, AdminDTO.class);
    }

    private String generateEmployeeId(Admin admin) {
        StringBuilder builder = new StringBuilder();
        String[] first = admin.getUser().getName().split(REGX);
        for (String s : first) {
            builder.append(s.charAt(0));
        }
        String init = builder.toString().toUpperCase();
        String adminId = String.valueOf(admin.getId());
        String userId = String.valueOf(admin.getUser().getId());
        return String.format(EMP_ID, init, adminId, userId);
    }

    @Override
    public AdminDTO getAdminById(Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(UserNotFoundException::new);
        return modelMapper.map(admin, AdminDTO.class);
    }

    @Override
    public void saveAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    @Override
    public Paginate<AdminDTO> getAllAdmins(int pageNumber) {
        if (pageNumber < 0) pageNumber = 0;
        else pageNumber -= 1;

        Pageable pageable = PageRequest.of(pageNumber, NUMBER_OF_ITEMS_PER_PAGE);
        Page<Admin> admin = adminRepository.findAll(pageable);
        Type type = new TypeToken<Paginate<AdminDTO>>() {
        }.getType();
        return modelMapper.map(admin, type);
    }

    @Override
    public AdminDTO getAdminByEmail(String email) {
        Admin admin = adminRepository.findAdminByUser_Email(email)
                .orElseThrow(UserNotFoundException::new);
        return modelMapper.map(admin, AdminDTO.class);
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
