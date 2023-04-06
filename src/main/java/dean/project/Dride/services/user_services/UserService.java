package dean.project.Dride.services.user_services;

import dean.project.Dride.data.dto.response.ApiResponse;
import dean.project.Dride.data.models.Users;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    ApiResponse uploadProfileImage(Long userId, MultipartFile profileImage);
    ApiResponse verifyAccount(Long userId, String token);

    Users getByEmail(String email);
}
