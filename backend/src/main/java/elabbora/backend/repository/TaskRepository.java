package elabbora.backend.repository;

import elabbora.backend.model.Task;
import elabbora.backend.model.TaskCategory;
import elabbora.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task> {
    @Query("SELECT t FROM Task t WHERE t.user = :user " +
            "AND (:completed IS NULL OR t.completed = :completed) " +
            "AND (:startDate IS NULL OR t.date >= :startDate) " +
            "AND (:endDate IS NULL OR t.date <= :endDate) " +
            "AND (:category IS NULL OR t.category = :category)")
    Page<Task> findFilteredTasks(
            @Param("user") User user,
            @Param("completed") Boolean completed,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("category") TaskCategory category,
            Pageable pageable
    );
}
