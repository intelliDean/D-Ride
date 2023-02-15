package dean.project.Dride.MyMapper;

import dean.project.Dride.data.dto.response.RegisterResponse;
import dean.project.Dride.data.models.Details;
import dean.project.Dride.data.dto.request.RegisterPassengerRequest;
import dean.project.Dride.data.models.Passenger;
import dean.project.Dride.data.repositories.PassengerRepo;

public class MyMapper {
    public static Details mapDetails(RegisterPassengerRequest request) {
        Details details = new Details();
        details.setName(request.getName());
        details.setPassword(request.getPassword());
        details.setEmail(request.getEmail());
        return details;
    }
}
