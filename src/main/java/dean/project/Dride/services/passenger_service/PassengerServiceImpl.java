package dean.project.Dride.services.passenger_service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import dean.project.Dride.config.distance.DistanceConfig;
import dean.project.Dride.config.security.util.JwtUtil;
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
import dean.project.Dride.services.user_service.utility.UtilityUserImpl;
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
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static dean.project.Dride.data.models.Role.PASSENGER;
import static dean.project.Dride.exceptions.ExceptionMessage.PASSENGER_REG_FAILED;
import static dean.project.Dride.utilities.Constants.*;

@Service
@AllArgsConstructor
@Slf4j
public class PassengerServiceImpl implements PassengerService {
    private final UtilityUserImpl utilityUserImpl;
    private final PassengerRepository passengerRepository;
    private final CloudService cloudService;
    private MockLocationService mockLocationService;
    private final PasswordEncoder passwordEncoder;
    private final DistanceConfig directionConfig;
    private final RideService rideService;
    private final JwtUtil jwtUtil;
    private final Context context;
    private final MailService mailService;
    private final ModelMapper modelMapper;
    private final SpringTemplateEngine templateEngine;
    private final GlobalApiResponse.GlobalApiResponseBuilder globalResponse;

    @Override
    public GlobalApiResponse register(RegisterRequest request) {
        User user = utilityUserImpl.createUser(request.getCreateUser());
        user.getRoles().add(PASSENGER);
        Passenger passenger = Passenger.builder()
                .user(user)
                .build();
        Passenger savedPassenger = passengerRepository.save(passenger);

        String welcomeMail = sendWelcomeMail(savedPassenger);

        return welcomeMail == null
                ? globalResponse.message(PASSENGER_REG_FAILED).build()
                : globalResponse.id(savedPassenger.getId()).message(REG_SUCCESS).build();
    }

    private String sendWelcomeMail(Passenger savedPassenger) {
        User user = savedPassenger.getUser();
        String name = user.getName();
        EmailNotificationRequest mailRequest = new EmailNotificationRequest();
        mailRequest.setSubject(USER_SUBJECT);
        mailRequest.getTo().add(new Recipient(name, user.getEmail()));

        String link = jwtUtil.generateVerificationLink(user.getId());
        // String message = DrideUtilities.passengerWelcomeMail();
        //String content = String.format(message, user.getName(), link);
        context.setVariables(Map.of("name", name, "verifyUrl", link));

        String htmlContent = templateEngine.process("passenger_welcome", context);
        mailRequest.setHtmlContent(htmlContent);
        return mailService.sendHTMLMail(mailRequest);
    }

    private User currentUser() {
        return utilityUserImpl.currentUser();
    }

    @Override
    public Optional<Passenger> getPassengerByUserId(Long userId) {
        return passengerRepository.findPassengerByUser_Id(userId);
    }

    @Override
    public PassengerDTO getPassenger() {
        return modelMapper.map(currentPassenger(), PassengerDTO.class);
    }

    private Passenger currentPassenger() {
        return getPassengerByUserId(currentUser().getId())
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void savePassenger(Passenger passenger) {
        passengerRepository.save(passenger);
    }

    @Override
    public PassengerDTO updatePassenger(JsonPatch updatePayload) {
        ObjectMapper mapper = new ObjectMapper();
        Passenger foundPassenger = currentPassenger();
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
    public void deletePassenger() {
        passengerRepository.deleteById(currentPassenger().getId());
    }

    @Override
    public GlobalApiResponse attemptBookRide(BookRideRequest bookRideRequest) {
        Passenger foundPassenger = currentPassenger();
        if (foundPassenger == null) throw new UserNotFoundException();

        var response = mockLocationService
                .getDistanceInformation(
                        bookRideRequest.getOrigin(),
                        bookRideRequest.getDestination()
                );
        DistanceMatrixElement distanceInformation = response.getRows().get(0).getElements().get(0);
//               getDistanceInformation(bookRideRequest.getOrigin(), bookRideRequest.getDestination());
        String eta = distanceInformation.getDuration().getText();

        BigDecimal fare = DrideUtilities
                .calculateRideFare(
                        distanceInformation
                                .getDistance()
                                .getText());
        return globalResponse
                .fare(fare)
                .estimatedTimeOfArrival(eta)
                .build();
    }

    @Override
    public BookRideResponse<?> bookRide(RideRequest request) {
        BookRideRequest bookRideRequest = BookRideRequest.builder()
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .build();

        var response = attemptBookRide(bookRideRequest);
        if (request.getRideStatus() == null || !request.getRideStatus().equals(Status.BOOKED)) {
            return new BookRideResponse<>(globalResponse
                    .message("Incomplete Ride Booking")
                    .fare(response.getFare())
                    .estimatedTimeOfArrival(response.getEstimatedTimeOfArrival())
                    .build());
        }

        Ride ride = Ride.builder()
                .fare(response.getFare())
                .passenger(currentPassenger())
                .destination(request.getDestination().toString())
                .origin(request.getOrigin().toString())
                .bookTime(LocalDateTime.now().toString())
                .rideStatus(request.getRideStatus())
                .build();

        Ride savedRide = rideService.save(ride);
        return new BookRideResponse<>(savedRide);
    }

    @Override
    public GlobalApiResponse rateDriver(RateRequest request) {
        Ride ride = rideService.getRideByPassengerIdAndDriverIdRideStatus(
                currentPassenger().getId(), request.getRateeId(), Status.END_RIDE);
        ride.setDriverRating(request.getRating());
        rideService.save(ride);
        String driverName = ride.getDriver().getUser().getName();
        String rating = request.getRating().toString();
        return globalResponse
                .message(String.format(RATING, driverName, rating))
                .build();
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
        return directionConfig.getGoogleDistanceUrl() + "/" + "json" + "?"
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
//                .message.html("User Registration Successful")
//                .build();
//    }
}
