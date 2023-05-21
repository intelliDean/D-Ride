package dean.project.Dride.service;

import dean.project.Dride.data.dto.request.*;
import dean.project.Dride.data.dto.response.api_response.GlobalApiResponse;
import dean.project.Dride.exceptions.DrideException;
import dean.project.Dride.services.passenger_service.PassengerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Slf4j
class PassengerServiceImplTest {
    @Autowired
    private PassengerService passengerService;
    private RegisterPassengerRequest request;

    @BeforeEach
    void setUp() throws IOException {

    }

    @Test
    void registerTest() {
        RegisterRequest req = new RegisterRequest();
        req.getCreateUser().setEmail("man@email.com");
        req.getCreateUser().setPassword("testPassword");
        req.getCreateUser().setName("Amira Tinubu");
        GlobalApiResponse registerResponse = passengerService.register(req);
        assertThat(registerResponse).isNotNull();
    }

    @Test
    public void getUserByIdTest() {
//        RegisterPassengerRequest req = new RegisterPassengerRequest();
//        req.setEmail("marie@email.com");
//        req.setPassword("testPassword");
//        req.setName("Amira Kay");
//        GlobalApiResponse registerResponse = passengerService.register(req);
//        assertThat(registerResponse).isNotNull();
//
//        Passenger foundPassenger = passengerService.getPassengerById(registerResponse.getId());
//        assertThat(foundPassenger).isNotNull();
//        assertThat(foundPassenger.getUser().getEmail()).isEqualTo("marie@email.com");
//
//        User user = foundPassenger.getUser();
//        assertThat(user).isNotNull();
//        assertThat(user.getName()).isEqualTo("Amira Kay");

    }

//    @Test
//    public void updatePassengerTest() throws JsonPointerException, JsonProcessingException {
//
//        RegisterPassengerRequest req = new RegisterPassengerRequest();
//        req.setEmail("dean122@email.com");
//        req.setPassword("tsteePassword");
//        req.setName("Testnrg");
//        var registerResponse = passengerService.register(req);
//
//        var freshPassenger = passengerService.getPassengerById(registerResponse.getId());
//        assertThat(freshPassenger.getPhoneNumber()).isNull();
//
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode node = mapper.readTree("2349099876543");
//        JsonPatch updatePayload = new JsonPatch(List.of(
//                new ReplaceOperation(new JsonPointer("/phoneNumber"),
//                        node)
//        ));
//
//        var updatedPassenger = passengerService.updatePassenger(freshPassenger.getId(), updatePayload);
//        assertThat(updatedPassenger).isNotNull();
//        assertThat(updatedPassenger.getPhoneNumber()).isNotNull();
//        assertThat(updatedPassenger.getPhoneNumber()).isEqualTo("2349099876543");
//    }

    @Test
    public void deletePassengerTest() {
        RegisterRequest req = new RegisterRequest();
        req.getCreateUser().setEmail("deanMan@email.com");
        req.getCreateUser().setPassword("testPassword");
        req.getCreateUser().setName("Amira Kay");
        GlobalApiResponse registerResponse = passengerService.register(req);
        assertThat(registerResponse).isNotNull();

        passengerService.deletePassenger();
        assertThrows(DrideException.class, () -> passengerService.getPassenger());

    }

    @Test
    public void bookRide() {
        RegisterRequest req = new RegisterRequest();
        req.getCreateUser().setEmail("mari@email.com");
        req.getCreateUser().setPassword("testPassword");
        req.getCreateUser().setName("Amira Kay");
        GlobalApiResponse registerResponse = passengerService.register(req);
        assertThat(registerResponse).isNotNull();

        BookRideRequest bookRideRequest = buildBookRideRequest(registerResponse.getId());
        GlobalApiResponse bookRideResponse = passengerService.attemptBookRide(bookRideRequest);
        log.info("response->{}", bookRideResponse);
        assertThat(bookRideResponse).isNotNull();
    }

    private BookRideRequest buildBookRideRequest(Long passengerId) {
        BookRideRequest bookRideRequest = new BookRideRequest();
        bookRideRequest.setOrigin(new Location("312", "Herbert Macaulay Way", "Yaba", "Lagos"));
        bookRideRequest.setDestination(new Location("371", "Herbert Macaulay Way", "Yaba", "Lagos"));
        return bookRideRequest;
    }

//    @Test     I will use this to mock user authentication     https://www.baeldung.com/spring-security-method-security
//    @WithMockUser(username = "john", roles = {"VIEWER"})
//    public void givenRoleViewer_whenCallGetUsername_thenReturnUsername() {
//        String userName = userRoleService.getUsername();
//
//        assertEquals("john", userName);
//    }
}