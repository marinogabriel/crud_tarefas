package elabbora.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    public interface Json {
        public interface Base {}
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonView(Json.Base.class)
    private UUID id;

    @Column(nullable = false, unique = true)
    @JsonView(Json.Base.class)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name")
    @JsonView(Json.Base.class)
    private String firstName;

    @Column(name = "last_name")
    @JsonView(Json.Base.class)
    private String lastName;

    public User(UUID id, String email, String firstName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
    }
}
