package com.library.service;

import com.library.dto.BorrowerDTO;
import com.library.dto.BorrowRecordDTO;
import com.library.entity.Borrower;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.BorrowerRepository;
import com.library.repository.BorrowRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    public BorrowerService(BorrowerRepository borrowerRepository, BorrowRecordRepository borrowRecordRepository) {
        this.borrowerRepository = borrowerRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    @Transactional
    public BorrowerDTO registerBorrower(BorrowerDTO borrowerDTO) {
        Borrower borrower = Borrower.builder()
                .name(borrowerDTO.getName())
                .email(borrowerDTO.getEmail())
                .membershipType(borrowerDTO.getMembershipType())
                .build();

        return toDTO(borrowerRepository.save(borrower));
    }

    public List<BorrowRecordDTO> getBorrowerRecords(UUID borrowerId) {
        if (!borrowerRepository.existsById(borrowerId)) {
            throw new ResourceNotFoundException("Borrower not found");
        }

        return borrowRecordRepository.findByBorrowerId(borrowerId)
                .stream()
                .map(this::toRecordDTO)
                .collect(Collectors.toList());
    }

    public List<BorrowerDTO> getOverdueBorrowers() {
        var overdueRecords = borrowRecordRepository.findOverdueRecords(LocalDate.now());

        return overdueRecords.stream()
                .map(record -> record.getBorrower())
                .distinct()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private BorrowerDTO toDTO(Borrower borrower) {
        return BorrowerDTO.builder()
                .id(borrower.getId())
                .name(borrower.getName())
                .email(borrower.getEmail())
                .membershipType(borrower.getMembershipType())
                .maxBorrowLimit(borrower.getMaxBorrowLimit())
                .build();
    }

    private BorrowRecordDTO toRecordDTO(com.library.entity.BorrowRecord record) {
        return BorrowRecordDTO.builder()
                .id(record.getId())
                .bookId(record.getBookId())
                .bookTitle(record.getBook() != null ? record.getBook().getTitle() : null)
                .borrowerId(record.getBorrowerId())
                .borrowerName(record.getBorrower() != null ? record.getBorrower().getName() : null)
                .borrowDate(record.getBorrowDate())
                .dueDate(record.getDueDate())
                .returnDate(record.getReturnDate())
                .fineAmount(record.getFineAmount())
                .build();
    }
}