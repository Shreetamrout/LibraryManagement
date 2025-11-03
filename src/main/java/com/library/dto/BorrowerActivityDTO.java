package com.library.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowerActivityDTO {
    private UUID borrowerId;
    private String borrowerName;
    private Long totalBorrowed;
    private Long overdueCount;
    private BigDecimal totalFines;
}