package elabbora.backend.specification;

import elabbora.backend.dto.TaskFilterDTO;
import elabbora.backend.model.Task;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {

    public static Specification<Task> filterTasks(TaskFilterDTO filter, String userEmail) {
        return (root, query, cb) -> {
            Predicate predicate = cb.equal(root.get("user").get("email"), userEmail);

            if (filter.getCompleted() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("completed"), filter.getCompleted()));
            }

            if (filter.getStartDate() != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("date"), filter.getStartDate()));
            }

            if (filter.getEndDate() != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("date"), filter.getEndDate()));
            }

            if (filter.getCategory() != null && filter.getCategory().getId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("category").get("id"), filter.getCategory().getId()));
            }

            return predicate;
        };
    }
}
