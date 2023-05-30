package dean.project.Dride.controller;

import com.github.fge.jsonpatch.JsonPatch;
import dean.project.Dride.data.dto.request.AdminDetailsRequest;
import dean.project.Dride.data.dto.request.InviteAdminRequest;
import dean.project.Dride.data.dto.response.api_response.GlobalApiResponse;
import dean.project.Dride.data.dto.response.entity_dtos.AdminDTO;
import dean.project.Dride.services.admin_service.AdminService;
import dean.project.Dride.services.notification.MailService;
import dean.project.Dride.utilities.Paginate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dean.project.Dride.utilities.Constants.ADMIN_BASE_URL;

@Tag(name = "Admin Controller")
@AllArgsConstructor
@RestController
@RequestMapping(ADMIN_BASE_URL)
public class AdminController {
    public final AdminService adminService;
    private final MailService mailService;

    @PostMapping
    @Operation(summary = "To send invitation email to an admin")
    public ResponseEntity<GlobalApiResponse> sendAdminInvitation(
            @Parameter(
                    name = "invitationRequest",
                    description = "DTO with user name and email",
                    required = true
            )
            @RequestBody InviteAdminRequest invitationRequest) {
        GlobalApiResponse response = adminService.sendInviteRequests(invitationRequest);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("update")
    @Operation(summary = "update user info in patches")
    public ResponseEntity<GlobalApiResponse> updateAdmin(
            @Parameter(
                    name = "jsonPatch",
                    description = "JsonPatch syntax",
                    required = true
            )
            @RequestBody JsonPatch jsonPatch) {
        GlobalApiResponse response = adminService.updateAdmin(jsonPatch);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/details")
    @Operation(summary = "admin adding password")
    public ResponseEntity<AdminDTO> adminDetails(
            @Parameter(
                    name = "request",
                    description = "DTO with user ID and Password",
                    required = true
            )
            @RequestBody AdminDetailsRequest request) {
        AdminDTO admin = adminService.adminDetails(request);
        return ResponseEntity.ok(admin);
    }

    @GetMapping("get")
    @Operation(summary = "current user")
    public ResponseEntity<AdminDTO> currentAdmin() {
        AdminDTO admin = adminService.getCurrentAdmin();
        return ResponseEntity.ok(admin);
    }

    @GetMapping("/getAdmin")
//    @Secured(value ="ADMINISTRATOR")
    @Operation(summary = "fetch admin by email")
    public ResponseEntity<AdminDTO> getAdminByMail(
            @Parameter(
                    name = "email",
                    description = "email to fetch admin",
                    required = true
            )
            @RequestParam String email) {
        AdminDTO admin = adminService.getAdminByEmail(email);
        return ResponseEntity.ok(admin);
    }

    @GetMapping("/all")
    //@Secured(value ="ADMINISTRATOR")
    @Operation(summary = "fetch all admins")
    public ResponseEntity<Paginate<AdminDTO>> getAllAdmins(
            @Parameter(
                    name = "pageNumber",
                    description = "page to view",
                    required = true
            )
            @RequestParam int pageNumber) {
        Paginate<AdminDTO> admin = adminService.getAllAdmins(pageNumber);
        return ResponseEntity.ok(admin);
    }
}
