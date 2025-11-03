package com.library.dto;

import lombok.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopBorrowedBookDTO {
    private UUID bookId;
    private String title;
    private String author;
    private Long borrowCount;
}