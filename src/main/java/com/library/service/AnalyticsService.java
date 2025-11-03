package com.library.service;

import com.library.dto.BorrowerActivityDTO;
import com.library.repository.BorrowRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final BorrowRecordRepository borrowRecordRepository;

    public AnalyticsService(BorrowRecordRepository borrowRecordRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
    }

    public List<BorrowerActivityDTO> getBorrowerActivity() {
        return borrowRecordRepository.getBorrowerActivity(LocalDate.now());
    }
}