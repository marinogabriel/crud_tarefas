package elabbora.backend.dto;

import elabbora.backend.model.TaskCategory;
import elabbora.backend.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskFilterDTO {
    private Boolean completed;
    private LocalDate startDate;
    private LocalDate endDate;
    private TaskCategory category;
    private User user;
}
