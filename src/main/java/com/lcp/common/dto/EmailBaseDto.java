package com.lcp.common.dto;

import com.lcp.common.enumeration.ContentType;
import lombok.Data;

import java.util.List;

@Data
public class EmailBaseDto {
    private String subject;
    private String body;
    private ContentType contentType;
    private List<String> toEmails;
    private List<String> bccEmails;
    private List<AttachmentDto> attachments;
}
