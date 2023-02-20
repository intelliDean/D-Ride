package dean.project.Dride.services;

import com.fasterxml.jackson.databind.node.TextNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jackson.jsonpointer.JsonPointerException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.ReplaceOperation;
import dean.project.Dride.data.dto.request.UserRegisterRequest;
import dean.project.Dride.data.dto.response.RegisterResponse;
import dean.project.Dride.data.models.Details;
import dean.project.Dride.data.models.Passenger;
import dean.project.Dride.services.passengerServices.PassengerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PassengerServiceImplTest {
    @Autowired
    PassengerService passengerService;
    UserRegisterRequest request;

    @BeforeEach
    void setUp() {
        request = new UserRegisterRequest();
        request.setName("Dean");
        request.setPassword("password");
        request.setEmail("@gmail");
    }
    @Test
    void registerTest() {

        RegisterResponse register = passengerService.register(request);
        assertThat(register).isNotNull();
        assertThat(register.getCode()).isEqualTo(HttpStatus.CREATED.value());
    }
    @Test
    void getUser(){
        var registerResponse = passengerService.register(request);
        Passenger p = passengerService.getById(registerResponse.getId());
        assertThat(p).isNotNull();
        Details d = p.getDetails();
        assertThat(d.getName()).isEqualTo(request.getName());
    }
    @Test
    void updated() throws JsonPointerException {
        JsonPatch upPayload = new JsonPatch(List.of(new ReplaceOperation(new JsonPointer("/email"), new TextNode("dean@gmail"))));
        var registerResponse = passengerService.register(request);

    }



}