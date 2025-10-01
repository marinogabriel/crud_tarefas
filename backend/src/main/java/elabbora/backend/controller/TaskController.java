package elabbora.backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import elabbora.backend.dto.PageResponseDTO;
import elabbora.backend.dto.TaskFilterDTO;
import elabbora.backend.model.Task;
import elabbora.backend.model.TaskCategory;
import elabbora.backend.model.User;
import elabbora.backend.service.AuthService;
import elabbora.backend.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    @JsonView(Task.Json.WithCategory.class)
    public PageResponseDTO<Task> getTasksForCurrentUser(Authentication authentication, TaskFilterDTO filter, Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        String email = user.getEmail();

        return new PageResponseDTO<>(
                taskService.getTasksByUserAndFilter(email, filter, pageable)
                    .map(Task::loadCategory));
    }

    @GetMapping("/{id}")
    @JsonView(Task.Json.WithCategory.class)
    public ResponseEntity<Task> getById(@PathVariable UUID id) {
        return taskService.getById(id)
                .map(task -> ResponseEntity.ok(task
                        .loadCategory()))
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    @JsonView(Task.Json.WithAll.class)
    public ResponseEntity<Task> create(@RequestBody Task task) {
        Task savedTask = taskService.create(task);
        return ResponseEntity.ok(savedTask
                .loadCategory());
    }

    @PutMapping("/{id}")
    @JsonView(Task.Json.WithCategory.class)
    public ResponseEntity<Task> update(@PathVariable UUID id, @RequestBody Task taskCategory) {
        return ResponseEntity.ok(taskService.update(id, taskCategory)
                .loadCategory());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
