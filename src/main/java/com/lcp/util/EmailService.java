package com.lcp.util;

import com.lcp.common.dto.EmailBaseDto;

public interface EmailService {
    void send(EmailBaseDto dto);
}
