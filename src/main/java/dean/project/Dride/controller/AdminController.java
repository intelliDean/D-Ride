package dean.project.Dride.controller;

import dean.project.Dride.data.dto.request.AdminDetailsRequest;
import dean.project.Dride.data.dto.request.InviteAdminRequest;
import dean.project.Dride.data.dto.response.ApiResponse;
import dean.project.Dride.data.models.Admin;
import dean.project.Dride.services.admin_service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    public final AdminService adminService;

    @PostMapping
    @Operation(summary = "To send invitation email to an admin")
    public ResponseEntity<ApiResponse> sendAdminInvitation(
            @Parameter(name = "invitationRequest", description = "A DTO with admin name and email", required = true)
            @RequestBody InviteAdminRequest invitationRequest) {
        ApiResponse response = adminService.sendInviteRequests(invitationRequest);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/details")
    public ResponseEntity<Admin> adminDetails(@RequestBody AdminDetailsRequest request) {
        Admin  admin = adminService.adminDetails(request);
        return ResponseEntity.ok(admin);
    }
    @GetMapping("getAdmin")
    public ResponseEntity<Admin> getAdminByMail(@RequestBody String email) {
      Admin  admin = adminService.getAdminByEmail(email);
        return ResponseEntity.ok(admin);
    }

    @GetMapping()
    public ResponseEntity<Admin> getAdminById(@RequestParam Long adminId) {
        Admin admin = adminService.getAdminById(adminId);
        return ResponseEntity.ok(admin);
    }
}
