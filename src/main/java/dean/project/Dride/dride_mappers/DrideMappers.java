package dean.project.Dride.dride_mappers;

import dean.project.Dride.data.dto.request.RegisterPassengerRequest;
import dean.project.Dride.data.models.Users;

import java.time.LocalDateTime;

public class DrideMappers {
    public static Users mapToDetails(RegisterPassengerRequest request) {
//        UserRepository users = new UserRepository();
        return Users.builder()
                .name(request.getName())
                .password(request.getPassword())
                .email(request.getEmail())
                .createdAt(LocalDateTime.now().toString())
                .build();
    }






}
