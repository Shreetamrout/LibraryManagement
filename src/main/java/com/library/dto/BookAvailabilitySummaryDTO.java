package com.library.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookAvailabilitySummaryDTO {
    private String category;
    private Long availableBooks;
    private Long totalBooks;
}