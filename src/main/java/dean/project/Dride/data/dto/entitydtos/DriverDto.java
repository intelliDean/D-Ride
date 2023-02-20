package dean.project.Dride.data.dto.entitydtos;

import dean.project.Dride.data.models.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverDto {
    private Long id;
    private String phoneNumber;
    private Address address;
    private MultipartFile profileImage;
    private String licenseId;
    private int age;
    private Gender gender;
    private BankInformation bankInformation;
    private Referee referee;
    private Details details;
}
