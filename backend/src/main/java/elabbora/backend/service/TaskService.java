package elabbora.backend.service;

import elabbora.backend.dto.TaskFilterDTO;
import elabbora.backend.model.Task;
import elabbora.backend.model.TaskCategory;
import elabbora.backend.model.User;
import elabbora.backend.repository.TaskCategoryRepository;
import elabbora.backend.repository.TaskRepository;
import elabbora.backend.specification.TaskSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository repository;
    private final TaskCategoryRepository taskCategoryRepository;

    @Autowired
    public TaskService(TaskRepository repository, TaskCategoryRepository taskCategoryRepository) {
        this.repository = repository;
        this.taskCategoryRepository = taskCategoryRepository;
    }

    public List<Task> getAll() {
        return repository.findAll();
    }


    public Page<Task> getTasksByUserAndFilter(String email, TaskFilterDTO filter, Pageable pageable) {
        Specification<Task> spec = TaskSpecification.filterTasks(filter, email);
        return repository.findAll(spec, pageable);
    }

    public Page<Task> getFilteredTasks(
            User user,
            Boolean completed,
            LocalDate startDate,
            LocalDate endDate,
            TaskCategory category,
            Pageable pageable
    ) {
        return repository.findFilteredTasks(user, completed, startDate, endDate, category, pageable);
    }

    public Optional<Task> getById(UUID id) {
        return repository.findById(id);
    }

    public Task create(Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        task.setUser(user);

        TaskCategory category = taskCategoryRepository.findById(task.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        task.setCategory(category);

        return repository.save(task);
    }

    public Task update(UUID id, Task updated) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setTitle(updated.getTitle());
                    existing.setDescription(updated.getDescription());
                    existing.setDate(updated.getDate());
                    existing.setCompleted(updated.getCompleted());
                    existing.setCategory(updated.getCategory());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        repository.deleteById(id);
    }
}
