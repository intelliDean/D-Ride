package dean.project.Dride.data.dto.request;

import dean.project.Dride.data.models.Details;
import dean.project.Dride.data.models.Gender;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDto {
    private Long id;
    private String phoneNumber;
    private Gender gender;
    private Details details;
}
