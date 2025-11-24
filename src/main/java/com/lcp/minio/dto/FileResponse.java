package com.lcp.minio.dto;

import lombok.Getter;
import lombok.Setter;

// Response models
@Getter
@Setter
public class FileResponse {
    private String fileName;
    private String fileUrl;
    private String originalName;
    private String fileType;
}