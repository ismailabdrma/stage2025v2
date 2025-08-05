package com.example.stage2025.utils;

import com.example.stage2025.entity.ActivityLog;

import java.time.LocalDateTime;

public class LogUtils {

    public static ActivityLog buildLog(
            String action,
            String entity,
            Long entityId,
            String email,
            String type,
            Long id,
            String details
    ) {
        return ActivityLog.builder()
                .action(action)
                .entity(entity)
                .entityId(entityId)
                .userEmail(email)
                .userType(type)
                .userId(id)
                .details(details)
                .actionDate(LocalDateTime.now())
                .build();
    }
}
