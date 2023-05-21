package dean.project.Dride.services.user_service;

import dean.project.Dride.data.dto.request.CreateUser;
import dean.project.Dride.data.dto.request.LoginRequest;
import dean.project.Dride.data.dto.response.entity_dtos.UserDTO;
import dean.project.Dride.utilities.Paginate;
import dean.project.Dride.data.dto.response.api_response.GlobalApiResponse;
import dean.project.Dride.data.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UserService {
    User createUser(CreateUser create);
    GlobalApiResponse uploadProfileImage(MultipartFile profileImage, Long appUserId);
    GlobalApiResponse verifyAccount(Long userId, String token);
    UserDTO getByEmail(String email);
    User getInnerUserByEmail(String email);
    UserDTO getByUserId(Long userId);
    Paginate<UserDTO> getAllUsers(int pageNumber);
}