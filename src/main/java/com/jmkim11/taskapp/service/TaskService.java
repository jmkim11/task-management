package com.jmkim11.taskapp.service;

import com.jmkim11.taskapp.domain.AuditLog;
import com.jmkim11.taskapp.domain.Task;
import com.jmkim11.taskapp.domain.User;
import com.jmkim11.taskapp.dto.TaskRequest;
import com.jmkim11.taskapp.dto.TaskResponse;
import com.jmkim11.taskapp.repository.AuditLogRepository;
import com.jmkim11.taskapp.repository.TaskRepository;
import com.jmkim11.taskapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private void logAction(String action, Long targetId, String username) {
        auditLogRepository.save(AuditLog.builder()
                .action(action)
                .username(username)
                .targetTaskId(targetId)
                .build());
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        User user = getCurrentUser();
        // Admins can see all tasks, but let's stick to standard flow: User sees own
        // tasks
        // Or if Admin, maybe sees all?
        // User requested: "관리자 페이지에서는... 로그 표시"
        // Let's implement: User sees OWN tasks. Admin sees audits.

        return taskRepository.findByUser(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        User user = getCurrentUser();
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .completed(false)
                .user(user)
                .build();

        Task savedTask = taskRepository.save(task);
        logAction("CREATE", savedTask.getId(), user.getUsername());
        return mapToResponse(savedTask);
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = getCurrentUser();
        if (!task.getUser().getId().equals(user.getId()) && user.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Not authorized to update this task");
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(request.getCompleted());

        logAction("UPDATE", task.getId(), user.getUsername());
        return mapToResponse(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = getCurrentUser();
        if (!task.getUser().getId().equals(user.getId()) && user.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Not authorized to delete this task");
        }

        taskRepository.delete(task);
        logAction("DELETE", id, user.getUsername());
    }

    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(task.isCompleted())
                .username(task.getUser().getUsername())
                .build();
    }
}
