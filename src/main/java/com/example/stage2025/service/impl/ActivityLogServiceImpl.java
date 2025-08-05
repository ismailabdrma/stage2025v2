package com.example.stage2025.service.impl;



import com.example.stage2025.dto.ActivityLogDto;
import com.example.stage2025.entity.ActivityLog;
import com.example.stage2025.mapper.ActivityLogMapper;
import com.example.stage2025.repository.ActivityLogRepository;
import com.example.stage2025.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    @Override
    public List<ActivityLogDto> getAllLogs() {
        return activityLogRepository.findAll()
                .stream()
                .map(ActivityLogMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ActivityLogDto> getLogsByUserId(Long userId) {
        return activityLogRepository.findByUserId(userId)
                .stream()
                .map(ActivityLogMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ActivityLogDto getLogById(Long id) {
        ActivityLog log = activityLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Log non trouvé"));
        return ActivityLogMapper.toDto(log);
    }

    @Override
    public ActivityLogDto save(ActivityLogDto dto) {
        ActivityLog log = ActivityLog.builder()
                .userId(dto.getUserId())
                .userEmail(dto.getUserEmail())
                .userType(dto.getUserType())
                .action(dto.getAction())
                .entity(dto.getEntity())
                .entityId(dto.getEntityId())
                .details(dto.getDetails())
                .actionDate(LocalDateTime.now())
                .build();

        return ActivityLogMapper.toDto(activityLogRepository.save(log));
    }
}
