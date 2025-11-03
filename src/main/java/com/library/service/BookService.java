package com.library.service;

import com.library.dto.BookDTO;
import com.library.dto.BookAvailabilitySummaryDTO;
import com.library.dto.TopBorrowedBookDTO;
import com.library.entity.Book;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    public BookService(BookRepository bookRepository, BorrowRecordRepository borrowRecordRepository) {
        this.bookRepository = bookRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    @Transactional
    public BookDTO addBook(BookDTO bookDTO) {
        var existingBook = bookRepository.findByTitleAndAuthor(
                bookDTO.getTitle(), bookDTO.getAuthor());

        if (existingBook.isPresent()) {
            Book book = existingBook.get();
            book.setTotalCopies(book.getTotalCopies() + bookDTO.getTotalCopies());
            book.setAvailableCopies(book.getAvailableCopies() + bookDTO.getTotalCopies());
            return toDTO(bookRepository.save(book));
        }

        Book book = Book.builder()
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .category(bookDTO.getCategory())
                .totalCopies(bookDTO.getTotalCopies())
                .availableCopies(bookDTO.getTotalCopies())
                .isAvailable(true)
                .deleted(false)
                .build();

        return toDTO(bookRepository.save(book));
    }

    public Page<BookDTO> getBooks(String category, Boolean available,
                                  Pageable pageable) {
        Page<Book> books;

        if (category != null && available != null && available) {
            books = bookRepository.findByCategoryAndIsAvailableTrueAndDeletedFalse(
                    category, pageable);
        } else if (category != null) {
            books = bookRepository.findByCategoryAndDeletedFalse(category, pageable);
        } else if (available != null && available) {
            books = bookRepository.findByIsAvailableTrueAndDeletedFalse(pageable);
        } else {
            books = bookRepository.findByDeletedFalse(pageable);
        }

        return books.map(this::toDTO);
    }

    @Transactional
    public BookDTO updateBook(UUID id, BookDTO bookDTO) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (bookDTO.getTitle() != null) book.setTitle(bookDTO.getTitle());
        if (bookDTO.getAuthor() != null) book.setAuthor(bookDTO.getAuthor());
        if (bookDTO.getCategory() != null) book.setCategory(bookDTO.getCategory());
        if (bookDTO.getTotalCopies() != null) {
            int diff = bookDTO.getTotalCopies() - book.getTotalCopies();
            book.setTotalCopies(bookDTO.getTotalCopies());
            book.setAvailableCopies(book.getAvailableCopies() + diff);
        }

        return toDTO(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        Long activeRecords = borrowRecordRepository.countActiveBorrowsByBorrowerId(id);
        if (activeRecords > 0) {
            throw new IllegalStateException("Cannot delete book with active borrow records");
        }

        book.setDeleted(true);
        bookRepository.save(book);
    }

    public List<BookDTO> getSimilarBooks(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        return bookRepository.findByCategoryAndDeletedFalseOrAuthorAndDeletedFalse(
                        book.getCategory(), book.getAuthor())
                .stream()
                .filter(b -> !b.getId().equals(id))
                .limit(5)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<BookAvailabilitySummaryDTO> getAvailabilitySummary() {
        return bookRepository.getAvailabilitySummary();
    }

    public List<TopBorrowedBookDTO> getTopBorrowedBooks() {
        return bookRepository.findTopBorrowedBooks(PageRequest.of(0, 5));
    }

    private BookDTO toDTO(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .category(book.getCategory())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .isAvailable(book.getIsAvailable())
                .build();
    }
}