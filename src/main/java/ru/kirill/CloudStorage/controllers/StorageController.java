package ru.kirill.CloudStorage.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.kirill.CloudStorage.services.MinioService;

import java.io.FileNotFoundException;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class StorageController {

    private final MinioService minioService;


    @GetMapping
    public String mainPage(){
        return "main";
    }

    @PostMapping
    public String loadFile(@RequestParam("file") MultipartFile file) throws FileNotFoundException {
        minioService.store(file);
        return "redirect:/";
    }
}
