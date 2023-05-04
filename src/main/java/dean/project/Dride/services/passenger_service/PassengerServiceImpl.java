package dean.project.Dride.services.passenger_service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import dean.project.Dride.config.distance.DistanceConfig;
import dean.project.Dride.data.dto.request.*;
import dean.project.Dride.data.dto.response.api_response.BookRideResponse;
import dean.project.Dride.data.dto.response.api_response.GlobalApiResponse;
import dean.project.Dride.data.dto.response.entity_dtos.PassengerDTO;
import dean.project.Dride.data.dto.response.google_dtos.DistanceMatrixElement;
import dean.project.Dride.data.dto.response.google_dtos.GoogleDistanceResponse;
import dean.project.Dride.data.models.Passenger;
import dean.project.Dride.data.models.Ride;
import dean.project.Dride.data.models.Status;
import dean.project.Dride.data.models.User;
import dean.project.Dride.data.repositories.PassengerRepository;
import dean.project.Dride.exceptions.UserNotFoundException;
import dean.project.Dride.services.cloud.CloudService;
import dean.project.Dride.services.mocklocation_service.MockLocationService;
import dean.project.Dride.services.notification.MailService;
import dean.project.Dride.services.ride_services.RideService;
import dean.project.Dride.utilities.DrideUtilities;
import dean.project.Dride.utilities.Paginate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;

import static dean.project.Dride.data.models.Role.PASSENGER;
import static dean.project.Dride.exceptions.ExceptionMessage.PASSENGER_REG_FAILED;
import static dean.project.Dride.utilities.Constants.*;

