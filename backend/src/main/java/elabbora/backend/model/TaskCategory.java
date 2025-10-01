package elabbora.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

// todo: unicidade por nome?
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task_categories")
public class TaskCategory {
    public interface Json {
        public interface Base {}
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonView(Json.Base.class)
    private UUID id;

    @Column
    @JsonView(Json.Base.class)
    private String name;
}