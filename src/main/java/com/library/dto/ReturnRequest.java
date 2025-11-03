package com.library.dto;

import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRequest {
    private UUID recordId;
}