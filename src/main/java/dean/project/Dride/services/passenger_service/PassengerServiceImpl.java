package dean.project.Dride.services.passenger_service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import dean.project.Dride.config.app.Paginate;
import dean.project.Dride.config.distance.DistanceConfig;
import dean.project.Dride.data.dto.request.*;
import dean.project.Dride.data.dto.response.*;
import dean.project.Dride.data.models.Passenger;
import dean.project.Dride.data.models.Ride;
import dean.project.Dride.data.models.Status;
import dean.project.Dride.data.models.User;
import dean.project.Dride.data.repositories.PassengerRepository;
import dean.project.Dride.exceptions.DrideException;
import dean.project.Dride.exceptions.UserNotFoundException;
import dean.project.Dride.services.cloud.CloudService;
import dean.project.Dride.services.mocklocation_service.MockLocationService;
import dean.project.Dride.services.notification.MailService;
import dean.project.Dride.services.ride_services.RideService;
import dean.project.Dride.utilities.DrideUtilities;
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
import static dean.project.Dride.utilities.DrideUtilities.NUMBER_OF_ITEMS_PER_PAGE;
import static dean.project.Dride.utilities.DrideUtilities.USER_SUBJECT;

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
    private ModelMapper modelMapper;

    @Override
    public RegisterResponse register(RegisterPassengerRequest registerRequest) {

        Passenger passenger = createPassenger(registerRequest);
        Passenger savedPassenger = passengerRepository.save(passenger);

        String welcomeMail = sendWelcomeMail(savedPassenger);

        if (welcomeMail == null) {
            RegisterResponse.builder()
                    .message("Registration Failed! Please check your connection and try again later")
                    .isSuccess(false)
                    .build();
        }
        return getRegisterResponse(savedPassenger);
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
        User appUser = savedPassenger.getUser();
        EmailNotificationRequest mailRequest = new EmailNotificationRequest();
        mailRequest.setSubject(USER_SUBJECT);
        mailRequest.getTo().add(new Recipient(appUser.getName(), appUser.getEmail()));
        String message = DrideUtilities.getMailTemplate();
        String content = String.format(message, appUser.getName(), DrideUtilities.generateVerificationLink(savedPassenger.getId()));
        mailRequest.setHtmlContent(content);

        return mailService.sendHtmlMail(mailRequest);
    }

    @Override
    public Passenger getPassengerById(Long passengerId) {
        final String exception = String.format("Passenger with id %d not found", passengerId);
        return passengerRepository.findById(passengerId)
                .orElseThrow(() -> new DrideException(exception));
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
    public Passenger updatePassenger(Long passengerId, JsonPatch updatePayload) {
        ObjectMapper mapper = new ObjectMapper();
        Passenger foundPassenger = getPassengerById(passengerId);
        //Passenger Object to node
        JsonNode node = mapper.convertValue(foundPassenger, JsonNode.class);
        try {
            //apply patch
            JsonNode updatedNode = updatePayload.apply(node);
            //node to Passenger Object
            var updatedPassenger = mapper.convertValue(updatedNode, Passenger.class);

            return passengerRepository.save(updatedPassenger);
        } catch (JsonPatchException e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public Paginate<Passenger> getAllPassengers(int pageNumber) {
        if (pageNumber < 1) pageNumber = 0;
        else pageNumber -= 1;

        Pageable pageable = PageRequest.of(pageNumber, NUMBER_OF_ITEMS_PER_PAGE);
        Page<Passenger> pagedPassenger = passengerRepository.findAll(pageable);
        Type returnedPassenger = new TypeToken<Paginate<Passenger>>() {
        }.getType();
        return modelMapper.map(pagedPassenger, returnedPassenger);
    }


    @Override
    public void deletePassenger(Long id) {
        passengerRepository.deleteById(id);
    }


    @Override
    public ApiResponse attemptBookRide(BookRideRequest bookRideRequest) {
        //Passenger currentUser = currentPassenger();

        //1. find passenger
        Passenger foundPassenger = getPassengerById(bookRideRequest.getPassengerId());
        if (foundPassenger == null) throw new DrideException(
                String.format("passenger with id %d not found", bookRideRequest.getPassengerId())
        );
        //2. calculate distance between origin and destination
        var response = mockLocationService.getDistanceInformation(bookRideRequest.getOrigin(), bookRideRequest.getDestination());
        DistanceMatrixElement distanceInformation = response.getRows().get(0).getElements().get(0);
//               getDistanceInformation(bookRideRequest.getOrigin(), bookRideRequest.getDestination());
        //3. calculate eta
        String eta = distanceInformation.getDuration().getText();
        //4. calculate price
        BigDecimal fare = DrideUtilities.calculateRideFare(distanceInformation.getDistance().getText());
        return ApiResponse.builder()
                .fare(fare)
                .estimatedTimeOfArrival(eta)
                .build();
    }

    @Override
    public Passenger getPassengerByEmail(String email) {
        return passengerRepository.findPassengerByUser_Email(email)
                .orElseThrow(() -> new UserNotFoundException("Could not find"));
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
            return new BookRideResponse<>(ApiResponse.builder()
                    .message("Incomplete Ride Booking")
                    .fare(response.getFare())
                    .estimatedTimeOfArrival(response.getEstimatedTimeOfArrival())
                    .build());
        }

        Passenger passenger = getPassengerById(request.getPassengerId());

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
    public ApiResponse rateDriver(RateDriverRequest request) {
        Ride ride = rideService.getRideByPassengerIdAndDriverIdRideStatus(
                request.getPassengerId(), request.getDriverId(), Status.END_RIDE);

        ride.setDriverRating(request.getRating());
        rideService.save(ride);
        String driverName = ride.getDriver().getUser().getName();

        return ApiResponse.builder()
                .message(String.format("That you for your feedback! You have rated %s : %s",
                        driverName, request.getRating().toString()))
                .build();
    }

    @Override
    public Optional<Passenger> getPassengerByUserId(Long userId) {
        return passengerRepository.findPassengerByUser_Id(userId);
    }

//    @Override
//    public Passenger currentPassenger() {
//        try {
//            UserDetails authenticatedUser =
//                    (UserDetails) SecurityContextHolder
//                            .getContext()
//                            .getAuthentication()
//                            .getPrincipal();
//            log.info("authenticatedUser: {}", authenticatedUser.getUsername());
//
//            return passengerRepository.findPassengerByUserDetails_Email(authenticatedUser.getUsername())
//                    .orElseThrow(() -> new BusinessLogicException("Passenger not found"));
//        } catch (Exception e) {
//            throw new UserNotFoundException("Passenger not found");
//        }
//    }

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
        return directionConfig.getGoogleDistanceUrl() + "/" + DrideUtilities.JSON_CONSTANT + "?"
                + "destinations=" + DrideUtilities.buildLocation(destination) + "&origins="
                + DrideUtilities.buildLocation(origin) + "&mode=driving" + "&traffic_model=pessimistic"
                + "&departure_time=" + LocalDateTime.now().toEpochSecond(ZoneOffset.of("+01:00"))
                + "&key=" + directionConfig.getGoogleApiKey();
    }

    private static RegisterResponse getRegisterResponse(Passenger savedPassenger) {
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setId(savedPassenger.getId());
        registerResponse.setSuccess(true);
        registerResponse.setMessage("User Registration Successful");
        return registerResponse;
    }
}
