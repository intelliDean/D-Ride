package dean.project.Dride.dride_mappers;

import dean.project.Dride.data.dto.request.PassengerDto;
import dean.project.Dride.data.models.Details;
import dean.project.Dride.data.dto.request.RegisterPassengerRequest;
import dean.project.Dride.data.models.Passenger;

import java.time.LocalDateTime;

public class DrideMappers {
    public static Details mapToDetails(RegisterPassengerRequest request) {
        Details details = new Details();

        details.setName(request.getName());
        details.setPassword(request.getPassword());
        details.setEmail(request.getEmail());
        details.setRegisteredAt(LocalDateTime.now().toString());
        return details;
    }
    public static PassengerDto mapPassengerToDto(Passenger passenger) {
        PassengerDto passengerDto = new PassengerDto();

        passengerDto.setId(passenger.getId());
        passengerDto.setDetails(passenger.getDetails());
        passengerDto.setGender(passenger.getGender());
        passengerDto.setPhoneNumber(passenger.getPhoneNumber());

        return passengerDto;
    }
}
