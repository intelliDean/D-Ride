package dean.project.Dride.data.models;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String phoneNumber;

    @OneToOne
    private Address address;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private DriverLicense driverLicense;

    private Integer age;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    //@OnDelete(action = OnDeleteAction.CASCADE)
    private Referee referee;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private BankInformation bankInformation;

    @JsonUnwrapped
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private User user;

    @OneToMany()
    private Set<Ride> rides = new HashSet<>();
}
