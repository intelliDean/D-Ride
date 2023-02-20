package dean.project.Dride.data.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Details {
    @JsonIgnore // it will not be rendered
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;        //an entity in the database needs a primary key i.e id
    private String name;
    private String password;
    private String email;
    @Transient
    private MultipartFile profileImage;
    private String registeredAt;
}
