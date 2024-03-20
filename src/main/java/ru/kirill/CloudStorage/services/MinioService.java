package ru.kirill.CloudStorage.services;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.AllArgsConstructor;
import okio.Path;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kirill.CloudStorage.models.User;
import ru.kirill.CloudStorage.repositories.MinioRepository;
import ru.kirill.CloudStorage.services.interfaces.StorageService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class MinioService implements StorageService {

    private final MinioRepository minioRepository;

    public List<String> loadAll(User user, String path){
        return minioRepository.loadAll(user, path);
    }

    public void store(MultipartFile file, User user, String path) {
        minioRepository.store(file, user, path);
    }

    public void uploadFolder(List<MultipartFile> files, User user, String path){
        minioRepository.uploadFolder(files, user, path);
    }

    public void storeDir(String dirName, User user, String path) {
        minioRepository.storeDir(dirName, user, path);
    }

    public void delete(String filename, User user, String path){
        minioRepository.delete(filename, user, path);
    }
}
