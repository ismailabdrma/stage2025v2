package com.example.stage2025.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ActivityLogDto {
    private Long id;
    private Long userId;
    private String userEmail;
    private String userType;
    private String action;
    private String entity;
    private Long entityId;
    private String details;
    private LocalDateTime actionDate;
}
