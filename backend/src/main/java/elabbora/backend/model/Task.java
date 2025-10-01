package elabbora.backend.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {
    // interfaces para controle de atributos nas respostas dos endpoints
    public interface Json {
        public interface Base {}
        public interface WithUser extends Base, User.Json.Base {}
        public interface WithCategory extends Base, TaskCategory.Json.Base {}
        public interface WithAll extends WithUser, WithCategory {}
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonView(Json.Base.class)
    private UUID id;

    @Column
    @Size(max = 100)
    @NotBlank
    @JsonView(Json.Base.class)
    private String title;

    @Column
    @Size(max = 500)
    @JsonView(Json.Base.class)
    private String description;

    @Column
    @NotNull
    @JsonView(Json.Base.class)
    private LocalDate date;

    @Column
    @NotNull
    @JsonView(Json.Base.class)
    private Boolean completed = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonView(Json.WithUser.class)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_category_id", nullable = false)
    @JsonView(Json.WithCategory.class)
    private TaskCategory category;

    public Task loadUser() {
        Hibernate.initialize(user);
        return this;
    }

    public Task loadCategory() {
        Hibernate.initialize(category);
        return this;
    }
}