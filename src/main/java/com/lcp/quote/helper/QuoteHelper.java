package com.lcp.quote.helper;

import com.lcp.quote.repository.QuoteRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class QuoteHelper {
    private static final String QUOTE_CODE_FORMAT = "%s%06d";
    private static final String QUOTE_CODE_PREFIX = "Q";
    private final QuoteRepository quoteRepository;

    public String genQuoteCode() {
        long count = quoteRepository.count();
        return String.format(QUOTE_CODE_FORMAT, QUOTE_CODE_PREFIX, ++count);
    }
}
