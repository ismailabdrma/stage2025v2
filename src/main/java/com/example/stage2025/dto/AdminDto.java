package com.example.stage2025.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AdminDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;


}