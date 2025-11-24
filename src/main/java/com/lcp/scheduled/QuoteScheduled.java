package com.lcp.scheduled;

import com.lcp.quote.repository.QuoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Log4j2
public class QuoteScheduled {
    private final QuoteRepository quoteRepository;

    @Scheduled(cron = "0 45 23  * * ?", zone = "Asia/Ho_Chi_Minh")
    @Transactional
    public void overdueInvoice() {
        log.info("Updating payment status to overdue");

        log.info("End updating payment status to overdue");
    }
}
