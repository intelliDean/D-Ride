package dean.project.Dride.data.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Referee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String occupation;
    private Integer age;
    @OneToOne
    private Address address;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String phoneNumber;
}
