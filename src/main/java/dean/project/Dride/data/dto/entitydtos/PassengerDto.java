package dean.project.Dride.data.dto.entitydtos;

import dean.project.Dride.data.models.Details;
import dean.project.Dride.data.models.Gender;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassengerDto {
    private Long id;
    private String phoneNumber;
    private Gender gender;
    private Details details;
}
