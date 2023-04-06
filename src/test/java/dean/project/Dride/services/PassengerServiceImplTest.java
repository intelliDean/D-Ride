package dean.project.Dride.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jackson.jsonpointer.JsonPointerException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.ReplaceOperation;
import dean.project.Dride.data.dto.request.*;
import dean.project.Dride.data.dto.response.ApiResponse;
import dean.project.Dride.data.dto.response.RegisterResponse;
import dean.project.Dride.data.models.Users;
import dean.project.Dride.data.models.Passenger;
import dean.project.Dride.exceptions.DrideException;
import dean.project.Dride.notification.MailService;
import dean.project.Dride.services.passengerServices.PassengerService;
import dean.project.Dride.utilities.DrideUtilities;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Slf4j
class PassengerServiceImplTest {
    @Autowired
    private PassengerService passengerService;
    private RegisterPassengerRequest request;

    @BeforeEach
    void setUp() throws IOException {
        request = new RegisterPassengerRequest();
        request.setEmail("test@email.com");
        request.setPassword("testPassword");
        request.setName("Amirah Tinubu");
    }

    @Test
    void registerTest() {
        RegisterResponse registerResponse = passengerService.register(request);
        assertThat(registerResponse).isNotNull();
    }

    @Test
    public void getUserByIdTest(){
        var registerResponse =passengerService.register(request);
        Passenger foundPassenger = passengerService.getPassengerById(registerResponse.getId());
        assertThat(foundPassenger).isNotNull();
        Users userDetails=foundPassenger.getUsers();
        assertThat(userDetails.getName()).isEqualTo(request.getName());
    }

    @Test
    public void updatePassengerTest() throws JsonPointerException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("2349099876543");
        JsonPatch updatePayload = new JsonPatch(List.of(
                new ReplaceOperation(new JsonPointer("/phoneNumber"),
                        node)
        ));
        var registerResponse = passengerService.register(request);
        var updatedPassenger =passengerService.updatePassengerInfo(registerResponse.getId(), updatePayload);
        assertThat(updatedPassenger).isNotNull();
        assertThat(updatedPassenger.getMessage()).isNotNull();
    }

    @Test
    public void deletePassengerTest() {
       var response = passengerService.register(request);
       passengerService.deletePassenger(response.getId());
       assertThrows(DrideException.class, ()->passengerService.getPassengerById(response.getId()));

    }

//    @Test
//    public void bookRide(){
//        RegisterResponse response = passengerService.register(request);
//        BookRideRequest bookRideRequest = buildBookRideRequest(response.getId());
//        ApiResponse bookRideResponse = passengerService.bookRide(bookRideRequest);
//        log.info("response->{}", bookRideResponse);
//        assertThat(bookRideResponse).isNotNull();
//    }

    private BookRideRequest buildBookRideRequest(Long passengerId){
        BookRideRequest bookRideRequest = new BookRideRequest();
        bookRideRequest.setPassengerId(passengerId);
        bookRideRequest.setOrigin(new Location("312", "Herbert Macaulay Way", "Yaba", "Lagos"));
        bookRideRequest.setDestination(new Location("371", "Herbert Macaulay Way", "Yaba", "Lagos"));
        return bookRideRequest;
    }




}