package elabbora.backend.controller;

import elabbora.backend.model.TaskCategory;
import elabbora.backend.service.TaskCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/task-categories")
@RequiredArgsConstructor
public class TaskCategoryController {

    private final TaskCategoryService taskCategoryService;

    @GetMapping
    public List<TaskCategory> getAll() {
        return taskCategoryService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskCategory> getById(@PathVariable UUID id) {
        return taskCategoryService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TaskCategory> create(@RequestBody TaskCategory taskCategory) {
        return ResponseEntity.ok(taskCategoryService.create(taskCategory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskCategory> update(@PathVariable UUID id, @RequestBody TaskCategory taskCategory) {
        return ResponseEntity.ok(taskCategoryService.update(id, taskCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        taskCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
