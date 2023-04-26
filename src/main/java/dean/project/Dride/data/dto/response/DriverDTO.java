package dean.project.Dride.data.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import dean.project.Dride.data.models.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DriverDTO {
    @JsonUnwrapped
    private User user;
    private String phoneNumber;
    private Address address;
    private Gender gender;
    private String licenseId;
    private String licenseImage;
    private int age;
    private Referee referee;
    private BankInformation bankInformation;

}
