package dean.project.Dride.data.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String phoneNumber;
    @OneToOne
    private Address address;
    @Transient
    private MultipartFile profileImage;
    private String licenseId;
    private int age;

    private Gender gender;
    @OneToOne
    private BankInformation bankInformation;
    @OneToOne
    private Referee referee;

}
