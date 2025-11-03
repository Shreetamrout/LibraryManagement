package com.library.config;

import com.library.repository.BorrowRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {

    private final BorrowRecordRepository borrowRecordRepository;

    public ScheduledTasks(BorrowRecordRepository borrowRecordRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
    }
    Logger log = null;
    /**
     * Run daily at midnight to check for overdue records
     */
    @Scheduled(cron = "0 0 0 * * ?")  // Every day at midnight
    public void checkOverdueRecords() {

        log.info("Running scheduled task: Checking overdue records");

        var overdueRecords = borrowRecordRepository.findOverdueRecords(LocalDate.now());

        log.info("Found {} overdue records", overdueRecords.size());

        // You can add logic here to:
        // - Send email notifications
        // - Update borrower status
        // - Generate reports
        // - etc.
    }

    /**
     * Run every hour to log system stats
     */
    @Scheduled(fixedRate = 3600000)  // Every hour
    public void logSystemStats() {
        long activeRecords = borrowRecordRepository.findAllActive().size();
        log.info("System Stats - Active borrow records: {}", activeRecords);
    }
}