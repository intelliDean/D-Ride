package dean.project.Dride.services.driverServices;

import dean.project.Dride.cloud_business.CloudService;
import dean.project.Dride.data.dto.request.EmailNotificationRequest;
import dean.project.Dride.data.dto.request.Recipient;
import dean.project.Dride.data.dto.request.UserRegisterRequest;
import dean.project.Dride.data.dto.response.RegisterResponse;
import dean.project.Dride.data.models.Details;
import dean.project.Dride.data.models.Driver;
import dean.project.Dride.data.repositories.DriverRepository;
import dean.project.Dride.exceptions.ImageUploadException;
import dean.project.Dride.exceptions.UserNotFoundException;
import dean.project.Dride.notification.MailService;
import dean.project.Dride.utilities.DrideUtilities;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Slf4j
@AllArgsConstructor
@Service
public class DriverServiceImpl implements DriverService {
    private final DriverRepository driverRepository;
    private final CloudService cloudService;
    private final ModelMapper modelMapper;
    private final MailService mailService;

    @Override
    public RegisterResponse register(UserRegisterRequest userRegisterRequest, MultipartFile licenseImage) {
        Details details = modelMapper.map(userRegisterRequest, Details.class);
        details.setRegisteredAt(LocalDateTime.now().toString());

        //steps
        //1. upload drivers license image
        var imageUrl = cloudService.upload(licenseImage);
        if (imageUrl == null) {
            throw new ImageUploadException("Driver Registration failed. Upload license and try again");
        }
        //2. create driver object
        Driver driver = Driver.builder()
                .details(details)
                .licenseImage(imageUrl)
                .build();
        //3. save driver
        Driver savedDriver = driverRepository.save(driver);
        //4. send verification mail to driver
        EmailNotificationRequest request = buildNotificationRequest(
                savedDriver.getDetails().getEmail(),
                savedDriver.getDetails().getName(),
                savedDriver.getId());
        String response = mailService.sendHtmlMail(request);

        if (response == null) return failureResponse();

        return RegisterResponse.builder()
                .code(HttpStatus.CREATED.value())
                .id(savedDriver.getId())
                .isSuccessful(true)
                .message("Driver Registration Successful")
                .build();
    }

    @Override
    public Driver getDriverById(Long driverId) {
        return driverRepository.findById(driverId)
                .orElseThrow(()-> new UserNotFoundException("Driver could not be found"));
    }

    @Override
    public Driver savedriver(Driver driver) {
        return driverRepository.save(driver);
    }

    private static RegisterResponse failureResponse() {
            return RegisterResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .id(-1L)
                    .isSuccessful(false)
                    .message("Driver Registration Failed")
                    .build();
        }

    private EmailNotificationRequest buildNotificationRequest(String email, String name, Long userId) {
        EmailNotificationRequest request = new EmailNotificationRequest();
        request.getTo().add(new Recipient(name, email));
        String template = DrideUtilities.getMailTemplate();
        String content = String.format(template, name, DrideUtilities.generateVerificationLink(userId));
        request.setHtmlContent(content);
        return request;
    }
}
