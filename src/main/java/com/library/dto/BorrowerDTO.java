package com.library.dto;

import com.library.entity.MembershipType;
import lombok.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowerDTO {
    private UUID id;
    private String name;
    private String email;
    private MembershipType membershipType;
    private Integer maxBorrowLimit;
}