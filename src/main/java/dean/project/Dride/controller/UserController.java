package dean.project.Dride.controller;

import dean.project.Dride.data.dto.response.ApiResponse;
import dean.project.Dride.exceptions.DrideException;
import dean.project.Dride.services.user_services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/upload/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProfileImage(@PathVariable Long userId, @RequestParam(value = "file") MultipartFile file) {
        try {
            ApiResponse response = userService.uploadProfileImage(userId, file);
            return ResponseEntity.ok(response);
        } catch (DrideException ex) {
            return ResponseEntity.badRequest().body(
                            ApiResponse.builder()
                                    .message(ex.getMessage())
                                    .build());
        }
    }
    @PostMapping("/verify")
    public ResponseEntity<?> verifyAccount(@RequestParam Long userId, @RequestParam String token) {
        try {
            ApiResponse response = userService.verifyAccount(userId, token);
            return ResponseEntity.ok(response);
        } catch (DrideException ex) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .message(ex.getMessage())
                            .build());
        }
    }
}
