package dean.project.Dride.data.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
    private String createdAt;
    private Boolean isEnabled;
}

//to set uo roles and privileges https://www.baeldung.com/role-and-privilege-for-spring-security-registration


//    @ManyToMany
//    @JoinTable(
//            name = "users_roles",
//            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
//    private Set<Role> roles = new HashSet<>();