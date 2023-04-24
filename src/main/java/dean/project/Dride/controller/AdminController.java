package dean.project.Dride.controller;

import dean.project.Dride.data.dto.request.AdminDetailsRequest;
import dean.project.Dride.data.dto.request.InviteAdminRequest;
import dean.project.Dride.data.dto.response.ApiResponse;
import dean.project.Dride.data.models.Admin;
import dean.project.Dride.services.admin_service.AdminService;
import dean.project.Dride.services.notification.MailService;
import dean.project.Dride.utilities.Paginate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    public final AdminService adminService;
    private final MailService mailService;

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
        Admin admin = adminService.adminDetails(request);
        return ResponseEntity.ok(admin);
    }

    @GetMapping("getAdmin")
//    @Secured(value ="ADMINISTRATOR")
    public ResponseEntity<Admin> getAdminByMail(@RequestBody String email) {
        Admin admin = adminService.getAdminByEmail(email);
        return ResponseEntity.ok(admin);
    }

    @GetMapping()
    //Secured(value ="ADMINISTRATOR")
    public ResponseEntity<Admin> getAdminById(@RequestParam Long adminId) {
        Admin admin = adminService.getAdminById(adminId);
        return ResponseEntity.ok(admin);
    }

    @GetMapping("/all")
    //@Secured(value ="ADMINISTRATOR")
    public ResponseEntity<Paginate<Admin>> getAllAdmins(@RequestParam int pageNumber) {
        Paginate<Admin> admin = adminService.getAllAdmins(pageNumber);
        return ResponseEntity.ok(admin);
    }

    @GetMapping("/name/{userId}")
    public ResponseEntity<?> getUserName(@PathVariable Long userId) {
        String name = mailService.getName(userId);
        return ResponseEntity.ok(name);
    }
}
