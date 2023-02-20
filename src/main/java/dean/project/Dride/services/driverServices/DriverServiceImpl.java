package dean.project.Dride.services.driverServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import dean.project.Dride.cloud_business.CloudService;
import dean.project.Dride.data.dto.entitydtos.DriverDto;
import dean.project.Dride.data.dto.request.UserRegisterRequest;
import dean.project.Dride.data.dto.response.RegisterResponse;
import dean.project.Dride.data.dto.response.UserUpdateResponse;
import dean.project.Dride.data.models.Details;
import dean.project.Dride.data.models.Driver;
import dean.project.Dride.data.repositories.DriverRepository;
import dean.project.Dride.dride_mappers.DrideMappers;
import dean.project.Dride.exceptions.ImageUploadException;
import dean.project.Dride.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class DriverServiceImpl implements DriverService {
    private final DriverRepository driverRepository;
    private final CloudService cloudService;
    private final ModelMapper modelMapper;

    @Override
    public RegisterResponse register(UserRegisterRequest userRegisterRequest, MultipartFile licenseImage) {
        Details details = modelMapper.map(userRegisterRequest, Details.class);
        details.setRegisteredAt(LocalDateTime.now().toString());

        //steps
        //1. upload drivers license image
        var imageUrl = cloudService.upload(licenseImage);
        if (imageUrl == null) {
            throw new ImageUploadException("Driver registration failed because license could not be uploaded. Try gain");
        }
        //2. create driver object
        Driver driver = Driver.builder()
                .details(details)
                .licenseImage(imageUrl)
                .build();
        //3. save driver
        Driver savedDriver = driverRepository.save(driver);
        return RegisterResponse.builder()
                .code(HttpStatus.CREATED.value())
                .id(savedDriver.getId())
                .isSuccessful(true)
                .message("Driver Registration Successful")
                .build();
    }



//    @Override
//    public DriverDto getById(Long id) {
//        Driver driver = driverRepository.findById(id).orElseThrow(
//                ()->new UserNotFoundException("Driver not found"));
//        return DrideMappers.maptoDriverDto(driver);
//    }

//    @Override
//    public List<DriverDto> getAllDriver() {
//        return null;
//    }


//    @Override
//    public UserUpdateResponse patchUpdate(Long driverId, JsonPatch updatePayload) {
//        ObjectMapper mapper = new ObjectMapper();
//        DriverDto driverDto = getById(driverId);
//        JsonNode node = mapper.convertValue(driverDto, JsonNode.class);
//
//        try {
//            JsonNode updatedNode = updatePayload.apply(node);
//            var updatedDriverDto = mapper.convertValue(updatedNode, DriverDto.class);
//            Driver driver = DrideMappers.mapToDriverEntity(updatedDriverDto);
//            var returnedDriver = driverRepository.save(driver);
//
//            return getUpdateResponse(returnedDriver);
//        } catch (JsonPatchException ex) {
//            log.error(ex.getMessage());
//            throw new RuntimeException();
//        }
//    }

//    @Override
//    public UserUpdateResponse updateDriver(Long id, DriverDto passengerDto) {
//        return null;
//    }
//
//    private static UserUpdateResponse getUpdateResponse(Driver driver) {
//        UserUpdateResponse patch = new UserUpdateResponse();
//        patch.setMessage(String.format("Passenger with ID %d updated successfully", driver.getId()));
//
//        return patch;
//    }
//
//    //@Override
//    public UserUpdateResponse updatePassenger(Long id, DriverDto driverDto) {
//        DriverDto returnedDriverDto = getById(id);
//
//        returnedDriverDto = DrideMappers.
//
//        re.s
//        driverDto.setDetails(passengerDto.getDetails());
//        passenger.setGender(passengerDto.getGender());
//        passenger.setPhoneNumber(passengerDto.getPhoneNumber());
//
//        Passenger returnedPassenger = passengerRepository.save(passenger);
//
//        return getUpdateResponse(returnedPassenger);
//        return null;
//    }
//
//    @Override
//    public void deleteById(Long id) {
//
//    }
//
//    @Override
//    public void deleteAll() {
//
//    }
}