@Service
@AllArgsConstructor
@Slf4j
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;
    private final CloudService cloudService;

    private MockLocationService mockLocationService;
    private final PasswordEncoder passwordEncoder;
    private final DistanceConfig directionConfig;
    private final RideService rideService;
    private final MailService mailService;
    private final ModelMapper modelMapper;
    private final GlobalApiResponse.GlobalApiResponseBuilder globalResponse;

    @Override
    public GlobalApiResponse register(RegisterPassengerRequest registerRequest) {

        Passenger passenger = createPassenger(registerRequest);
        Passenger savedPassenger = passengerRepository.save(passenger);

        String welcomeMail = sendWelcomeMail(savedPassenger);

        if (welcomeMail == null)
            return globalResponse
                .message(PASSENGER_REG_FAILED)
                .build();

        return globalResponse
                .id(savedPassenger.getId())
                .message(REG_SUCCESS)
                .build();
    }

    private Passenger createPassenger(RegisterPassengerRequest registerRequest) {
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreatedAt(LocalDateTime.now().toString());
        user.setRoles(new HashSet<>());
        user.getRoles().add(PASSENGER);
        return Passenger.builder()
                .user(user)
                .build();
    }

    private String sendWelcomeMail(Passenger savedPassenger) {
        User user = savedPassenger.getUser();
        EmailNotificationRequest mailRequest = new EmailNotificationRequest();
        mailRequest.setSubject(USER_SUBJECT);
        mailRequest.getTo().add(new Recipient(user.getName(), user.getEmail()));

        String message = DrideUtilities.passengerWelcomeMail();
        String link = DrideUtilities.generateVerificationLink(user.getId());
        String content = String.format(message, user.getName(), link);

        mailRequest.setHtmlContent(content);
        return mailService.sendHTMLMail(mailRequest);
    }

    @Override
    public PassengerDTO getPassengerById(Long passengerId) {
        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(UserNotFoundException::new);
        return modelMapper.map(passenger, PassengerDTO.class);
    }

    @Override
    public void savePassenger(Passenger passenger) {
        passengerRepository.save(passenger);
    }

    @Override
    public Optional<Passenger> getPassengerBy(Long passengerId) {
        return passengerRepository.findById(passengerId);
    }

    @Override
    public PassengerDTO updatePassenger(Long passengerId, JsonPatch updatePayload) {
        ObjectMapper mapper = new ObjectMapper();
        Passenger foundPassenger = getInnerPassenger(passengerId);
        //Passenger Object to node
        JsonNode node = mapper.convertValue(foundPassenger, JsonNode.class);
        try {
            //apply patch
            JsonNode updatedNode = updatePayload.apply(node);
            //node to Passenger Object
            var updatedPassenger = mapper.convertValue(updatedNode, Passenger.class);

            passengerRepository.save(updatedPassenger);
            return modelMapper.map(foundPassenger, PassengerDTO.class);
        } catch (JsonPatchException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public Paginate<PassengerDTO> getAllPassengers(int pageNumber) {
        int page = pageNumber < 1 ? 0 : pageNumber - 1;
        Pageable pageable = PageRequest.of(page, NUMBER_OF_ITEMS_PER_PAGE);
        Page<Passenger> pagedPassenger = passengerRepository.findAll(pageable);
        Type returnedPassenger = new TypeToken<Paginate<PassengerDTO>>() {
        }.getType();
        return modelMapper.map(pagedPassenger, returnedPassenger);
    }


    @Override
    public void deletePassenger(Long id) {
        passengerRepository.deleteById(id);
    }

    private Passenger getInnerPassenger(Long id) {
        return passengerRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public GlobalApiResponse attemptBookRide(BookRideRequest bookRideRequest) {
        Passenger foundPassenger = getInnerPassenger(bookRideRequest.getPassengerId());
        if (foundPassenger == null) throw new UserNotFoundException();

        var response = mockLocationService.getDistanceInformation(bookRideRequest.getOrigin(), bookRideRequest.getDestination());
        DistanceMatrixElement distanceInformation = response.getRows().get(0).getElements().get(0);
//               getDistanceInformation(bookRideRequest.getOrigin(), bookRideRequest.getDestination());
        String eta = distanceInformation.getDuration().getText();

        BigDecimal fare = DrideUtilities.calculateRideFare(distanceInformation.getDistance().getText());
        return globalResponse
                .fare(fare)
                .estimatedTimeOfArrival(eta)
                .build();
    }

    @Override
    public PassengerDTO getPassengerByEmail(String email) {
        Passenger passenger = passengerRepository.findPassengerByUser_Email(email)
                .orElseThrow(UserNotFoundException::new);
        return modelMapper.map(passenger, PassengerDTO.class);
    }

    @Override
    public BookRideResponse<?> bookRide(RideRequest request) {
        BookRideRequest bookRideRequest = BookRideRequest.builder()
                .passengerId(request.getPassengerId())
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .build();

        var response = attemptBookRide(bookRideRequest);
        if (request.getRideStatus() == null || !request.getRideStatus().equals(Status.BOOKED)) {
            return new BookRideResponse<>(globalResponse
                    .message(INCOMPLETE)
                    .fare(response.getFare())
                    .estimatedTimeOfArrival(response.getEstimatedTimeOfArrival())
                    .build());
        }

        Passenger passenger = getInnerPassenger(request.getPassengerId());

        Ride ride = Ride.builder()
                .fare(response.getFare())
                .passenger(passenger)
                .destination(request.getDestination().toString())
                .origin(request.getOrigin().toString())
                .bookTime(LocalDateTime.now().toString())
                .rideStatus(request.getRideStatus())
                .build();

        Ride savedRide = rideService.save(ride);
        return new BookRideResponse<>(savedRide);
    }

    @Override
    public GlobalApiResponse rateDriver(RateDriverRequest request) {
        Ride ride = rideService.getRideByPassengerIdAndDriverIdRideStatus(
                request.getPassengerId(), request.getDriverId(), Status.END_RIDE);
        ride.setDriverRating(request.getRating());
        rideService.save(ride);
        String driverName = ride.getDriver().getUser().getName();
        String rating = request.getRating().toString();
        return globalResponse
                .message(String.format(RATING, driverName, rating))
                .build();
    }

    @Override
    public Optional<Passenger> getPassengerByUserId(Long userId) {
        return passengerRepository.findPassengerByUser_Id(userId);
    }

    private DistanceMatrixElement getDistanceInformation(Location origin, Location destination) {
        RestTemplate restTemplate = new RestTemplate();
        String url = buildDistanceRequestUrl(origin, destination);
        ResponseEntity<GoogleDistanceResponse> response =
                restTemplate.getForEntity(url, GoogleDistanceResponse.class);
        return Objects.requireNonNull(response.getBody()).getRows().stream()
                .findFirst().orElseThrow()
                .getElements().stream()
                .findFirst()
                .orElseThrow();
    }

    private String buildDistanceRequestUrl(Location origin, Location destination) {
        return directionConfig.getGoogleDistanceUrl() + "/" + JSON_CONSTANT + "?"
                + "destinations=" + DrideUtilities.buildLocation(destination) + "&origins="
                + DrideUtilities.buildLocation(origin) + "&mode=driving" + "&traffic_model=pessimistic"
                + "&departure_time=" + LocalDateTime.now().toEpochSecond(ZoneOffset.of("+01:00"))
                + "&key=" + directionConfig.getGoogleApiKey();
    }

//    private GlobalApiResponse getRegisterResponse(Passenger savedPassenger) {
//        RegisterResponse registerResponse = new RegisterResponse();
//        registerResponse.setId(savedPassenger.getId());
//        registerResponse.setSuccess(true);
//        registerResponse.setMessage("User Registration Successful");
//        return globalResponse
//                .id(savedPassenger.getId())
//                .message("User Registration Successful")
//                .build();
//    }
}
