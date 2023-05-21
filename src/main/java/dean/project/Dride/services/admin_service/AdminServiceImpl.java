package dean.project.Dride.services.admin_service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
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
import dean.project.Dride.services.user_service.CurrentUserService;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static dean.project.Dride.data.models.Role.ADMINISTRATOR;
import static dean.project.Dride.exceptions.ExceptionMessage.EMAIL_EXCEPTION;
import static dean.project.Dride.exceptions.ExceptionMessage.UPDATE_FAILED;
import static dean.project.Dride.utilities.Constants.*;


@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final CurrentUserService currentUserService;
    private final AdminRepository adminRepository;
    private final MailService mailService;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final GlobalApiResponse.GlobalApiResponseBuilder globalResponse;

    @Override
    public GlobalApiResponse sendInviteRequests(InviteAdminRequest invitation) {

        EmailNotificationRequest request = new EmailNotificationRequest();
        Admin admin = createAdminProfile(invitation);
        Recipient recipient = createRecipient(admin);

        request.setSubject("Admin Invitation");
        request.getTo().add(recipient);
        Long userId = admin.getUser().getId();
        String adminName = admin.getUser().getName();

        String adminMail = DrideUtilities.getAdminMailTemplate();
        String link = DrideUtilities.generateVerificationLink(userId);
        request.setHtmlContent(String.format(adminMail, adminName, link));

        var response = mailService.sendHTMLMail(request);
        if (response == null) throw new DrideException(EMAIL_EXCEPTION);

        return globalResponse
                .message(String.format("Invitation mail sent to %s", admin.getUser().getName()))
                .build();
    }
    private Admin currentAdmin() {
        return getAdminByUserId(currentUserService.currentUser().getId())
                .orElseThrow(UserNotFoundException::new);
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
        String[] splitNames = admin.getUser().getName().split(" ");
        Arrays.stream(splitNames).forEach(eachName -> builder.append(eachName.charAt(0)));

        String toUppercase = builder.toString().toUpperCase();
        String adminId = String.valueOf(admin.getId());
        String userId = String.valueOf(admin.getUser().getId());
        return String.format("%s-0%s-0%s", toUppercase, adminId, userId);
    }

    @Override
    public AdminDTO getCurrentAdmin() {
        return modelMapper.map(
                currentAdmin(),
                AdminDTO.class);
    }
    @Override
    public void saveAdmin(Admin admin) {
        adminRepository.save(admin);
    }
    @Override
    public Paginate<AdminDTO> getAllAdmins(int pageNumber) {
        int page = pageNumber < 1 ? 0 : pageNumber - 1;
        Pageable pageable = PageRequest.of(page, NUMBER_OF_ITEMS_PER_PAGE);
        Page<Admin> admin = adminRepository.findAll(pageable);
        Type type = new TypeToken<Paginate<AdminDTO>>() {
        }.getType();
        return modelMapper.map(admin, type);
    }

    @Override
    public GlobalApiResponse updateAdmin(JsonPatch jsonPatch) {
        Admin admin = currentAdmin();
        JsonNode node = objectMapper.convertValue(admin, JsonNode.class);
        try {
            JsonNode updatedNode = jsonPatch.apply(node);
            Admin updatedAdmin = objectMapper.convertValue(updatedNode, Admin.class);
            adminRepository.save(updatedAdmin);
            return globalResponse
                    .message(SUCCESS)
                    .build();
        } catch (JsonPatchException e) {
            throw new DrideException(UPDATE_FAILED);
        }
    }

    @Override
    public AdminDTO getAdminByEmail(String email) {
        return modelMapper.map(adminRepository.findByUserEmail(email)
                .orElseThrow(UserNotFoundException::new), AdminDTO.class);
    }

    @Override
    public Optional<Admin> getAdminByUserId(Long adminId) {
        return adminRepository.findAdminByUser_Id(adminId);
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
