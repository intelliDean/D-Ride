package dean.project.Dride.data.dto.response.google_dtos;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Fare {
    private String currency;
    private double value;

    private String text;
}
