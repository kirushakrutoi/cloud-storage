package ru.kirill.CloudStorage.services;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@AllArgsConstructor
public class MinioService {

    private final MinioClient minioClient;

    public void store(MultipartFile file) {
        System.out.println(file.getName());
        try {
            minioClient.putObject(
                    PutObjectArgs.builder().bucket("user-files").object(file.getOriginalFilename()).stream(
                                    file.getInputStream(), file.getSize(), -1)
                            .build());
        } catch (ServerException | InternalException | XmlParserException | InvalidResponseException |
                 InvalidKeyException | NoSuchAlgorithmException | IOException | ErrorResponseException |
                 InsufficientDataException e) {
            throw new RuntimeException(e);
        }


    }
}
