package dean.project.Dride.services.user_service;

import dean.project.Dride.data.dto.response.entity_dtos.UserDTO;
import dean.project.Dride.utilities.Paginate;
import dean.project.Dride.data.dto.response.api_response.GlobalApiResponse;
import dean.project.Dride.data.models.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    GlobalApiResponse uploadProfileImage(MultipartFile profileImage, Long appUserId);
    GlobalApiResponse verifyAccount(Long userId, String token);
    String CurrentAppUser();
    UserDTO getByEmail(String email);
    User getInnerUserByEmail(String email);
    UserDTO getByUserId(Long userId);
    Paginate<UserDTO> getAllUsers(int pageNumber);
}
