package com.bir.zyntra.service.storage.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName = "zyntra-images";

    public String uploadFile(InputStream inputStream, String originalFileName, long size, String mimeType) {
        String key = "images/" + UUID.randomUUID() + "_" + originalFileName;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(mimeType)
                .build();

        s3Client.putObject(request, RequestBody.fromInputStream(inputStream, size));

        return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(key)).toExternalForm();
    }

    public String uploadBytes(byte[] bytes, String originalFileName, String mimeType) {
        String key = "images/" + UUID.randomUUID() + "_" + originalFileName;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(mimeType)
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(bytes));

        return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(key)).toExternalForm();
    }
}
