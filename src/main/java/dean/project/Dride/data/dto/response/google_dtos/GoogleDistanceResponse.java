package dean.project.Dride.data.dto.response.google_dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class GoogleDistanceResponse {

    @JsonProperty("destination_address")
    private List<String> destinationAddresses;

    @JsonProperty("origin_address")
    private List<String> originAddresses;

    private List<DistanceMatrixRow> rows;
}
