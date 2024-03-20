package ru.kirill.CloudStorage.configurations;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioClientConfig {
    @Value("${MINIO_ROOT_USER}")
    private String minioUser;

    @Value("${MINIO_ROOT_PASSWORD}")
    private String minioPassword;
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint("http://127.0.0.1:9000")
                .credentials(minioUser, minioPassword)
                .build();
    }
}
