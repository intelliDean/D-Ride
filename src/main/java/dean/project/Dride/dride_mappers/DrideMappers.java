package dean.project.Dride.dride_mappers;

import dean.project.Dride.data.dto.entitydtos.DriverDto;
import dean.project.Dride.data.dto.entitydtos.PassengerDto;
import dean.project.Dride.data.dto.request.UserRegisterRequest;
import dean.project.Dride.data.models.Details;
import dean.project.Dride.data.models.Driver;
import dean.project.Dride.data.models.Passenger;

import java.time.LocalDateTime;

public class DrideMappers {
    public static Details mapToDetails(UserRegisterRequest request) {
//        DetailsRepository details = new DetailsRepository();
        return Details.builder()
                .name(request.getName())
                .password(request.getPassword())
                .email(request.getEmail())
                .registeredAt(LocalDateTime.now().toString())
                .build();
    }

    public static PassengerDto mapPassengerToDto(Passenger passenger) {
        return PassengerDto.builder()
                .id(passenger.getId())
                .details(passenger.getDetails())
                .gender(passenger.getGender())
                .phoneNumber(passenger.getPhoneNumber())
                .build();
    }





}
