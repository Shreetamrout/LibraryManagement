package com.library.dto;

import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRequest {
    private UUID bookId;
    private UUID borrowerId;
}