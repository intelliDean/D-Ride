package dean.project.Dride.services.user_service.utility;


import dean.project.Dride.data.dto.request.CreateUser;
import dean.project.Dride.data.models.User;

public interface UtilityUser {
    User currentUser ();
    User createUser(CreateUser createUser);
    int calculateAge(String dateOfBirth);
}
