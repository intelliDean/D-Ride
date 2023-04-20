package dean.project.Dride.services.user_service;

import dean.project.Dride.config.app.Paginate;
import dean.project.Dride.data.dto.response.ApiResponse;
import dean.project.Dride.data.models.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    ApiResponse uploadProfileImage(MultipartFile profileImage, Long appUserId);

    ApiResponse verifyAccount(Long userId, String token);

    String CurrentAppUser();

    User getByEmail(String email);
    User getByUserId(Long userId);
    Paginate<User> getAllUsers(int pageNumber);


}
