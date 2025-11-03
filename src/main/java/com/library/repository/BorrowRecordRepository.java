package com.library.repository;

import com.library.dto.BorrowerActivityDTO;
import com.library.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, UUID> {

    List<BorrowRecord> findByBorrowerId(UUID borrowerId);

    @Query("SELECT COUNT(br) FROM BorrowRecord br " +
            "WHERE br.borrowerId = :borrowerId AND br.returnDate IS NULL")
    Long countActiveBorrowsByBorrowerId(@Param("borrowerId") UUID borrowerId);

    @Query("SELECT br FROM BorrowRecord br " +
            "WHERE br.returnDate IS NULL")
    List<BorrowRecord> findAllActive();

    @Query("SELECT br FROM BorrowRecord br " +
            "WHERE br.dueDate < :today AND br.returnDate IS NULL")
    List<BorrowRecord> findOverdueRecords(@Param("today") LocalDate today);

    @Query("SELECT new com.library.dto.BorrowerActivityDTO(" +
            "b.id, b.name, " +
            "COUNT(br.id), " +
            "SUM(CASE WHEN br.dueDate < :today AND br.returnDate IS NULL THEN 1 ELSE 0 END), " +
            "COALESCE(SUM(br.fineAmount), 0)) " +
            "FROM Borrower b LEFT JOIN BorrowRecord br ON b.id = br.borrowerId " +
            "GROUP BY b.id, b.name")
    List<BorrowerActivityDTO> getBorrowerActivity(@Param("today") LocalDate today);
}