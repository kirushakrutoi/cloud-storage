package ru.kirill.CloudStorage.services.interfaces;


import org.springframework.web.multipart.MultipartFile;
import ru.kirill.CloudStorage.models.User;

import java.util.List;

public interface StorageService {
    public List<String> loadAll(User user, String path);
    public void store(MultipartFile file, User user, String path);
    public void uploadFolder(List<MultipartFile> files, User user, String path);
    public void storeDir(String dirName, User user, String path);
    public void delete(String filename, User user, String path);

}
