package dean.project.Dride.data.dto.request;

import dean.project.Dride.data.models.Gender;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CompleteDriverRequest {
    private String phoneNumber;
    private Gender gender;
    private Integer houseNumber;
    private String street;
    private String city;
    private String state;
    private String country;
    private String accountName;
    private String accountNumber;
    private String bankName;
}
