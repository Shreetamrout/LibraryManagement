package com.library.controller;

import com.library.dto.BorrowRecordDTO;
import com.library.dto.BorrowRequest;
import com.library.dto.ReturnRequest;
import com.library.service.BorrowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping("/borrow")
    public ResponseEntity<BorrowRecordDTO> borrowBook(
            @Valid @RequestBody BorrowRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(borrowService.borrowBook(request));
    }

    @PostMapping("/return")
    public ResponseEntity<BorrowRecordDTO> returnBook(
            @Valid @RequestBody ReturnRequest request) {
        return ResponseEntity.ok(borrowService.returnBook(request));
    }

    @GetMapping("/records/active")
    public ResponseEntity<List<BorrowRecordDTO>> getActiveRecords() {
        return ResponseEntity.ok(borrowService.getActiveRecords());
    }
}