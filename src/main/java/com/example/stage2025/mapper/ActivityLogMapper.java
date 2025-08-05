package com.example.stage2025.mapper;

import com.example.stage2025.dto.ActivityLogDto;
import com.example.stage2025.entity.ActivityLog;

public class ActivityLogMapper {
    public static ActivityLogDto toDto(ActivityLog log) {
        ActivityLogDto dto = new ActivityLogDto();
        dto.setId(log.getId());
        dto.setUserId(log.getUserId());
        dto.setUserEmail(log.getUserEmail());
        dto.setUserType(log.getUserType());
        dto.setAction(log.getAction());
        dto.setEntity(log.getEntity());
        dto.setEntityId(log.getEntityId());
        dto.setDetails(log.getDetails());
        dto.setActionDate(log.getActionDate());
        return dto;
    }
}

