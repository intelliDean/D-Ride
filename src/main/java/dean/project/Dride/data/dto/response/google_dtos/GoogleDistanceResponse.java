package dean.project.Dride.data.dto.response.google_dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

import static dean.project.Dride.utilities.Constants.DESTINATION_ADDRESSES;
import static dean.project.Dride.utilities.Constants.ORIGIN_ADDRESSES;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class GoogleDistanceResponse {

    @JsonProperty(DESTINATION_ADDRESSES)
    private List<String> destinationAddresses;

    @JsonProperty(ORIGIN_ADDRESSES)
    private List<String> originAddresses;

    private List<DistanceMatrixRow> rows;
}
