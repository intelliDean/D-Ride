package dean.project.Dride.data.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Users {
    @JsonIgnore // it will not be rendered
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;        //an entity in the database needs a primary key i.e id
    private String name;
    private String password;
    @Column(unique = true)
    private String email;
    private String profileImage;
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
    private String createdAt;
    private Boolean isEnabled;
}
