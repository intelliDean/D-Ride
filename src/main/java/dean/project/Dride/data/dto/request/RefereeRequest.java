package dean.project.Dride.data.dto.request;

import dean.project.Dride.data.models.Gender;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RefereeRequest {
    private String firstName;
    private String lastName;
    private String occupation;
    private String dateOfBirth;
    private Integer houseNumber;
    private String street;
    private String city;
    private String state;
    private String country;
    private Gender gender;
    private String phoneNumber;
}
