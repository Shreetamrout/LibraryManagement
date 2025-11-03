package com.library.controller;

import com.library.dto.BorrowerActivityDTO;
import com.library.dto.TopBorrowedBookDTO;
import com.library.service.AnalyticsService;
import com.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final BookService bookService;

    public AnalyticsController(AnalyticsService analyticsService, BookService bookService) {
        this.analyticsService = analyticsService;
        this.bookService = bookService;
    }

    @GetMapping("/top-borrowed-books")
    public ResponseEntity<List<TopBorrowedBookDTO>> getTopBorrowedBooks() {
        return ResponseEntity.ok(bookService.getTopBorrowedBooks());
    }

    @GetMapping("/borrower-activity")
    public ResponseEntity<List<BorrowerActivityDTO>> getBorrowerActivity() {
        return ResponseEntity.ok(analyticsService.getBorrowerActivity());
    }
}