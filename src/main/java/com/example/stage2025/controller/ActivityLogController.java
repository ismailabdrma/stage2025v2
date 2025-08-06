// src/main/java/com/example/stage2025/controller/ActivityLogController.java
package com.example.stage2025.controller;

import com.example.stage2025.dto.ActivityLogDto;
import com.example.stage2025.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity-logs")
@RequiredArgsConstructor
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    // 1. List all logs (admin only)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ActivityLogDto>> getAll() {
        return ResponseEntity.ok(activityLogService.getAllLogs());
    }

    // 2. List logs by user ID
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<List<ActivityLogDto>> getByUser(@PathVariable Long userId) {
        // Optionally, add a check here to allow users to see only their own logs unless admin
        return ResponseEntity.ok(activityLogService.getLogsByUserId(userId));
    }

    // 3. Get log by ID (admin only)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ActivityLogDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(activityLogService.getLogById(id));
    }
}

