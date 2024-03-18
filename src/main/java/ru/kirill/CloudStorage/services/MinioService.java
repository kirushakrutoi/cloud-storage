package ru.kirill.CloudStorage.services;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.AllArgsConstructor;
import okio.Path;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kirill.CloudStorage.models.User;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MinioService {

    private final MinioClient minioClient;

    public List<String> loadAll(User user, String path){
        List<String> names = new ArrayList<>();

        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket("user-files").prefix("user-" + user.getId() + "-files/" + path).build());

        try {
            for(Result<Item> item : results){
                Item item1 = item.get();
                String[] name = item1.objectName().split("/");
                names.add(name[name.length-1]);
            }
        } catch (ServerException | InternalException | XmlParserException | InvalidResponseException |
                InvalidKeyException | NoSuchAlgorithmException | IOException | ErrorResponseException |
                InsufficientDataException e) {
            throw new RuntimeException(e);
        }

        return names;

    }

    public void store(MultipartFile file, User user, String path) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket("user-files")
                            .object("user-" + user.getId() + "-files/" + path  + file.getOriginalFilename())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build());

        } catch (ServerException | InternalException | XmlParserException | InvalidResponseException |
                 InvalidKeyException | NoSuchAlgorithmException | IOException | ErrorResponseException |
                 InsufficientDataException e) {
            throw new RuntimeException(e);
        }
    }

    public void storeDir(String dirName, User user, String path) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket("user-files")
                            .object("user-" + user.getId() + "-files/" + path + dirName + '/' + " ")
                            .stream(new ByteArrayInputStream(new byte[] {}), 0, -1)
                            .build());

        } catch (ServerException | InternalException | XmlParserException | InvalidResponseException |
                 InvalidKeyException | NoSuchAlgorithmException | IOException | ErrorResponseException |
                 InsufficientDataException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String filename, User user, String path){
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket("user-files")
                            .object("user-" + user.getId() + "-files/" + path  + filename)
                            .build());
        } catch (ServerException | InternalException | XmlParserException | InvalidResponseException |
                 InvalidKeyException | NoSuchAlgorithmException | IOException | ErrorResponseException |
                 InsufficientDataException e) {
            throw new RuntimeException(e);
        }
    }
}
