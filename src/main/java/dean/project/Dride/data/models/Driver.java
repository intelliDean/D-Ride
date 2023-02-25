package dean.project.Dride.data.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String phoneNumber;
    @OneToOne
    private Address address;
//    @Transient used to be MultipartFile
    private String profileImage;
    private String licenseImage;
    private String licenseId;
    private int age;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private BankInformation bankInformation;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Referee referee;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Details details;
}
