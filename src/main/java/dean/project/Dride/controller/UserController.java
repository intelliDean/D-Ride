package dean.project.Dride.controller;


import dean.project.Dride.data.dto.response.api_response.GlobalApiResponse;
import dean.project.Dride.data.dto.response.entity_dtos.UserDTO;
import dean.project.Dride.data.models.User;
import dean.project.Dride.exceptions.DrideException;
import dean.project.Dride.services.notification.MailService;
import dean.project.Dride.services.user_service.user.UserService;
import dean.project.Dride.utilities.Paginate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final MailService mailService;
    private final GlobalApiResponse.GlobalApiResponseBuilder globalResponse;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload User Profile Picture")
    public ResponseEntity<GlobalApiResponse> uploadProfileImage(
            @Parameter(
                    name = "file",
                    description = "Image file to upload",
                    required = true
            )
            @RequestParam MultipartFile file) {
        GlobalApiResponse response = userService.uploadProfileImage(file);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/account/verify")
    @Operation(summary = "To verify user before enabling their account")
    public ResponseEntity<GlobalApiResponse> verifyAccount(
            @Parameter(
                    name = "userId",
                    description = "ID of the user to verify",
                    required = true
            )
            @RequestParam Long userId,
            @Parameter(
                    name = "token",
                    description = "The token sent to the user via email after registration",
                    required = true
            )
            @RequestParam String token
    ) {
        try {
            var response = userService.verifyAccount(userId, token);
            return ResponseEntity.ok(response);
        } catch (DrideException exception) {
            return ResponseEntity.badRequest().body(
                    globalResponse
                            .message(exception.getMessage())
                            .build()
            );
        }
    }
     @GetMapping("current")
    @Operation(summary = "Current User")
    public ResponseEntity<User> currentUser() {
        return ResponseEntity.ok(userService.currentUser());
    }

    @GetMapping("{userId}")
    @Operation(summary = "Get User By ID")
    //@Secured(value = {"ADMINISTRATOR", "PASSENGER", "DRIVER"})
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(
                    name = "userId",
                    description = "Fetch user with this ID",
                    required = true
            )
            @PathVariable Long userId
    ) {
        UserDTO user = userService.getByUserId(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("mail")
    @Operation(summary = "Get User By Email")
    public ResponseEntity<UserDTO> getUserByEmail(
            @Parameter(
                    name = "email",
                    description = "Fetch the user with email",
                    required = true
            )
            @RequestParam String email
    ) {
        UserDTO user = userService.getByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @Operation(summary = "Get All Users")
    //@Secured(value ="ADMINISTRATOR")
    public ResponseEntity<Paginate<UserDTO>> getAllUsers(
            @Parameter(
                    name = "pageNumber",
                    description = "Page to view"
            )
            @RequestParam int pageNumber
    ) {
        Paginate<UserDTO> users = userService.getAllUsers(pageNumber);
        return ResponseEntity.ok(users);
    }
}
