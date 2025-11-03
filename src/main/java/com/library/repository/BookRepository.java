package com.library.repository;

import com.library.dto.BookAvailabilitySummaryDTO;
import com.library.dto.TopBorrowedBookDTO;
import com.library.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {

    Optional<Book> findByTitleAndAuthor(String title, String author);

    Page<Book> findByDeletedFalse(Pageable pageable);

    Page<Book> findByCategoryAndDeletedFalse(String category, Pageable pageable);

    Page<Book> findByIsAvailableTrueAndDeletedFalse(Pageable pageable);

    Page<Book> findByCategoryAndIsAvailableTrueAndDeletedFalse(
            String category, Pageable pageable);

    @Query("SELECT new com.library.dto.BookAvailabilitySummaryDTO(" +
            "b.category, " +
            "SUM(CASE WHEN b.isAvailable = true THEN 1 ELSE 0 END), " +
            "COUNT(b)) " +
            "FROM Book b WHERE b.deleted = false " +
            "GROUP BY b.category")
    List<BookAvailabilitySummaryDTO> getAvailabilitySummary();

    @Query("SELECT new com.library.dto.TopBorrowedBookDTO(" +
            "b.id, b.title, b.author, COUNT(br.id)) " +
            "FROM Book b JOIN BorrowRecord br ON b.id = br.bookId " +
            "WHERE b.deleted = false " +
            "GROUP BY b.id, b.title, b.author " +
            "ORDER BY COUNT(br.id) DESC")
    List<TopBorrowedBookDTO> findTopBorrowedBooks(Pageable pageable);

    List<Book> findByCategoryAndDeletedFalseOrAuthorAndDeletedFalse(
            String category, String author);
}