package elabbora.backend.service;

import elabbora.backend.model.TaskCategory;
import elabbora.backend.repository.TaskCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskCategoryService {

    private final TaskCategoryRepository repository;

    @Autowired
    public TaskCategoryService(TaskCategoryRepository repository) {
        this.repository = repository;
    }

    public List<TaskCategory> getAll() {
        return repository.findAll();
    }

    public Optional<TaskCategory> getById(UUID id) {
        return repository.findById(id);
    }

    public TaskCategory create(TaskCategory taskCategory) {
        return repository.save(taskCategory);
    }

    public TaskCategory update(UUID id, TaskCategory updated) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("TaskCategory not found"));
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("TaskCategory not found");
        }
        repository.deleteById(id);
    }
}
