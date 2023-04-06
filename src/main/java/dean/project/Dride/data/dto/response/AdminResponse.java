package dean.project.Dride.data.dto.response;


import dean.project.Dride.data.models.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminResponse {
    private Long employeeId;
    private Users users;

}
