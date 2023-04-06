package dean.project.Dride.services.passengerServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import dean.project.Dride.services.cloud_business.CloudService;
import dean.project.Dride.data.dto.request.RegisterPassengerRequest;
import dean.project.Dride.data.dto.response.*;
import dean.project.Dride.data.models.Passenger;
import dean.project.Dride.data.models.Users;
import dean.project.Dride.data.repositories.PassengerRepository;
import dean.project.Dride.exceptions.DrideException;
import dean.project.Dride.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

import static dean.project.Dride.data.models.Role.PASSENGER;
import static dean.project.Dride.exceptions.ExceptionMessage.PASSENGER_WITH_ID;
import static dean.project.Dride.exceptions.ExceptionMessage.PASSENGER_WITH_NAME;
import static dean.project.Dride.utilities.DrideUtilities.NUMBER_OF_ITEMS_PER_PAGE;

@Slf4j
@AllArgsConstructor
@Service
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;
    private final CloudService cloudService;

    private final PasswordEncoder passwordEncoder;
    //private final DistanceConfig directionConfig;


    @Override
    public RegisterResponse register(RegisterPassengerRequest registerPassengerRequest) {
        Users passengerDetails = mapDetails(registerPassengerRequest);
        passengerDetails.setRoles(new HashSet<>());
        passengerDetails.getRoles().add(PASSENGER);
        passengerDetails.setPassword(passwordEncoder.encode(registerPassengerRequest.getPassword()));


        Passenger passenger = Passenger.builder()
                .users(passengerDetails)
                .build();

        Passenger savedPassenger = passengerRepository.save(passenger);

        return getRegisterResponse(savedPassenger);
    }

    private static Users mapDetails(RegisterPassengerRequest registerPassengerRequest) {
        return Users.builder()
                .name(registerPassengerRequest.getName())
                .email(registerPassengerRequest.getEmail())
                .password(registerPassengerRequest.getPassword())
                .createdAt(LocalDateTime.now().toString())
                .build();
    }

    private static RegisterResponse getRegisterResponse(Passenger passenger) {
        return RegisterResponse.builder()
                .id(passenger.getId())
                .message("User Registration Successful")
                .isSuccess(true)
                .build();
    }

    @Override
    public Passenger getPassengerById(Long passengerId) {
        return passengerRepository.findById(passengerId)
                .orElseThrow(()->new UserNotFoundException(
                        String.format(PASSENGER_WITH_ID.getMessage(), passengerId)));
    }

    @Override
    public Optional<Passenger> getPassengerOp(Long passengerId) {
        return passengerRepository.findById(passengerId);
    }

    @Override
    public Passenger getPassengerByName(String passengerName) {
        return passengerRepository.findPassengerByUsers_Name(passengerName)
                .orElseThrow(()->new UserNotFoundException(
                        String.format(PASSENGER_WITH_NAME.getMessage(), passengerName)));
    }


    @Override
    public UserUpdateResponse updatePassengerInfo(Long passId, JsonPatch updatePayload) {
        ObjectMapper mapper = new ObjectMapper();
        Passenger passenger = getPassengerById(passId);

        // convert passenger object to node
        JsonNode node = mapper.convertValue(passenger, JsonNode.class);
        try {
            // apply patch
            JsonNode updatedNode = updatePayload.apply(node);
            // convert node back to passenger object
            var updatedPassenger = mapper.convertValue(updatedNode, Passenger.class);
            passengerRepository.save(updatedPassenger);


            return UserUpdateResponse.builder()
                    .message(String.format("Passenger with ID %d updated successfully", passenger.getId()))
                    .build();
        } catch (JsonPatchException ex) {
            throw new DrideException(ex.getMessage());
        }
    }

    @Override
    public void deletePassenger(Long id) {
        passengerRepository.deleteById(id);
    }

    @Override
    public void deleteAllPassengers() {
        passengerRepository.deleteAll();
    }

    @Override
    public Page<Passenger> getAllPassengers(int pageNumber) {
        if (pageNumber < 1) {
            pageNumber = 0;
        } else {
            pageNumber -= 1;
        }
        Pageable pageable = PageRequest.of(pageNumber, NUMBER_OF_ITEMS_PER_PAGE);
        return passengerRepository.findAll(pageable);
    }

    @Override
    public void savePassenger(Passenger passenger) {
        passengerRepository.save(passenger);
    }

//    @Override
//    public ApiResponse bookRide(BookRideRequest bookRideRequest) {
//        //1. find passenger
//       getPassengerById(bookRideRequest.getPassengerId());
//        //2. calculate distance between origin and destination
//        DistanceMatrixElement distanceInformation = getDistanceInformation(bookRideRequest.getOrigin(), bookRideRequest.getDestination());
//        //3. calculate eta
//        var eta = distanceInformation.getDuration().getText();
//        //4. calculate price
//        BigDecimal fare = DrideUtilities.calculateRideFare(distanceInformation.getDistance().getText());
//        return ApiResponse.builder().fare(fare).estimatedTimeOfArrival(eta).build();
//    }
//    private DistanceMatrixElement getDistanceInformation(Location origin, Location destination) {
//        RestTemplate restTemplate = new RestTemplate();
//        String url = buildDistanceRequestUrl(origin, destination);
//        ResponseEntity<GoogleDistanceResponse> response =
//                restTemplate.getForEntity(url, GoogleDistanceResponse.class);
//        return Objects.requireNonNull(response.getBody()).getRows().stream()
//                .findFirst().orElseThrow()
//                .getElements().stream()
//                .findFirst()
//                .orElseThrow();
//    }
//    private  String buildDistanceRequestUrl(Location origin, Location destination){
//        return directionConfig.getGoogleDistanceUrl()+"/"+DrideUtilities.JSON_CONSTANT+"?"
//                +"destinations="+DrideUtilities.buildLocation(destination)+"&origins="
//                +DrideUtilities.buildLocation(origin)+"&mode=driving"+"&traffic_model=pessimistic"
//                +"&departure_time="+ LocalDateTime.now().toEpochSecond(ZoneOffset.of("+01:00"))
//                +"&key="+directionConfig.getGoogleApiKey();
//    }
}
