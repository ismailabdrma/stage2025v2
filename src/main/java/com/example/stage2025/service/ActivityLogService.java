package com.example.stage2025.service;



import com.example.stage2025.dto.ActivityLogDto;

import java.util.List;

public interface ActivityLogService {
    List<ActivityLogDto> getAllLogs();
    List<ActivityLogDto> getLogsByUserId(Long userId);
    ActivityLogDto getLogById(Long id);
    ActivityLogDto save(ActivityLogDto dto);
}

