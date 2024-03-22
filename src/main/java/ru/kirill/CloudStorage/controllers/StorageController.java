package ru.kirill.CloudStorage.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kirill.CloudStorage.models.User;
import ru.kirill.CloudStorage.security.UserDetailsImpl;
import ru.kirill.CloudStorage.services.MinioService;
import ru.kirill.CloudStorage.services.interfaces.StorageService;
import ru.kirill.CloudStorage.utils.PathUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class StorageController {

    private final StorageService storageService;


    @GetMapping
    public String mainPage(@RequestParam(value = "path", required = false) String path, Model model){
        User user = getUser();
        List<String> names;

        names = storageService.loadAll(user, Objects.requireNonNullElse(path, ""));

        model.addAttribute("names", names);
        model.addAttribute("path", path);
        if(path != null)
            model.addAttribute("nav", PathUtil.createMap(path));

        return "main";
    }

    @PostMapping("/upload/file")
    public String loadFile(@RequestParam("file") MultipartFile file,
                           @RequestParam("path") String path,
                           RedirectAttributes redirectAttributes) throws FileNotFoundException {

        User user = getUser();

        storageService.store(file, user, Objects.requireNonNullElse(path, ""));

        redirectAttributes.addAttribute("path", path);
        return "redirect:/";
    }

    @PostMapping("/upload/folder")
    public String loadFolder(@RequestParam("file") List<MultipartFile> files,
                           @RequestParam("path") String path,
                           RedirectAttributes redirectAttributes) throws FileNotFoundException {
        User user = getUser();

        for(MultipartFile file : files){
            System.out.println(file.getOriginalFilename());
        }


        storageService.uploadFolder(files, user, Objects.requireNonNullElse(path, ""));

        redirectAttributes.addAttribute("path", path);
        return "redirect:/";
    }

    @PostMapping("/newdir")
    public String loadNewDir(@RequestParam("dirname") String dirName,
                             @RequestParam("path") String path,
                             RedirectAttributes redirectAttributes){
        User user = getUser();

        storageService.storeDir(dirName, user, Objects.requireNonNullElse(path, ""));

        redirectAttributes.addAttribute("path", path);
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteFile(@RequestParam("path") String path,
                             @RequestParam("filename") String filename,
                             RedirectAttributes redirectAttributes){

        User user = getUser();

        if(path != null) {
            storageService.delete(filename, user, path);
            redirectAttributes.addAttribute("path", path);
        }
        else
            storageService.delete(filename, user, "");


        return "redirect:/";
    }

    @GetMapping("/file/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("path") String path,
                                                          @RequestParam("filename") String filename) throws IOException {

        User user = getUser();

        byte[] data = storageService.download(filename, user, Objects.requireNonNullElse(path, ""));
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    private User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return userDetails.getUser();
    }
}
