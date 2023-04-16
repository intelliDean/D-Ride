package dean.project.Dride.data.dto.response;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookRideResponse <T> implements Serializable {
    private T response;
}
