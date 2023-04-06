package dean.project.Dride.data.dto.response;


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