package com.lcp.minio.controller;

import com.lcp.minio.dto.UrlResponse;
import com.lcp.minio.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private MinioService minioService;

    /**
     * Upload file to the default bucket
     */
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        return minioService.uploadFile(file);
    }

    /**
     * Upload file to a specific bucket
     */
    @PostMapping("/upload/bucket/{bucketName}")
    public String uploadFileToBucket(
            @RequestParam("file") MultipartFile file,
            @PathVariable String bucketName) {
        return minioService.uploadFile(file, bucketName);
    }

    /**
     * Download file from the default bucket
     */
    @GetMapping("/download/{fileName}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileName) {
        InputStream inputStream = minioService.getFile(fileName);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(inputStream));
    }

    /**
     * Download file from a specific bucket
     */
    @GetMapping("/download/{bucketName}/{fileName}")
    public ResponseEntity<InputStreamResource> downloadFileFromBucket(
            @PathVariable String bucketName,
            @PathVariable String fileName) {
        InputStream inputStream = minioService.getFile(fileName, bucketName);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(inputStream));
    }

    /**
     * Get pre-signed URL for a file in the default bucket
     */
    @GetMapping("/url/{fileName}")
    public ResponseEntity<UrlResponse> getFileUrl(@PathVariable String fileName) {
        String url = minioService.getPreSignedUrl(fileName);

        UrlResponse response = new UrlResponse();
        response.setUrl(url);

        return ResponseEntity.ok(response);
    }

    /**
     * Get pre-signed URL for a file in a specific bucket
     */
    @GetMapping("/url/{bucketName}/{fileName}")
    public ResponseEntity<UrlResponse> getFileUrlFromBucket(
            @PathVariable String bucketName,
            @PathVariable String fileName) {
        String url = minioService.getPreSignedUrl(fileName, bucketName);

        UrlResponse response = new UrlResponse();
        response.setUrl(url);

        return ResponseEntity.ok(response);
    }

    /**
     * Delete file from the default bucket
     */
    @DeleteMapping("/{fileName}")
    public ResponseEntity<Void> deleteFile(@PathVariable String fileName) {
        minioService.deleteFile(fileName);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete file from a specific bucket
     */
    @DeleteMapping("/{bucketName}/{fileName}")
    public ResponseEntity<Void> deleteFileFromBucket(
            @PathVariable String bucketName,
            @PathVariable String fileName) {
        minioService.deleteFile(fileName, bucketName);
        return ResponseEntity.ok().build();
    }

    /**
     * Create a new bucket
     */
    @PostMapping("/bucket/{bucketName}")
    public ResponseEntity<Void> createBucket(@PathVariable String bucketName) {
        minioService.ensureBucketExists(bucketName);
        return ResponseEntity.ok().build();
    }
}