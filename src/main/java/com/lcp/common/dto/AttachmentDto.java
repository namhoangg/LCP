package com.lcp.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AttachmentDto {
    private String fileName;
    private byte[] data;
}
