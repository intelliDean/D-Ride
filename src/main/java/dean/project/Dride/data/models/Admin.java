package dean.project.Dride.data.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String employeeId;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)       //fetches the appuser with admin when fetched
    private Users users;

}
