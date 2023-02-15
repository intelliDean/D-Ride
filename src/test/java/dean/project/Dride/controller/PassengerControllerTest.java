package dean.project.Dride.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dean.project.Dride.data.dto.request.RegisterPassengerRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class PassengerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    void setUp() {

    }

    @Test
    void testRegister() throws Exception {
        RegisterPassengerRequest request =
        RegisterPassengerRequest.builder()
                        .name("Dean")
                                .email("@gmail")
                                        .password("1234")
                                                .build();

       mockMvc.perform(post("/api/v1/passenger/register")
                 .content(objectMapper.writeValueAsString(request))
                 .contentType(MediaType.APPLICATION_JSON_VALUE))
                 .andExpect(status().is(HttpStatus.CREATED.value()))
               .andDo(print());
    }
}