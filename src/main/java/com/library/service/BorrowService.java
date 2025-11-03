package com.library.service;

import com.library.dto.BorrowRecordDTO;
import com.library.dto.BorrowRequest;
import com.library.dto.ReturnRequest;
import com.library.entity.Book;
import com.library.entity.BorrowRecord;
import com.library.entity.Borrower;
import com.library.entity.FinePolicy;
import com.library.exception.BusinessException;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.BorrowerRepository;
import com.library.repository.FinePolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;
    private final FinePolicyRepository finePolicyRepository;

    private static final BigDecimal DEFAULT_FINE_PER_DAY = new BigDecimal("1.00");
    private static final int BORROW_PERIOD_DAYS = 14;

    public BorrowService(BorrowRecordRepository borrowRecordRepository, BookRepository bookRepository, BorrowerRepository borrowerRepository, FinePolicyRepository finePolicyRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.bookRepository = bookRepository;
        this.borrowerRepository = borrowerRepository;
        this.finePolicyRepository = finePolicyRepository;
    }

    @Transactional
    public BorrowRecordDTO borrowBook(BorrowRequest request) {
        Borrower borrower = borrowerRepository.findById(request.getBorrowerId())
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found"));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        // Validate borrow limit
        Long activeCount = borrowRecordRepository.countActiveBorrowsByBorrowerId(
                borrower.getId());
        if (activeCount >= borrower.getMaxBorrowLimit()) {
            throw new BusinessException("Borrower has reached maximum borrow limit");
        }

        // Validate book availability
        if (book.getAvailableCopies() < 1) {
            throw new BusinessException("No copies available for this book");
        }

        // Create borrow record
        LocalDate borrowDate = LocalDate.now();
        BorrowRecord record = BorrowRecord.builder()
                .bookId(book.getId())
                .borrowerId(borrower.getId())
                .borrowDate(borrowDate)
                .dueDate(borrowDate.plusDays(BORROW_PERIOD_DAYS))
                .build();

        // Update book availability
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        return toDTO(borrowRecordRepository.save(record));
    }

    @Transactional
    public BorrowRecordDTO returnBook(ReturnRequest request) {
        BorrowRecord record = borrowRecordRepository.findById(request.getRecordId())
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found"));

        if (record.getReturnDate() != null) {
            throw new BusinessException("Book already returned");
        }

        Book book = bookRepository.findById(record.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        LocalDate returnDate = LocalDate.now();
        record.setReturnDate(returnDate);

        // Calculate fine if overdue
        if (returnDate.isAfter(record.getDueDate())) {
            long daysLate = ChronoUnit.DAYS.between(record.getDueDate(), returnDate);
            BigDecimal finePerDay = getFinePerDay(book.getCategory());
            record.setFineAmount(finePerDay.multiply(BigDecimal.valueOf(daysLate)));
        }

        // Increase available copies
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return toDTO(borrowRecordRepository.save(record));
    }

    public List<BorrowRecordDTO> getActiveRecords() {
        return borrowRecordRepository.findAllActive()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private BigDecimal getFinePerDay(String category) {
        return finePolicyRepository.findByCategory(category)
                .map(FinePolicy::getFinePerDay)
                .orElse(DEFAULT_FINE_PER_DAY);
    }

    private BorrowRecordDTO toDTO(BorrowRecord record) {
        return BorrowRecordDTO.builder()
                .id(record.getId())
                .bookId(record.getBookId())
                .bookTitle(record.getBook() != null ? record.getBook().getTitle() : null)
                .borrowerId(record.getBorrowerId())
                .borrowerName(record.getBorrower() != null ?
                        record.getBorrower().getName() : null)
                .borrowDate(record.getBorrowDate())
                .dueDate(record.getDueDate())
                .returnDate(record.getReturnDate())
                .fineAmount(record.getFineAmount())
                .build();
    }
}