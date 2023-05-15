package dean.project.Dride.controller;

import dean.project.Dride.data.dto.request.AdminDetailsRequest;
import dean.project.Dride.data.dto.request.InviteAdminRequest;
import dean.project.Dride.data.dto.response.entity_dtos.AdminDTO;
import dean.project.Dride.data.dto.response.api_response.GlobalApiResponse;
import dean.project.Dride.services.admin_service.AdminService;
import dean.project.Dride.services.notification.MailService;
import dean.project.Dride.utilities.Paginate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dean.project.Dride.utilities.AdminUrls.*;

@AllArgsConstructor
@RestController
@RequestMapping(ADMIN_BASE_URL)
public class AdminController {
    public final AdminService adminService;
    private final MailService mailService;

    @PostMapping
    @Operation(summary = "To send invitation email to an admin")
    public ResponseEntity<GlobalApiResponse> sendAdminInvitation(
            @Parameter(name = "invitationRequest", description = "A DTO with admin name and email", required = true)
            @RequestBody InviteAdminRequest invitationRequest) {
        GlobalApiResponse response = adminService.sendInviteRequests(invitationRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping(ADMIN_DETAILS)
    public ResponseEntity<AdminDTO> adminDetails(@RequestBody AdminDetailsRequest request) {
        AdminDTO admin = adminService.adminDetails(request);
        return ResponseEntity.ok(admin);
    }

    @GetMapping(GET_ADMIN)
//    @Secured(value ="ADMINISTRATOR")
    public ResponseEntity<AdminDTO> getAdminByMail(@RequestParam String email) {
        AdminDTO admin = adminService.getAdminByEmail(email);
        return ResponseEntity.ok(admin);
    }

    @GetMapping(GET)
    public ResponseEntity<AdminDTO> getAdminById(@RequestParam Long adminId) {
        AdminDTO admin = adminService.getAdminById(adminId);
        return ResponseEntity.ok(admin);
    }

    @GetMapping(ALL_ADMINS)
    //@Secured(value ="ADMINISTRATOR")
    public ResponseEntity<Paginate<AdminDTO>> getAllAdmins(@RequestParam int pageNumber) {
        Paginate<AdminDTO> admin = adminService.getAllAdmins(pageNumber);
        return ResponseEntity.ok(admin);
    }
}
