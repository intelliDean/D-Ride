package dean.project.Dride.data.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.util.Set;

import static dean.project.Dride.utilities.UserUrls.USERS;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = USERS)
public class User {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String password;
    @Column(unique = true, updatable = true)
    private String email;
    private String profileImage;
    private Set<Role> roles;
    private String createdAt;
    private Boolean isEnabled;

}
//@Formula("(CASE WHEN is_enabled = 1 THEN 'Yes' ELSE 'No' END)")
//    private String enabled;
//    @Column(name = "is_enabled")