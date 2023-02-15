package dean.project.Dride.data.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String employeeId;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)       //fetches the appuser with admin when fetched
    private Details appUser;

}
