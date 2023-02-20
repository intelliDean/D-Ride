package dean.project.Dride.data.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserRegisterRequest {
    //@JsonProperty("full_name")  //used to specify the column name of the field name in the db
    private String name;
    private String email;
    private String password;
}
