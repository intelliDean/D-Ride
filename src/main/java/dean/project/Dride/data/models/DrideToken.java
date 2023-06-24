package dean.project.Dride.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DrideToken {
    @Id
    @GeneratedValue
    private Long id;
    private String accessToken;
    private String refreshToken;
    private boolean expired;
    private boolean revoked;
}
