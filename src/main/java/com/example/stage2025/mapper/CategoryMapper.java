package com.example.stage2025.mapper;

import com.example.stage2025.dto.CategoryDto;
import com.example.stage2025.entity.Category;

public class CategoryMapper {
    public static CategoryDto toDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setActive(category.isActive());
        return dto;
    }
}