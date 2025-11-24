package com.lcp.minio.service;

import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Service
public class MinioService {
    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String defaultBucketName;

    @PostConstruct
    public void init() {
        try {
            ensureBucketExists(defaultBucketName);
        } catch (Exception e) {
            throw new RuntimeException("Error initializing default MinIO bucket", e);
        }
    }

    /**
     * Ensures that a bucket with the given name exists
     *
     * @param bucketName name of the bucket to check/create
     */
    public void ensureBucketExists(String bucketName) {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating or checking bucket: " + bucketName, e);
        }
    }

    /**
     * Upload file to the default bucket
     */
    public String uploadFile(MultipartFile file) {
        return uploadFile(file, defaultBucketName);
    }

    /**
     * Upload file to a specific bucket
     */
    public String uploadFile(MultipartFile file, String bucketName) {
        try {
            // Ensure bucket exists
            ensureBucketExists(bucketName);

            String fileName = System.currentTimeMillis() + "-" + java.util.UUID.randomUUID() + "_" + file.getOriginalFilename();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return bucketName + "/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file to MinIO bucket: " + bucketName, e);
        }
    }

    /**
     * Get file from the default bucket
     */
    public InputStream getFile(String fileName) {
        return getFile(fileName, defaultBucketName);
    }

    /**
     * Get file from a specific bucket
     */
    public InputStream getFile(String fileName, String bucketName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error getting file from MinIO bucket: " + bucketName, e);
        }
    }

    /**
     * Get pre-signed URL from the default bucket
     */
    public String getPreSignedUrl(String fileName) {
        return getPreSignedUrl(fileName, defaultBucketName);
    }

    /**
     * Get pre-signed URL from a specific bucket
     */
    public String getPreSignedUrl(String fileName, String bucketName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(fileName)
                            .expiry(60, TimeUnit.MINUTES)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error generating pre-signed URL from bucket: " + bucketName, e);
        }
    }

    /**
     * Delete file from the default bucket
     */
    public void deleteFile(String fileName) {
        deleteFile(fileName, defaultBucketName);
    }

    /**
     * Delete file from a specific bucket
     */
    public void deleteFile(String fileName, String bucketName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error deleting file from MinIO bucket: " + bucketName, e);
        }
    }
}