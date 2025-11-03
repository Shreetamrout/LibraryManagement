package com.library.controller;

import com.library.dto.BorrowerDTO;
import com.library.dto.BorrowRecordDTO;
import com.library.service.BorrowerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/borrowers")
@RequiredArgsConstructor
public class BorrowerController {

    private final BorrowerService borrowerService;

    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    @PostMapping
    public ResponseEntity<BorrowerDTO> registerBorrower(
            @Valid @RequestBody BorrowerDTO borrowerDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(borrowerService.registerBorrower(borrowerDTO));
    }

    @GetMapping("/{id}/records")
    public ResponseEntity<List<BorrowRecordDTO>> getBorrowerRecords(
            @PathVariable UUID id) {
        return ResponseEntity.ok(borrowerService.getBorrowerRecords(id));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<BorrowerDTO>> getOverdueBorrowers() {
        return ResponseEntity.ok(borrowerService.getOverdueBorrowers());
    }
}