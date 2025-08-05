// src/main/java/com/example/stage2025/dto/DashboardStatsDto.java
package com.example.stage2025.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardStatsDto {
    private long userCount;
    private long productCount;
    private long orderCount;
    private long supplierCount;
    private long paymentCount;
    private Double totalSales; // Or BigDecimal if you prefer
}

